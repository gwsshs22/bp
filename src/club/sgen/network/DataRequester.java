package club.sgen.network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import club.sgen.entity.Betting;
import club.sgen.entity.Product;

public abstract class DataRequester {
	public static final String serverURL = "http://ec2-54-213-12-61.us-west-2.compute.amazonaws.com:8080/bettingpop_s";
	private static final DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");

	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	public static Date toDate(int y, int M, int d, int h, int m, int s) {
		StringBuilder sb = new StringBuilder();

		String arr[] = new String[6];
		arr[0] = String.valueOf(y);
		arr[1] = String.valueOf(M);
		arr[2] = String.valueOf(d);
		arr[3] = String.valueOf(h);
		arr[4] = String.valueOf(m);
		arr[5] = String.valueOf(s);
		for (int i = 4 - arr[0].length(); i > 0; i--)
			arr[0] += "0" + arr[0];
		for (int i = 1; i < 6; i++)
			for (int j = 2 - arr[i].length(); j > 0; j--)
				arr[i] += "0" + arr[i];
		for (int i = 0; i < 6; i++)
			sb.append(arr[i]);
		try {
			return df.parse(sb.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getCurrentDate();
	}

	public static void registerBetting(Betting betting, Product product,
			final String image, AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		parseBetting(params, betting);
		parseProduct(params, product);
		execute(serverURL + "/servlets/registerBetting", params, callback,
				new DataParser("registerID") {
					@Override
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						map.put("success", data.getBoolean("success"));
						map.put("product_key", data.getInt("product_key"));
					}

					@Override
					public void afterParsing(HashMap<String, Object> map) {
						Object success = map.get("success");
						// user가 등록되면 프로필을 등록한다.
						if (success != null && (Boolean) success) {
							if (image != null) {
								int product_key = (Integer) map
										.get("product_key");
								executeFileUpload(image,
										"UPDATE product SET image = ? WHERE product_key = \""
												+ product_key + "\"",
										String.valueOf(product_key));
							}
						}
					}
				});
	}

	public static void registerBetting(Betting betting, int product_key,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		parseBetting(params, betting);
		execute(serverURL + "/servlets/registerBetting", params, callback,
				new DataParser("registerID") {
					@Override
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						map.put("success", data.getBoolean("success"));
					}

				});
	}

	private static void parseBetting(ArrayList<NameValuePair> params,
			Betting betting) {
		if (betting == null)
			return;
		addParam(params, "description", betting.getDescription());
		addParam(params, "goal", betting.getGoal());
		addParam(params, "name", betting.getName());
		addParam(params, "max_number", String.valueOf(betting.getMax_number()));
		addParam(params, "term_end", df.format(betting.getTerm_end()));
		addParam(params, "term_start", df.format(getCurrentDate()));
		addParam(params, "type", betting.getType().toString());
	}

	private static void parseProduct(ArrayList<NameValuePair> params,
			Product product) {
		if (product == null)
			return;
		addParam(params, "prodcut_description", product.getDescription());
		addParam(params, "product_name", product.getName());
		addParam(params, "product_term_start", df.format(getCurrentDate()));
		addParam(params, "product_term_end", df.format(product.getTerm_start()));
		addParam(params, "product_type", String.valueOf(product.getType()));
		addParam(params, "product_price", String.valueOf(product.getPrice()));
	}

	public static void updateResultImage(int betting_key, String image) {
		executeFileUpload(image,
				"UPDATE betting SET result_image = ? WHERE betting_key = \""
						+ betting_key + "\"", String.valueOf(betting_key));
	}

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
										"UPDATE user SET image = ? WHERE id = \""
												+ id + "\"", id);
						}
					}
				});
	}

	private static void executeFileUpload(String fileName, String query,
			String userID) {
		new AsyncExecutor<String>().setCallable(
				new FileUploadCallable(fileName, query, userID)).execute();
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
