package club.sgen.network;

import java.io.File;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.widget.ImageView;
import club.sgen.network.cache.ImageCache;

public class ImageDownloadAsyncCallback implements AsyncCallback<File>, AsyncExecutorAware<File>{

	private String url;
	private WeakReference<ImageView> imageViewReference;
	private AsyncExecutor<File> asyncExecutor;
	private ImageCache imageCache;

	public ImageDownloadAsyncCallback(String url, ImageView imageView,
			ImageCache imageCache) {
		this.url = url;
		this.imageViewReference = new WeakReference<ImageView>(imageView);
		this.imageCache = imageCache;
	}

	@Override
	public void setAsyncExecutor(AsyncExecutor<File> asyncExecutor) {
		this.asyncExecutor = asyncExecutor;
	}

	public boolean isSameUrl(String url2) {
		return url.equals(url2);
	}

	@Override
	public void onResult(File bitmapFile) {
		Bitmap bitmap = addBitmapToCache(bitmapFile);
		applyBitmapToImageView(bitmap);
	}

	private Bitmap addBitmapToCache(File bitmap) {
		imageCache.addBitmap(url, bitmap);
		return imageCache.getBitmap(url);
	}


	private void applyBitmapToImageView(Bitmap bitmap) {
		ImageView imageView = imageViewReference.get();
		if (imageView != null) {
			if (isSameCallback(imageView)) {
				imageView.setImageBitmap(bitmap);
				imageView.setTag(null);
			}
		}
	}

	private boolean isSameCallback(ImageView imageView) {
		return this == imageView.getTag();
	}

	public void cancel(boolean b) {
		asyncExecutor.cancel(true);
	}

	@Override
	public void exceptionOccured(Exception e) {
	}

	@Override
	public void cancelled() {
	}
}
