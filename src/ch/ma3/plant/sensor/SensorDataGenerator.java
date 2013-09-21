package ch.ma3.plant.sensor;

import java.util.HashMap;
import java.util.Map;

import com.json.generators.JSONGenerator;
import com.json.generators.JsonGeneratorFactory;

public class SensorDataGenerator {

	private JSONGenerator generator;

	public SensorDataGenerator() {
		JsonGeneratorFactory factory = JsonGeneratorFactory.getInstance();
		generator = factory.newJsonGenerator();

	}

	public String makeJSON(Sensor sensor) {
		Map<String, Object> data = new HashMap<>();

		data.put("name", sensor.getName());
		data.put("value", sensor.getValue());
		data.put("unit", sensor.getUnit());

		return generator.generateJson(data);
	}
}
