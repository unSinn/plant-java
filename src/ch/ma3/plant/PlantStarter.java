package ch.ma3.plant;

import it.sauronsoftware.cron4j.Scheduler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.ma3.plant.relay.Relay;
import ch.ma3.plant.relay.RelayCronJob;
import ch.ma3.plant.sensor.DeviceController;
import ch.ma3.plant.sensor.SensorDataWriterCronJob;
import ch.ma3.plant.sensor.SerialConnector;

import com.phidgets.PhidgetException;

public class PlantStarter {
	private static Logger log = LogManager.getLogger(PlantStarter.class);

	private Scheduler scheduler;
	private SerialConnector serial;
	private Relay relay;

	public PlantStarter() {
		scheduler = new Scheduler();
	}

	public void start() {
		log.info("Starting Services up...");
		serial = new SerialConnector();
		setupArduino(serial);
		relay = new Relay();
		setupCron(relay);
		log.info("Started.");
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

	private void setupArduino(SerialConnector serial) {
		new DeviceController(serial);
	}

	private void setupCron(Relay relay) {
		RelayCronJob.forRelay(relay).pin(0).turnOn().at("00 10 * * *")
				.schedule(scheduler);
		RelayCronJob.forRelay(relay).pin(0).turnOff().at("00 22 * * *")
				.schedule(scheduler);

		SensorDataWriterCronJob.at("*/5 * * * *").schedule(scheduler);

		scheduler.start();
	}

}
