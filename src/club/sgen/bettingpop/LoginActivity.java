package club.sgen.bettingpop;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import club.sgen.entity.User;
import club.sgen.network.AsyncCallback;
import club.sgen.network.BettingpopApplication;
import club.sgen.network.DataRequester;
import club.sgen.network.R;

public class LoginActivity extends Activity implements
		AsyncCallback<HashMap<String, Object>> {

	private ImageView login;
	private EditText id;
	private EditText password;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		login = (ImageView) findViewById(R.id.login_okbutton);
		id = (EditText) findViewById(R.id.login_userid);
		password = (EditText) findViewById(R.id.login_userpassword);

		login.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String idStr = id.getText().toString();
				String passStr = password.getText().toString();
				if (idStr.equals("") || passStr.equals("")) {
					Toast.makeText(LoginActivity.this,
							"id와 password를 제대로 입력해 주십시오.", Toast.LENGTH_LONG)
							.show();
					return;
				}
				DataRequester.login(idStr, passStr, LoginActivity.this);
			}
		});
		// TODO Auto-generated method stub
	}

	@Override
	public void onResult(HashMap<String, Object> result) {
		String type = (String) result.get("type");
		Boolean errorOccured = (Boolean) result.get("error_occured");
		if (type.equals("login")) {
			if (!errorOccured) {
				boolean success = (Boolean) result.get("success");
				if (success) {
					User user = (User) result.get("user");
					BettingpopApplication.login(user);
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(this, "로그인 인증에 실패했습니다.", Toast.LENGTH_LONG)
							.show();
				}

			} else {

			}
		}
	}

	@Override
	public void exceptionOccured(Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelled() {
		// TODO Auto-generated method stub

	}

}
