	package club.sgen.test;

	import java.io.UnsupportedEncodingException;
	import java.util.HashMap;

	import android.app.Activity;
	import android.content.Intent;
	import android.graphics.Bitmap;
	import android.os.Bundle;
	import android.view.View;
	import android.view.View.OnClickListener;
	import android.widget.Button;
	import android.widget.EditText;
	import android.widget.ImageView;
	import club.sgen.crop.PictureSelector;
	import club.sgen.network.AsyncCallback;
	import club.sgen.network.DataRequester;
	import club.sgen.network.R;

	public class MainActivity extends Activity implements
			AsyncCallback<HashMap<String, Object>> {
		private Button btn;
		private EditText id;
		private EditText pass;
		private Button make;
		private Button imgDwn;
		private ImageView image;
		private PictureSelector selector;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main2);

			btn = (Button) findViewById(R.id.request_btn);
			id = (EditText) findViewById(R.id.id_text);
			pass = (EditText) findViewById(R.id.password_text);
			image = (ImageView) findViewById(R.id.user_image);
			selector = new PictureSelector(this);
			selector.setImageSize(300, 300);
			imgDwn = (Button) findViewById(R.id.img_down_btn);
			imgDwn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(MainActivity.this,
							ImageDownloadTestActivity.class));
				}

			});
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selector.createDialog().show();
				}

			});
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						DataRequester.showProductInfo(new String(id.getEditableText().toString().getBytes("UTF-8")), MainActivity.this);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});
			make = (Button) findViewById(R.id.betting_make_btn);
			make.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(MainActivity.this,
							BettingMakeActivity.class);
					startActivity(intent);
				}

			});
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			selector.deleteTempFile();
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			selector.onActivityResult(requestCode, resultCode, data);
			Bitmap bitmap = selector.getBitmap();
			if (bitmap != null)
				image.setImageBitmap(bitmap);
		}

		@Override
		public void onResult(HashMap<String, Object> result) {
			String type = (String) result.get("type");
			Boolean errorOccured = (Boolean) result.get("error_occured");
			if (type.equals("showProductInfo")) {
				if (!errorOccured) {
					int x = 0;
					x += 10;
				} else {

				}
			}
		}

		@Override
		public void exceptionOccured(Exception e) {
		}

		@Override
		public void cancelled() {
		}

	}
