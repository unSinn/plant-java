package ch.ma3.plant.sensor;

import ch.ma3.plant.sensor.devices.Device;
import ch.ma3.plant.sensor.devices.Servo;

public class DeviceController {
	private SerialConnector con;
	private Servo servo;

	public DeviceController(SerialConnector con) {
		this.con = con;

		servo = new Servo(this);
		Thread t = new Thread(servo);
		t.start();
	}

	public void sendValue(Device device, byte i) {
		con.sendString(device, i);
	}

}
