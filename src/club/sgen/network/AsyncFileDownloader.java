package club.sgen.network;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import android.content.Context;

public class AsyncFileDownloader {
	private Context context;

	public AsyncFileDownloader(Context context) {
		this.context = context;
	}

	public void download(String url, AsyncCallback<File> callback) {
		download(url, null, callback);
	}

	public void download(String url, File destination, AsyncCallback<File> callback) {
		try {
			destination = getDestinationIfNotNullOrCreateTemp(destination, callback);
		} catch (IOException e) {
			callback.exceptionOccured(e);
			return;
		}
		runAsyncDownload(url, destination, callback);
	}

	private File getDestinationIfNotNullOrCreateTemp(File destination,
			AsyncCallback<File> callback) throws IOException {
		if (destination != null) {
			return destination;
		}
		return createTemporaryFile();
	}

	private File createTemporaryFile() throws IOException {
		return File.createTempFile("afd", ".tmp", context.getCacheDir());
	}

	private void runAsyncDownload(String url, File destination, AsyncCallback<File> callback) {
		Callable<File> callable = new FileDownloadCallable(url, destination);
		new AsyncExecutor<File>().setCallable(callable).setCallback(callback).execute();
	}
}
