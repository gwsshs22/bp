package club.sgen.network;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import android.util.Log;

public class FileUploadCallable implements Callable<String> {
	private String urlString = DataRequester.serverURL + "/servlets/upload";
	private String fileName;
	private String query;
	private String userID;

	public FileUploadCallable(String fileName, String query, String userID) {
		this.fileName = fileName;
		this.query = query;
		this.userID = userID;
	}

	@Override
	public String call() throws Exception {
		return httpFileUpload();
	}

	public String httpFileUpload() throws Exception {
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String result = null;
		FileInputStream mFileInputStream = null;
		URL connectUrl = null;
		try {

			mFileInputStream = new FileInputStream(fileName);
			connectUrl = new URL(urlString);
			Log.d("Test", "mFileInputStream  is " + mFileInputStream);

			// open connection
			HttpURLConnection conn = (HttpURLConnection) connectUrl
					.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			// write data
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"" + "userID"
					+ "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(userID);
			dos.writeBytes(lineEnd);

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"" + "query"
					+ "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(query);
			dos.writeBytes(lineEnd);

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ fileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			int bytesAvailable = mFileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);

			byte[] buffer = new byte[bufferSize];
			int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

			Log.d("Test", "image byte is " + bytesRead);

			// read image
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = mFileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
			}

			dos.writeBytes(lineEnd);

			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			// close streams
			Log.e("Test", "File is written");
			mFileInputStream.close();
			dos.flush(); // finish upload...

			// get response
			int ch;
			InputStream is = conn.getInputStream();
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			result = b.toString();
			Log.e("Test", "result = " + result);
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
}
