package club.sgen.network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class DataRequester {
	public static final String serverURL = "http://ec2-54-213-12-61.us-west-2.compute.amazonaws.com:8080/bettingpop_s";

	public static void login(String id, String password,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "id", id);
		addParam(params, "password", makePassword(password));
		execute(serverURL + "/servlets/login", params, callback,
				new DataParser("login") {
					@Override
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						map.put("success", data.getBoolean("success"));
					}
				});
	}

	public static void registerID(final String id, String password,
			String email, final String image,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "id", id);
		addParam(params, "password", makePassword(password));
		addParam(params, "email", email);
		execute(serverURL + "/servlets/registerID", params, callback,
				new DataParser("registerID") {
					@Override
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						map.put("success", data.getBoolean("success"));
					}

					@Override
					public void afterParsing(HashMap<String, Object> map) {
						Object success = map.get("success");
						// user가 등록되면 프로필을 등록한다.
						if (success != null && (Boolean) success) {
							if (image != null)
								executeFileUpload(image,
										"UPDATE user SET image = ? WHERE id = \"" + id
												+ "\"",id);
						}
					}
				});
	}

	private static void executeFileUpload(String fileName, String query,String userID) {
		new AsyncExecutor<String>().setCallable(
				new FileUploadCallable(fileName, query,userID)).execute();
	}

	private static void execute(String url, ArrayList<NameValuePair> params,
			AsyncCallback<HashMap<String, Object>> callback, DataParser parser) {
		new AsyncExecutor<HashMap<String, Object>>()
				.setCallable(new EntityDownloadCallable(url, params, parser))
				.setCallback(callback).execute();
	}

	private static void addParam(ArrayList<NameValuePair> params, String key,
			String value) {
		if (params == null)
			return;
		params.add(new BasicNameValuePair(key, value));
	}

	private static String makePassword(String org) {
		String result = "";
		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(org.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
}
