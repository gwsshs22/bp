package club.sgen.network;

import android.app.Application;
import club.sgen.entity.User;
import club.sgen.network.cache.FileCacheFactory;
import club.sgen.network.cache.ImageCacheFactory;

public class BettingpopApplication extends Application {
	private static User user;

	public static void login(User u) {
		user = u;
	}

	public static User getUser() {
		return user;
	}

	public static void logout() {
		user = null;
	}

	public boolean isLogined() {
		return user != null;
	}

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
