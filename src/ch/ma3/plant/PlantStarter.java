package ch.ma3.plant;

import it.sauronsoftware.cron4j.Scheduler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.ma3.plant.relay.Relay;
import ch.ma3.plant.relay.RelayCronJob;
import ch.ma3.plant.sensor.DeviceController;
import ch.ma3.plant.sensor.SensorDataWriterCronJob;
import ch.ma3.plant.sensor.SerialConnector;
import ch.ma3.plant.sensor.devices.LightStatusLED;
import ch.ma3.plant.sensor.devices.Watchdog;

public class PlantStarter {
	private static Logger log = LogManager.getLogger(PlantStarter.class);

	private Scheduler scheduler;
	private SerialConnector serial;
	private Relay relay;
	private DeviceController controller;

	public PlantStarter() {

	}

	public void start() {
		log.info("Starting Services up...");
		serial = new SerialConnector();
		scheduler = new Scheduler();
		controller = new DeviceController(serial);
		relay = new Relay();
		setupCron(relay);
		setupWatchDog();
		setupLightStatusLED();
		log.info("Started.");
	}

	private void setupLightStatusLED() {
		LightStatusLED led = new LightStatusLED(controller, relay);
	}

	private void setupWatchDog() {
		Watchdog w = new Watchdog(controller);
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void stop() {

		if (serial != null) {
			serial.close();
		}
		scheduler.stop();
	}

	private void setupCron(Relay relay) {
		RelayCronJob.forRelay(relay).pin(Relay.LIGHTPIN).turnOn().at("* 22-9 * * *")
				.schedule(scheduler);
		RelayCronJob.forRelay(relay).pin(Relay.LIGHTPIN).turnOff().at("* 10-21 * * *") // 10:00
																			// -
																			// 21:59
				.schedule(scheduler);

		SensorDataWriterCronJob.at("*/5 * * * *").schedule(scheduler);

		scheduler.start();
	}

}
