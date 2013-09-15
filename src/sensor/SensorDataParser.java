package sensor;

import java.util.Map;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

public class SensorDataParser {

	private JSONParser parser;

	public SensorDataParser() {
		JsonParserFactory factory = JsonParserFactory.getInstance();
		parser = factory.newJsonParser();
	}

	public void parse(String s) {
		/*
		 * Map jsonData = parser.parseJson(s);
		 * 
		 * String value = (String) jsonData.get("name");
		 */
	}

}
