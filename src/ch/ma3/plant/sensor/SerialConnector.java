package ch.ma3.plant.sensor;

import ch.ma3.plant.sensor.devices.Device;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialConnector {

	private SerialPort serialPort;
	private SensorDataParser parser;

	public SerialConnector() {

		serialPort = new SerialPort("/dev/ttyUSB0");
		parser = new SensorDataParser();

		try {
			serialPort.openPort();// Open serial port
			serialPort.setParams(57600, 8, 1, 0);// Set params.

			int mask = SerialPort.MASK_RXCHAR;
			serialPort.setEventsMask(mask);
			serialPort.addEventListener(new MySerialPortEventListener());
			System.out.println("Port " + serialPort.getPortName() + " open!");
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}
	}

	public void sendString(Device device, byte value) {
		try {
			System.out.println("Sending: " + device.getChar() + value);
			serialPort.writeBytes(device.getChar().getBytes());
			serialPort.writeByte(value);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class MySerialPortEventListener implements SerialPortEventListener {
		String buffer;

		public void serialEvent(SerialPortEvent sev) {
			if (sev.getEventValue() > 0) {
				try {
					buffer += new String(serialPort.readBytes());

					if (buffer.contains("\n")) {
						String[] list = buffer.split("\n");
						String line;
						line = list[0];
						if (line != null && line.startsWith("{")) {
							System.out.println(line);
							parser.parse(line);
						}
						String residue = "";
						for (int i = 1; i < list.length; i++) {
							residue += list[i];
						}
						buffer = residue;
					}
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
