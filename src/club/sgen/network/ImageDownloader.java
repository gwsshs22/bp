package club.sgen.network;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import club.sgen.network.cache.ImageCache;
import club.sgen.network.cache.ImageCacheFactory;

public class ImageDownloader {
	static final String TAG = "ImageDownloader";

	private Context context;
	private ImageCache imageCache;

	public ImageDownloader(Context context, String imageCacheName) {
		this.context = context;
		imageCache = ImageCacheFactory.getInstance().get(imageCacheName);
	}

	public void download(String fileName, ImageView imageView,
			Drawable noImageDrawable) {
		imageView.setImageDrawable(noImageDrawable);
		if (fileName == null)
			return;
		String url = DataRequester.serverURL + "/images/" + fileName;
		Bitmap bitmap = imageCache.getBitmap(url);
		if (bitmap == null) {
			forceDownload(url, imageView, noImageDrawable);
		} else {
			cancelPotentialDownload(url, imageView);
			imageView.setImageBitmap(bitmap);
		}
	}

	public void download(String fileName, ImageView imageView,
			Drawable noImageDrawable, BitmapHandler handler) {
		imageView.setImageDrawable(noImageDrawable);
		if (fileName == null)
			return;
		String url = DataRequester.serverURL + "/images/" + fileName;
		Bitmap bitmap = imageCache.getBitmap(url, handler);
		if (bitmap == null) {
			forceDownload(url, imageView, noImageDrawable);
		} else {
			cancelPotentialDownload(url, imageView);
			imageView.setImageBitmap(bitmap);
		}
	}

	private void forceDownload(String url, ImageView imageView,
			Drawable noImageDrawable) {
		if (!cancelPotentialDownload(url, imageView))
			return;
		runAsyncImageDownloading(url, imageView);
	}

	private boolean cancelPotentialDownload(String url, ImageView imageView) {
		ImageDownloadAsyncCallback asyncCallback = (ImageDownloadAsyncCallback) imageView
				.getTag();
		if (asyncCallback == null)
			return true;
		if (asyncCallback.isSameUrl(url))
			return false;
		asyncCallback.cancel(true);
		return true;
	}

	private void runAsyncImageDownloading(String url, ImageView imageView) {
		File tempFile = createTemporaryFile();
		if (tempFile == null)
			return;

		Callable<File> callable = new FileDownloadCallable(url, tempFile);
		ImageDownloadAsyncCallback callback = new ImageDownloadAsyncCallback(
				url, imageView, imageCache);
		imageView.setTag(callback);

		new AsyncExecutor<File>().setCallable(callable).setCallback(callback)
				.execute();
	}

	private File createTemporaryFile() {
		try {
			return File.createTempFile("image", ".tmp", context.getCacheDir());
		} catch (IOException e) {
			Log.e(TAG, "fail to create temp file", e);
			return null;
		}
	}

}