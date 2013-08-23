package club.sgen.bettingpop;

import club.sgen.network.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BettingresultActivity extends Activity {

	private ImageView closebutton;
	private ImageView userimg;
	private TextView username;
	private TextView usertitle;
	private TextView userterm;
	private ImageView productimage;
	private TextView productname;
	private TextView productprice;
	private ImageView bettingbutton;
	private TextView bettingfailnumber;
	private ImageView resultbutton;
	private ImageView sharebutton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.betting_result);

		closebutton = (ImageView) findViewById(R.id.bettingresult_closebutton);
		userimg = (ImageView) findViewById(R.id.bettingresult_userimage);
		username = (TextView) findViewById(R.id.bettingresult_name);
		usertitle = (TextView) findViewById(R.id.bettingresult_title);
		userterm = (TextView) findViewById(R.id.bettingresult_term);
		productimage = (ImageView) findViewById(R.id.bettingresult_bettingproductimage);
		productname = (TextView) findViewById(R.id.bettingresult_bettingproductname);
		productprice = (TextView) findViewById(R.id.bettingresult_bettingproductprice);
		bettingbutton = (ImageView) findViewById(R.id.bettingresult_bettingbutton);
		bettingfailnumber = (TextView) findViewById(R.id.bettingresult_bettingfailnumber);
		resultbutton = (ImageView) findViewById(R.id.bettingresult_result);
		sharebutton = (ImageView) findViewById(R.id.bettingresult_share);

		closebutton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});

		bettingbutton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});
		
		resultbutton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});

		// TODO Auto-generated method stub
	}

}
