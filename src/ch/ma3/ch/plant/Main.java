package ch.ma3.ch.plant;

import it.sauronsoftware.cron4j.Scheduler;
import ch.ma3.ch.plant.relay.Relay;
import ch.ma3.ch.plant.relay.RelayArgsParser;
import ch.ma3.ch.plant.relay.RelayCronJob;

import com.phidgets.PhidgetException;

public class Main {

	private static boolean running;

	public static void main(String[] args) {

		try {

			Relay relay = new Relay();

			RelayArgsParser.parseArgs(relay, args);

			Scheduler s = new Scheduler();
			RelayCronJob.forRelay(relay).pin(1).turnOn().at("0 10 * * *")
					.schedule(s);
			RelayCronJob.forRelay(relay).pin(2).turnOff().at("0 22 * * *")
					.schedule(s);

			
			
			s.start();
			System.out.println("Started.");
			while (running) {
				// run forever
			}
			s.stop();

		} catch (PhidgetException e) {
			System.out.println(e.getDescription() + "Phidget Error"
					+ e.getErrorNumber());
			e.printStackTrace();
		}

	}

}
