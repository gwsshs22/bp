package club.sgen.network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import club.sgen.entity.Betting;
import club.sgen.entity.Pop;
import club.sgen.entity.Product;
import club.sgen.entity.User;

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
				new DataParser("registerBetting") {
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

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

	public static Pop toPop(JSONObject object) throws JSONException,
			ParseException {
		Pop pop = new Pop();

		User user = toUser(object);
		Product product = toProduct(object);
		Betting betting = toBetting(object);
		pop.setAgree(object.getInt("agree"));
		pop.setDisagree(object.getInt("disagree"));
		pop.setUser(user);
		pop.setBetting(betting);
		pop.setProduct(product);
		return pop;
	}

	public static User toUser(JSONObject object) throws JSONException,
			ParseException {
		User user = new User();
		user.setId(object.getString("user_id"));
		user.setUser_key(object.getInt("user_key"));
		user.setName(object.getString("name"));
		user.setEmail(object.getString("email"));
		user.setPoint(object.getInt("point"));
		user.setImage(object.getString("image"));

		return user;
	}

	public static Product toProduct(JSONObject object) throws JSONException,
			ParseException {
		Product product = new Product();
		product.setName(object.getString("product_name"));
		product.setType(Product.TYPE.getTypeByString(object
				.getString("product_type")));
		product.setImage(object.getString("product_image"));
		product.setPrice(object.getInt("product_price"));
		product.setDescription(object.getString("product_description"));
		product.setTerm_start(parseDateString(object
				.getString("product_term_start")));
		product.setTerm_end(parseDateString(object
				.getString("product_term_end")));
		product.setProduct_key(object.getInt("product_key"));
		return product;
	}

	private static Date parseDateString(String string) {
		if (string != null)
			try {
				return df.parse(string);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}

	public static Betting toBetting(JSONObject object) throws JSONException,
			ParseException {
		Product product = new Product();
		product.setName(object.getString("product_name"));
		product.setType(Product.TYPE.getTypeByString(object
				.getString("product_type")));
		product.setImage(object.getString("product_image"));
		product.setPrice(object.getInt("product_price"));
		product.setDescription(object.getString("product_description"));
		product.setTerm_start(parseDateString(object
				.getString("product_term_start")));
		product.setTerm_end(parseDateString(object
				.getString("product_term_end")));
		product.setProduct_key(object.getInt("product_key"));

		Betting betting = new Betting();
		betting.setProduct_key(object.getInt("product_key"));
		betting.setBetting_key(object.getInt("betting_key"));
		betting.setUserId(object.getString("betting_user_id"));
		betting.setName(object.getString("betting_name"));
		betting.setGoal(object.getString("betting_goal"));
		betting.setType(Betting.TYPE.getTypeByString(object
				.getString("betting_type")));
		betting.setTerm_start(parseDateString(object
				.getString("betting_term_start")));
		betting.setTerm_end(parseDateString(object
				.getString("betting_term_end")));
		betting.setProduct_key(object.getInt("product_key"));
		betting.setProduct(product);

		return betting;
	}

	public static void showAllbettinglist(
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		execute(serverURL + "/servlets/showAllbettinglist", params, callback,
				new DataParser("showAllbettinglist") {
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						JSONArray success = data.getJSONArray("success");
						ArrayList<Pop> pops = new ArrayList<Pop>();
						for (int i = 0; i < success.length(); i++) {
							try {
								pops.add(toPop(success.getJSONObject(i)));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						map.put("pops", pops);
					}
				});
	}
	
	public static void showJoinbettinglist(final String id,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showJoinbettinglist", params, callback,
				new DataParser("showJoinbettinglist") {
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						JSONArray success = data.getJSONArray("success");
						ArrayList<Pop> pops = new ArrayList<Pop>();
						for (int i = 0; i < success.length(); i++) {
							try {
								pops.add(toPop(success.getJSONObject(i)));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						map.put("pops", pops);
					}
				});
	}
	
	public static void showFriendbettinglist(final String id,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showFriendbettinglist", params, callback,
				new DataParser("showFriendbettinglist") {
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						JSONArray success = data.getJSONArray("success");
						ArrayList<Pop> pops = new ArrayList<Pop>();
						for (int i = 0; i < success.length(); i++) {
							try {
								pops.add(toPop(success.getJSONObject(i)));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						map.put("pops", pops);
					}
				});
	}
	
	public static void showMakebettinglist(final String id,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showMakebettinglist", params, callback,
				new DataParser("showMakebettinglist") {
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						JSONArray success = data.getJSONArray("success");
						ArrayList<Pop> pops = new ArrayList<Pop>();
						for (int i = 0; i < success.length(); i++) {
							try {
								pops.add(toPop(success.getJSONObject(i)));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						map.put("pops", pops);
					}
				});
	}

	public static void showBettingInfo(final String betting_key,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "betting_key", betting_key);
		execute(serverURL + "/servlets/showBettingInfo", params, callback,
				new DataParser("showBettingInfo") {
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						try {
							map.put("pop",toPop(data.getJSONObject("success")));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public static void showMyproductlist(final String id,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showMyproductlist", params, callback,
				new DataParser("showMyproductlist") {
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						JSONArray success = data.getJSONArray("success");
						ArrayList<Product> products = new ArrayList<Product>();
						for (int i = 0; i < success.length(); i++) {
							try {
								products.add(toProduct(success.getJSONObject(i)));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						map.put("products", products);
					}
				});
	}

	public static void showProductInfo(final String product_key,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "product_key", product_key);
		execute(serverURL + "/servlets/showProductInfo", params, callback,
				new DataParser("showProductInfo") {
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						try {
							map.put("product",
									toProduct(data.getJSONObject("success")));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						;
					}
				});
	}

	public static void showUserInfo(final String id,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showUserInfo", params, callback,
				new DataParser("showUserInfo") {
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						try {
							map.put("user",
									toUser(data.getJSONObject("success")));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public static void showFriendlist(final String id,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showFriendlist", params, callback,
				new DataParser("showFriendlist") {
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						JSONArray success = data.getJSONArray("success");
						ArrayList<User> users = new ArrayList<User>();
						for (int i = 0; i < success.length(); i++) {
							try {
								users.add(toUser(success.getJSONObject(i)));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						map.put("users", users);
					}
				});
	}

	public static void showFriendrequestlist(final String id,
			AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showFriendrequestlist", params,
				callback, new DataParser("showFriendrequestlist") {
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						JSONArray success = data.getJSONArray("success");
						ArrayList<User> users = new ArrayList<User>();
						for (int i = 0; i < success.length(); i++) {
							try {
								users.add(toUser(success.getJSONObject(i)));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						map.put("users", users);
					}
				});
	}

	public static void changePassword(final String id, String currentPassword,
			String newPassword, AsyncCallback<HashMap<String, Object>> callback) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "id", id);
		addParam(params, "currentPassword", makePassword(currentPassword));
		addParam(params, "newPassword", makePassword(newPassword));
		execute(serverURL + "/servlets/changePassword", params, callback,
				new DataParser("changePassword") {
					public void addEntities(HashMap<String, Object> map,
							JSONObject data) throws JSONException {
						map.put("success", data.getBoolean("success"));
					}
				});
	}

}
