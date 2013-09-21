package ch.ma3.plant.sensor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SensorValues {

	private static Logger log = LogManager.getLogger(SensorValues.class);

	private static SensorValues instance;
	private HashMap<String, Sensor> values;
	private SensorDataGenerator generator;
	private File f;
	private FileWriter fw;

	private SensorValues() {
		values = new HashMap<>();
		generator = new SensorDataGenerator();
		f = new File("sensors.json");

		try {
			fw = new FileWriter(f);
		} catch (IOException e) {
			log.error(e);
		}
	}

	public static SensorValues getInstance() {
		if (instance == null) {
			instance = new SensorValues();
		}
		return instance;
	}

	public void writeToFile() {
		StringBuilder sbf = new StringBuilder();
		for (Sensor sensor : values.values()) {
			sbf.append(generator.makeJSON(sensor));
		}

		try {
			f.createNewFile();
			fw.write(sbf.toString());
			fw.flush();
		} catch (IOException e) {
			log.error(e);
		}

	}

	public void setValue(String id, Sensor s) {
		values.put(id, s);
	}
}
