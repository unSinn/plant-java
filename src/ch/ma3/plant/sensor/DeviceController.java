package ch.ma3.plant.sensor;

import ch.ma3.plant.sensor.devices.Device;

public class DeviceController {
	private SerialConnector con;
	
	public DeviceController(SerialConnector con) {
		this.con = con;
	}

	public void sendValue(Device device, byte i) {
		con.sendString(device, i);
	}
	
	public void sendValue(Device device, byte i, byte j) {
		con.sendString(device, i , j);
	}

}
