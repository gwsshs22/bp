package club.sgen.bettingpop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import club.sgen.network.R;

public class BettingstartActivity extends Activity {

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
	private ImageView sharebutton;
		
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.betting_start);
	    
	    closebutton = (ImageView) findViewById(R.id.bettingstart_closebutton);
	    userimg = (ImageView) findViewById(R.id.bettingstart_userimage);
	    username = (TextView) findViewById(R.id.bettingstart_name);
	    usertitle = (TextView) findViewById(R.id.bettingstart_title);
	    userterm = (TextView) findViewById(R.id.bettingstart_term);
	    productimage = (ImageView) findViewById(R.id.bettingstart_bettingproductimage);
	    productname = (TextView) findViewById(R.id.bettingstart_bettingproductname);
	    productprice = (TextView) findViewById(R.id.bettingstart_bettingproductprice);
	    bettingbutton = (ImageView) findViewById(R.id.bettingstart_bettingbutton);
	    bettingfailnumber = (TextView) findViewById(R.id.bettingstart_bettingfailnumber);
	    sharebutton = (ImageView) findViewById(R.id.bettingstart_share);
	    
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
	    
	}

}
