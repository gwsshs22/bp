package club.sgen.bettingpop;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import club.sgen.crop.PictureSelector;
import club.sgen.network.AsyncCallback;
import club.sgen.network.DataRequester;
import club.sgen.network.R;

public class RegistActivity extends Activity implements
		AsyncCallback<HashMap<String, Object>> {
	private ImageView userProfile;
	private ImageView signupbutton;
	private EditText id;
	private EditText password;
	private EditText passwordre;
	private EditText email;
	private PictureSelector pictureSelector;
	private AlertDialog imageSelectDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist);

		signupbutton = (ImageView) findViewById(R.id.regist_okbutton);
		id = (EditText) findViewById(R.id.regist_userid);
		password = (EditText) findViewById(R.id.regist_userpassword);
		passwordre = (EditText) findViewById(R.id.regist_userpasswordre);
		email = (EditText) findViewById(R.id.regist_useremail);
		userProfile = (ImageView) findViewById(R.id.regist_userimage);

		pictureSelector = new PictureSelector(this);
		imageSelectDialog = pictureSelector.createDialog();
		userProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				imageSelectDialog.show();
			}
		});

		signupbutton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				String idStr = id.getText().toString();
				String passStr = password.getText().toString();
				String passReStr = passwordre.getText().toString();
				String emailStr = email.getText().toString();
				if (idStr.equals("") || passStr.equals("")
						|| emailStr.equals("") || passReStr.equals("")) {
					Toast.makeText(RegistActivity.this, "모든 항목을 입력해 주십시오.",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (!passStr.equals(passReStr)) {
					Toast.makeText(RegistActivity.this,
							"비밀번호와 비밀번호 확인에 입력하신 비밀번호가 서로 다릅니다.",
							Toast.LENGTH_LONG).show();
					return;
				}
				DataRequester.registerID(idStr, passStr, emailStr,
						pictureSelector.getFileName(), RegistActivity.this);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		pictureSelector.deleteTempFile();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		pictureSelector.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = pictureSelector.getBitmap();
		if (bitmap != null)
			userProfile.setImageBitmap(bitmap);
	}

	@Override
	public void onResult(HashMap<String, Object> result) {
		String type = (String) result.get("type");
		Boolean errorOccured = (Boolean) result.get("error_occured");
		if (type.equals("registerID")) {
			if (!errorOccured) {
				boolean success = (Boolean) result.get("success");
				if (success) {
					Toast.makeText(this, "성공적으로 가입되었습니다.", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(this, "중복되는 ID 입니다.", Toast.LENGTH_LONG)
							.show();
				}
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
