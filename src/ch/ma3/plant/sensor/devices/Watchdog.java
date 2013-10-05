package ch.ma3.plant.sensor.devices;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import ch.ma3.plant.sensor.DeviceController;

public class Watchdog implements Device, Runnable {

	private static Logger log = LogManager.getLogger(Watchdog.class);

	private boolean running = true;
	private DeviceController controller;
	private DateTime dt;

	public Watchdog(DeviceController controller) {
		this.controller = controller;
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		log.info("Watchdog started.");
		while (running) {
			try {
				dt = DateTime.now();
				controller.sendValue(this, (byte) dt.getHourOfDay(),
						(byte) dt.getMinuteOfHour());
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				log.error(e);
			}
		}
	}

	@Override
	public char getChar() {
		return 'T';
	}

}
