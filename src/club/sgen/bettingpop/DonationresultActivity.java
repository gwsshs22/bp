package club.sgen.bettingpop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import club.sgen.network.R;

public class DonationresultActivity extends Activity {
	
	private ImageView closebutton;
	private ImageView userimg;
	private TextView username;
	private TextView usertitle;
	private TextView userterm;
	private ImageView productimage;
	private TextView productname;
	private TextView productprice;
	private TextView donationsuccessnumber;
	private TextView donationfailnumber;
	private ImageView resultbutton;
	private ImageView sharebutton;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.donation_result);
	    
	    closebutton = (ImageView) findViewById(R.id.donationresult_closebutton);
		userimg = (ImageView) findViewById(R.id.donationresult_userimage);
		username = (TextView) findViewById(R.id.donationresult_name);
		usertitle = (TextView) findViewById(R.id.donationresult_title);
		userterm = (TextView) findViewById(R.id.donationresult_term);
		productimage = (ImageView) findViewById(R.id.donationresult_bettingproductimage);
		productname = (TextView) findViewById(R.id.donationresult_bettingproductname);
		productprice = (TextView) findViewById(R.id.donationresult_bettingproductprice);
		donationsuccessnumber = (TextView) findViewById(R.id.donationresult_bettingsuccessnumber);
		donationfailnumber = (TextView) findViewById(R.id.donationresult_bettingfailnumber);
		resultbutton = (ImageView) findViewById(R.id.donationresult_result);
		sharebutton = (ImageView) findViewById(R.id.donationresult_share);
		
		closebutton.setOnClickListener(new Button.OnClickListener() {

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
