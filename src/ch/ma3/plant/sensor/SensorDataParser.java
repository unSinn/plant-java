package ch.ma3.plant.sensor;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.json.exceptions.JSONParsingException;
import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

public class SensorDataParser {

	private static Logger log = LogManager.getLogger(SensorDataParser.class);

	private JSONParser parser;

	public SensorDataParser() {
		JsonParserFactory factory = JsonParserFactory.getInstance();
		parser = factory.newJsonParser();
	}

	public void parse(String s) {
		try {

			Map jsonData = parser.parseJson(s);
			Sensor sensor = new Sensor();

			String name = (String) jsonData.get("name");
			sensor.setName((String) jsonData.get("name"));
			sensor.setUnit((String) jsonData.get("unit"));
			sensor.setValue(Float.parseFloat((String) jsonData.get("value")));

			SensorValues.getInstance().setValue(name, sensor);
		} catch (JSONParsingException e) {
			log.info(e);
		}

	}

}
