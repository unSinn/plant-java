package ch.ma3.plant;

import it.sauronsoftware.cron4j.Scheduler;
import ch.ma3.plant.relay.Relay;
import ch.ma3.plant.relay.RelayArgsParser;
import ch.ma3.plant.relay.RelayCronJob;
import ch.ma3.plant.sensor.DeviceController;
import ch.ma3.plant.sensor.SerialConnector;

import com.phidgets.PhidgetException;

public class Main {

	private static boolean running = true;

	public static void main(String[] args) {

		try {

			SerialConnector serial = new SerialConnector();
			setupArduino(serial);
			setupRelay(args);

			System.out.println("Started.");
			while (running) {
				// run forever
			}

		} catch (PhidgetException e) {
			System.out.println(e.getDescription() + "Phidget Error"
					+ e.getErrorNumber());
			e.printStackTrace();
		}

	}

	private static void setupArduino(SerialConnector serial) {
		DeviceController controller = new DeviceController(serial);
	}

	private static void setupRelay(String[] args) throws PhidgetException {
		Relay relay = new Relay();
		RelayArgsParser.parseArgs(relay, args);
		setupCron(relay);
	}

	private static void setupCron(Relay relay) {
		Scheduler s = new Scheduler();
		RelayCronJob.forRelay(relay).pin(0).turnOn().at("00 10 * * *")
				.schedule(s);
		RelayCronJob.forRelay(relay).pin(0).turnOff().at("00 22 * * *")
				.schedule(s);
		s.start();
	}

}
