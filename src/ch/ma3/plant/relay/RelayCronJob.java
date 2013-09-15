package ch.ma3.plant.relay;

import it.sauronsoftware.cron4j.Scheduler;

import com.phidgets.PhidgetException;

public class RelayCronJob implements Runnable {

	private String cronString;
	private Relay relay;
	private boolean onOff;
	private int pin;

	private RelayCronJob(Relay relay) {
		this.relay = relay;
	}

	public static RelayCronJob forRelay(Relay relay) {
		return new RelayCronJob(relay);
	}

	public RelayCronJob at(String cronString) {
		this.cronString = cronString;
		return this;
	}

	public RelayCronJob pin(int pin) {
		this.pin = pin;
		return this;
	}

	public RelayCronJob turnOn() {
		onOff = true;
		return this;
	}

	public RelayCronJob turnOff() {
		onOff = false;
		return this;
	}

	public void run() {
		try {
			relay.setSwitch(pin, onOff);
			System.out.println("Turning switch " + pin + " to " + onOff);
		} catch (PhidgetException e) {
			System.out.println(e.getDescription()
					+ "Phidget Error: Cannot set relay value."
					+ e.getErrorNumber());
			e.printStackTrace();
		}
	}

	public void schedule(Scheduler scheduler) {
		scheduler.schedule(cronString, this);
	}
}
