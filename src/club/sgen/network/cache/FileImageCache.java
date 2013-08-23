package club.sgen.network.cache;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import club.sgen.network.BitmapHandler;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FileImageCache implements ImageCache {
	private static final String TAG = "FileImageCache";

	private FileCache fileCache;

	public FileImageCache(String cacheName) {
		fileCache = FileCacheFactory.getInstance().get(cacheName);
	}

	@Override
	public void addBitmap(String key, final Bitmap bitmap) {
		try {
			fileCache.put(key, new ByteProvider() {
				@Override
				public void writeTo(OutputStream os) {
					bitmap.compress(CompressFormat.PNG, 100, os);
				}
			});
		} catch (IOException e) {
			Log.e(TAG, "fail to bitmap to fileCache", e);
		}
	}

	@Override
	public void addBitmap(String key, File bitmapFile) {
		try {
			fileCache.put(key, bitmapFile, true);
		} catch (IOException e) {
			Log.e(TAG, String.format("fail to bitmap file[%s] to fileCache",
					bitmapFile.getAbsolutePath()), e);
		}
	}

	@Override
	public void addBitmap(String key, File bitmapFile, BitmapHandler handler) {
		addBitmap(key, bitmapFile);
	}

	@Override
	public Bitmap getBitmap(String key, BitmapHandler handler) {
		FileEntry cachedFile = fileCache.get(key);
		if (cachedFile == null) {
			return null;
		}
		Bitmap org = BitmapFactory.decodeFile(cachedFile.getFile()
				.getAbsolutePath());
		Bitmap ret = handler.handleBitmap(org);
		org.recycle();
		return ret;
	}

	@Override
	public Bitmap getBitmap(String key) {
		FileEntry cachedFile = fileCache.get(key);
		if (cachedFile == null) {
			return null;
		}
		return BitmapFactory.decodeFile(cachedFile.getFile().getAbsolutePath());
	}

	@Override
	public void clear() {
		fileCache.clear();
	}

}