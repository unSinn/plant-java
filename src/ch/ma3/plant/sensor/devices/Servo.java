package ch.ma3.plant.sensor.devices;

import ch.ma3.plant.sensor.DeviceController;

public class Servo implements Device, Runnable {

	private boolean running = true;
	private DeviceController controller;

	public Servo(DeviceController controller) {
		this.controller = controller;
	}

	public String getChar() {
		// TODO Auto-generated method stub
		return "S";
	}

	public void run() {
		while (running) {
			try {
				byte i = (byte) (Math.random() * 128);
				controller.sendValue(this, i);
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
