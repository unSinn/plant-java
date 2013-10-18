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
			serialPort.setParams(SerialPort.BAUDRATE_57600,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);// Set params.

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
			byte[] bytes = new byte[2];
			bytes[0] = (byte) device.getChar();
			bytes[1] = value;
			sendBytes(bytes);
		} catch (SerialPortException e) {
			log.error(e);
		}
	}

	public void sendString(Device device, byte i, byte j) {
		try {
			byte[] bytes = new byte[3];
			bytes[0] = (byte) device.getChar();
			bytes[1] = i;
			bytes[2] = j;
			sendBytes(bytes);
		} catch (SerialPortException e) {
			log.error(e);
		}
	}

	private void sendBytes(byte[] bytes) throws SerialPortException {
		synchronized (serialPort) {
			serialPort.writeBytes(bytes);
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
