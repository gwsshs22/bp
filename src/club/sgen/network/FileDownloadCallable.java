package club.sgen.network;

import java.io.File;
import java.util.concurrent.Callable;

public class FileDownloadCallable implements Callable<File>{

	private String url;
	private File file;

	public FileDownloadCallable(String url, File file) {
		this.url = url;
		this.file = file;
	}

	@Override
	public File call() throws Exception {
		return HttpRequestHelper.getInstance().download(url, file);
	}
}
