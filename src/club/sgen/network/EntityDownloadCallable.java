package club.sgen.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class EntityDownloadCallable implements
		Callable<HashMap<String, Object>> {
	private String url;
	private DataParser parser;
	private ArrayList<NameValuePair> params;

	public EntityDownloadCallable(String url, ArrayList<NameValuePair> params,
			DataParser parser) {
		this.url = url;
		this.params = params;
		this.parser = parser;
	}

	@Override
	public HashMap<String, Object> call() throws Exception {
		HttpClient client = new DefaultHttpClient();
		InputStream is = null;
		String result = null;
		try {
			HttpParams httpParams = client.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
			HttpPost post = new HttpPost(url);
			UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(
					params, "UTF-8");
			post.setEntity(entityRequest);
			HttpResponse response = client.execute(post);
			HttpEntity entityResponse = response.getEntity();
			is = entityResponse.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
			is.close();
			result = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			client.getConnectionManager().shutdown();
		}
		return parser.parseData(result);
	}
}
