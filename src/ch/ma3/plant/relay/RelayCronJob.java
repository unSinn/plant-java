package ch.ma3.plant.relay;

import it.sauronsoftware.cron4j.Scheduler;

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
		relay.setSwitch(pin, onOff);
	}

	public void schedule(Scheduler scheduler) {
		scheduler.schedule(cronString, this);
	}
}
