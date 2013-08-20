package club.sgen.network;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class DataParser {
	private String type;

	public DataParser(String type) {
		this.type = type;
	}

	public HashMap<String, Object> parseData(String str) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		if (str != null) {
			try {
				JSONObject obj = new JSONObject(str);
				addEntities(map, obj);
				map.put("error_occured", false);
			} catch (JSONException e) {
				e.printStackTrace();
				map.put("error_occured", true);
			}
		} else
			map.put("error_occured", true);
		afterParsing(map);
		return map;
	}

	public abstract void addEntities(HashMap<String, Object> map,
			JSONObject data) throws JSONException;

	public void afterParsing(HashMap<String, Object> map) {
	}
}
