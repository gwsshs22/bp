package club.sgen.bettingpop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import club.sgen.network.R;

public class IntroActivity extends Activity {

	private ImageView loginbtn;
	private ImageView registbtn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.intro);

		loginbtn = (ImageView) findViewById(R.id.intro_loginbutton);
		registbtn = (ImageView) findViewById(R.id.intro_registbutton);
		
		loginbtn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
				startActivity(intent);
				
			}
		});
		
		registbtn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IntroActivity.this, RegistActivity.class);
				startActivity(intent);
				
			}
		});

		// TODO Auto-generated method stub
	}
}
