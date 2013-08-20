package club.sgen.test;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import club.sgen.network.ImageDownloader;
import club.sgen.network.R;
import club.sgen.network.cache.ImageCacheFactory;

public class ImageDownloadTestActivity extends Activity {
	private Button downBtn;
	private Button delBtn;
	private ImageView view;
	private BitmapDrawable noImage;
	private ImageDownloader imageDownloader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_layout);
		downBtn = (Button) findViewById(R.id.image_down_test_btn);
		delBtn = (Button) findViewById(R.id.image_test_delete_btn);
		noImage = new BitmapDrawable(getResources(),
				BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_launcher));
		imageDownloader = new ImageDownloader(this,
				ImageCacheFactory.DEFAULT_CACHE_NAME);
		view = (ImageView) findViewById(R.id.user_image_test);
		delBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				view.setImageDrawable(noImage);
			}

		});

		downBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageDownloader.download("dhhfgji_1376754793397.jpg", view,
						noImage);
			}

		});
	}
}
