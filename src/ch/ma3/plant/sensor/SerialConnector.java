package ch.ma3.plant.sensor;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.ma3.plant.sensor.devices.Device;

public class SerialConnector {

	private static Logger log = LogManager.getLogger(SerialConnector.class);
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
			log.info("Port " + serialPort.getPortName() + " open!");
		} catch (SerialPortException ex) {
			log.error(ex);
		}
	}

	public void sendString(Device device, byte value) {
		try {
			log.info("Sending: " + device.getChar() + value);
			serialPort.writeBytes(device.getChar().getBytes());
			serialPort.writeByte(value);
		} catch (SerialPortException e) {
			log.error(e);
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
							parser.parse(line);
						}
						String residue = "";
						for (int i = 1; i < list.length; i++) {
							residue += list[i];
						}
						buffer = residue;
					}
				} catch (SerialPortException e) {
					log.error(e.getStackTrace());
				}
			}
		}
	}

	public void close() {
		try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			log.error(e.getStackTrace());
		}
	}
}
