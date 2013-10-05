package ch.ma3.plant.sensor.devices;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.ma3.plant.relay.Relay;
import ch.ma3.plant.sensor.DeviceController;

public class LightStatusLED implements Runnable, Device{
	
	private static Logger log = LogManager.getLogger(LightStatusLED.class);

	private boolean running = true;
	private DeviceController controller;
	private Relay relay;
	
	public LightStatusLED(DeviceController controller, Relay relay) {
		this.controller = controller;
		this.relay = relay;
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		log.info("LightStatusLED started.");
		while (running) {
			try {
				if(relay.getSwitchState(Relay.LIGHTPIN)){
					controller.sendValue(this, (byte)255);
				}
				else{
					controller.sendValue(this, (byte)0);
				}
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				log.error(e);
			}
		}
	}

	@Override
	public char getChar() {
		return 'B';
	}

}
