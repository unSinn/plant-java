package ch.ma3.plant.sensor.devices;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.ma3.plant.sensor.DeviceController;

public class Servo implements Device, Runnable {

	private static Logger log = LogManager.getLogger(Servo.class);
	private boolean running = true;
	private DeviceController controller;

	public Servo(DeviceController controller) {
		this.controller = controller;
	}

	public char getChar() {
		return 'S';
	}

	public void run() {
		while (running) {
			try {
				byte i = (byte) (Math.random() * 128);
				controller.sendValue(this, i);
				Thread.sleep(500);
			} catch (InterruptedException e) {
				log.error(e);
			}
		}

	}

}
