package club.sgen.network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.sql.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import club.sgen.entity.Betting;
import club.sgen.entity.Product;
import club.sgen.entity.User;

public abstract class DataRequester {
	public static final String serverURL = "http://ec2-54-213-12-61.us-west-2.compute.amazonaws.com:8080/bettingpop_s";
	public static final DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
	
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
	
	
	//wowowowo
	
	public static User toUser(JSONObject object) throws JSONException, ParseException
	{
		User user = new User();
		user.setId(object.getString("user_id"));
		user.setUser_key(object.getInt("user_key"));
		user.setName(object.getString("name"));
		user.setEmail(object.getString("email"));
		user.setPoint(object.getInt("point"));
		user.setImage(object.getString("image"));
		
		return user;
	}
	
	public static Product toProduct(JSONObject object) throws JSONException, ParseException
	{
		Product product = new Product();
		product.setName(object.getString("product_name"));
		product.setType(object.getString("product_type"));
		product.setImage(object.getString("product_image"));
		product.setPrice(object.getInt("product_price"));
		product.setDescription(object.getString("product_description"));
		product.setTerm_start(df.parse(object.getString("product_term_start")));
		product.setTerm_end(df.parse(object.getString("product_term_end")));
		product.setProduct_key(object.getInt("product_key"));
		return product;
	}
	
	public static Betting toBetting(JSONObject object) throws JSONException, ParseException
	{
		Product product = new Product();
		product.setName(object.getString("product_name"));
		product.setType(object.getString("product_type"));
		product.setImage(object.getString("product_image"));
		product.setPrice(object.getInt("product_price"));
		product.setDescription(object.getString("product_description"));
		product.setTerm_start(df.parse(object.getString("product_term_start")));
		product.setTerm_end(df.parse(object.getString("product_term_end")));
		product.setProduct_key(object.getInt("product_key"));
		
		Betting betting = new Betting();
		betting.setProduct_key(object.getInt("product_key"));
		betting.setBetting_key(object.getInt("betting_key"));
		betting.setUser_id(object.getString("bettubg_user_id"));
		betting.setName(object.getString("betting_name"));
		betting.setGoal(object.getString("betting_goal"));
		betting.setType(object.getString("betting_type"));
		betting.setTerm_start(df.parse(object.getString("betting_term_start")));
		betting.setTerm_end(df.parse(object.getString("betting_term_end")));
		betting.setProduct_key(object.getInt("product_key"));
		betting.setProduct(product);
		
		return betting;
	}
	
	public static void showAllbettinglist(AsyncCallback<HashMap<String, Object>> callback){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		execute(serverURL + "/servlets/showAllbettinglist", params, callback,
				new DataParser("showAllbettinglist"){
			public void addEntities(HashMap<String, Object> map, JSONObject data) throws JSONException{
				JSONArray success = data.getJSONArray("success");
				ArrayList<Betting> bettings = new ArrayList<Betting>();
				for(int i = 0; i < success.length();i++){
					try {
						bettings.add(toBetting(success.getJSONObject(i)));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				map.put("bettings", bettings);
			}
		});
	}
	
	public static void showBettingInfo(final String betting_key,  AsyncCallback<HashMap<String, Object>> callback){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "betting_key", betting_key);
		execute(serverURL + "/servlets/showBettingInfo", params, callback,
				new DataParser("showBettingInfo"){
			public void addEntities(HashMap<String, Object> map, JSONObject data) throws JSONException{
				try {
					map.put("betting", toBetting(data.getJSONObject("success")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public static void showJoinbettinglist(final String id, AsyncCallback<HashMap<String, Object>> callback){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showJoinbettinglist", params, callback,
				new DataParser("showJoinbettinglist"){
			public void addEntities(HashMap<String, Object> map, JSONObject data) throws JSONException{
				JSONArray success = data.getJSONArray("success");
				ArrayList<Betting> bettings = new ArrayList<Betting>();
				for(int i = 0; i < success.length();i++){
					try {
						bettings.add(toBetting(success.getJSONObject(i)));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				map.put("bettings", bettings);
			}
		});
	}
	
	
	public static void showMakebettinglist(final String id, AsyncCallback<HashMap<String, Object>> callback){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showMakebettinglist", params, callback,
				new DataParser("showMakebettinglist"){
			public void addEntities(HashMap<String, Object> map, JSONObject data) throws JSONException{
				JSONArray success = data.getJSONArray("success");
				ArrayList<Betting> bettings = new ArrayList<Betting>();
				for(int i = 0; i < success.length();i++){
					try {
						bettings.add(toBetting(success.getJSONObject(i)));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				map.put("bettings", bettings);
			}
		});
	}
	
	
	public static void showMyproductlist(final String id, AsyncCallback<HashMap<String, Object>> callback){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showMyproductlist", params, callback,
				new DataParser("showMyproductlist"){
			public void addEntities(HashMap<String, Object> map, JSONObject data) throws JSONException{
				JSONArray success = data.getJSONArray("success");
				ArrayList<Product> products = new ArrayList<Product>();
				for(int i = 0; i < success.length();i++){
					try {
						products.add(toProduct(success.getJSONObject(i)));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				map.put("product", products);
			}
		});
	}
	
	public static void showProductInfo(final String product_key, AsyncCallback<HashMap<String, Object>> callback){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "product_key", product_key);
		execute(serverURL + "/servlets/showProductInfo", params, callback,
				new DataParser("showBettings"){
			public void addEntities(HashMap<String, Object> map, JSONObject data) throws JSONException{
				try {
					map.put("product", toProduct(data.getJSONObject("success")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
			}
		});
	}
	
	public static void showUserInfo(final String id, AsyncCallback<HashMap<String, Object>> callback){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showUserInfo", params, callback,
				new DataParser("showBettingInfo"){
			public void addEntities(HashMap<String, Object> map, JSONObject data) throws JSONException{
				try {
					map.put("user", toUser(data.getJSONObject("success")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void showFriendlist(final String id, AsyncCallback<HashMap<String, Object>> callback){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showFriendlist", params, callback, 
				new DataParser("showFriendlist"){
			public void addEntities(HashMap<String, Object> map, JSONObject data) throws JSONException{
				JSONArray success = data.getJSONArray("success");
				ArrayList<User> users = new ArrayList<User>();
				for(int i = 0; i < success.length();i++){
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
	
	public static void showFriendrequestlist(final String id, AsyncCallback<HashMap<String, Object>> callback){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		addParam(params, "user_id", id);
		execute(serverURL + "/servlets/showFriendrequestlist", params, callback,
				new DataParser("showFriendrequestlist"){public void addEntities(HashMap<String, Object> map, JSONObject data) throws JSONException{
					JSONArray success = data.getJSONArray("success");
					ArrayList<User> users = new ArrayList<User>();
					for(int i = 0; i < success.length();i++){
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
	
	
	public static void changePassword(final String id, String currentPassword, String newPassword,
			AsyncCallback<HashMap<String, Object>> callback){
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
