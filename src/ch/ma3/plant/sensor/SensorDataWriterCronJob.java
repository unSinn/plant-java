package ch.ma3.plant.sensor;

import it.sauronsoftware.cron4j.Scheduler;

public class SensorDataWriterCronJob implements Runnable {

	private String cronString;

	@Override
	public void run() {
		SensorValues.getInstance().writeToFile();
	}

	private SensorDataWriterCronJob(String cronString) {
		this.cronString = cronString;
	}

	public static SensorDataWriterCronJob at(String cronString) {
		return new SensorDataWriterCronJob(cronString);
	}

	public void schedule(Scheduler scheduler) {
		scheduler.schedule(cronString, this);
	}
}
