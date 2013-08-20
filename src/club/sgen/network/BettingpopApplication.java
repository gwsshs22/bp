package club.sgen.network;

import club.sgen.network.cache.FileCacheFactory;
import club.sgen.network.cache.ImageCacheFactory;
import android.app.Application;

public class BettingpopApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		FileCacheFactory.initialize(this);
		FileCacheFactory.getInstance().create(
				ImageCacheFactory.DEFAULT_CACHE_NAME, 50000);
		ImageCacheFactory.getInstance().createTwoLevelCache(
				ImageCacheFactory.DEFAULT_CACHE_NAME, 50);
	}
}
