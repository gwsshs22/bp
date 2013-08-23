package club.sgen.network.cache;

import java.io.File;

import android.graphics.Bitmap;
import club.sgen.network.BitmapHandler;

public interface ImageCache {
	public void addBitmap(String key, Bitmap bitmap);

	public void addBitmap(String key, File bitmapFile);

	public Bitmap getBitmap(String key);

	public void addBitmap(String key, File bitmapFile, BitmapHandler handler);

	public Bitmap getBitmap(String key, BitmapHandler handler);

	public void clear();
}
