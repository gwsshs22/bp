package club.sgen.network.cache;

import java.io.File;

import android.graphics.Bitmap;

public interface ImageCache {
	public void addBitmap(String key, Bitmap bitmap);

	public void addBitmap(String key, File bitmapFile);

	public Bitmap getBitmap(String key);

	public void clear();
}
