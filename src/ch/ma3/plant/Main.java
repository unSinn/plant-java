package ch.ma3.plant;

import sensor.SerialConnector;
import it.sauronsoftware.cron4j.Scheduler;
import ch.ma3.plant.relay.Relay;
import ch.ma3.plant.relay.RelayArgsParser;
import ch.ma3.plant.relay.RelayCronJob;

import com.phidgets.PhidgetException;

public class Main {

	private static boolean running = true;

	public static void main(String[] args) {

		try {

			SerialConnector serial = new SerialConnector();

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

	private static void setupRelay(String[] args) throws PhidgetException {
		Relay relay = new Relay();
		RelayArgsParser.parseArgs(relay, args);
		setupCron(relay);
	}

	private static void setupCron(Relay relay) {
		Scheduler s = new Scheduler();
		RelayCronJob.forRelay(relay).pin(1).turnOn().at("0 10 * * *")
				.schedule(s);
		RelayCronJob.forRelay(relay).pin(1).turnOff().at("0 22 * * *")
				.schedule(s);
		s.start();
	}

}
