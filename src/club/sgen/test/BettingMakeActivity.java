package club.sgen.test;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import club.sgen.crop.PictureSelector;
import club.sgen.network.AsyncCallback;
import club.sgen.network.R;

public class BettingMakeActivity extends Activity implements
		AsyncCallback<HashMap<String, Object>> {
	private ImageView imageView;
	private EditText goal;
	private EditText maxNumber;
	private Bitmap bitmap;
	private PictureSelector pictureSelector;
	private AlertDialog selectDialog;
	private Button uploadBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.betting_make);
		pictureSelector = new PictureSelector(this);
		pictureSelector.setImageSize(300, 300);
		selectDialog = pictureSelector.createDialog();
		imageView = (ImageView) findViewById(R.id.profile_image);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectDialog.show();
			}

		});
		uploadBtn = (Button) findViewById(R.id.upload_button);
		uploadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
		});
		goal = (EditText) findViewById(R.id.goal);
		maxNumber = (EditText) findViewById(R.id.m_number);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		pictureSelector.deleteTempFile();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		pictureSelector.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = pictureSelector.getBitmap();
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResult(HashMap<String, Object> result) {
		String type = (String) result.get("type");
		Boolean errorOccured = (Boolean) result.get("error_occured");
		if (type.equals("!!")) {

		}
	}

	@Override
	public void exceptionOccured(Exception e) {
	}

	@Override
	public void cancelled() {
	}
}
