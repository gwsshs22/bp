package club.sgen.bettingpop;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import club.sgen.network.AsyncCallback;
import club.sgen.network.R;

public class BettingmakeActivity extends Activity implements
		AsyncCallback<HashMap<String, Object>> {
	
	private ImageView closebutton;
	private RadioButton bettingradio;
	private RadioButton donationradio;
	private EditText title;
	private EditText term;
	private RadioButton buyproduct;
	private RadioButton writeproduct;
	private EditText writeproductname;
	private RadioButton number2;
	private RadioButton number4;
	private RadioButton number6;
	private RadioButton number8;
	private ImageView upload;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.betting_make);
		
		closebutton = (ImageView) findViewById(R.id.bettingmake_closebutton);
		bettingradio = (RadioButton) findViewById(R.id.bettingmake_bettingicon);
		donationradio = (RadioButton) findViewById(R.id.bettingmake_donationicon);
		title = (EditText) findViewById(R.id.bettingmake_title);
		term = (EditText) findViewById(R.id.bettingmake_term);
		buyproduct = (RadioButton) findViewById(R.id.bettingmake_findgift);
		writeproduct = (RadioButton) findViewById(R.id.bettingmake_inputgift);
		writeproductname = (EditText) findViewById(R.id.bettingmake_writeproduct);
		number2 = (RadioButton) findViewById(R.id.bettingmake_people2);
		number4 = (RadioButton) findViewById(R.id.bettingmake_people4);
		number6 = (RadioButton) findViewById(R.id.bettingmake_people6);
		number8 = (RadioButton) findViewById(R.id.bettingmake_people8);
		upload = (ImageView) findViewById(R.id.bettingmake_ok);
		
		closebutton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});
		
		upload.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});
		

	}

	@Override
	public void onResult(HashMap<String, Object> result) {
		String type = (String) result.get("type");
		Boolean errorOccured = (Boolean) result.get("error_occured");
		if (type.equals("registerBetting")) {
			if (!errorOccured) {
				boolean success = (Boolean) result.get("success");
				
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
