package club.sgen.bettingpop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import club.sgen.network.R;

public class DonationstartActivity extends Activity {
	
	private ImageView closebutton;
	private ImageView userimg;
	private TextView username;
	private TextView usertitle;
	private TextView userterm;
	private ImageView productimage;
	private TextView productname;
	private TextView productprice;
	private ImageView donationbutton;
	private TextView donationsuccessnumber;
	private TextView donationfailnumber;
	private ImageView sharebutton;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.donation_start);
	    
	    closebutton = (ImageView) findViewById(R.id.donationstart_closebutton);
		userimg = (ImageView) findViewById(R.id.donationstart_userimage);
		username = (TextView) findViewById(R.id.donationstart_name);
		usertitle = (TextView) findViewById(R.id.donationstart_title);
		userterm = (TextView) findViewById(R.id.donationstart_term);
		productimage = (ImageView) findViewById(R.id.donationstart_donationproductimage);
		productname = (TextView) findViewById(R.id.donationstart_donationproductname);
		productprice = (TextView) findViewById(R.id.donationstart_donationproductprice);
		donationbutton = (ImageView) findViewById(R.id.donationstart_donationbutton);
		donationsuccessnumber = (TextView) findViewById(R.id.donationstart_bettingsuccessnumber);
		donationfailnumber = (TextView) findViewById(R.id.donationstart_bettingfailnumber);
		sharebutton = (ImageView) findViewById(R.id.donationresult_share);
	    
		closebutton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});

		donationbutton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});
		
	    // TODO Auto-generated method stub
	}

}
