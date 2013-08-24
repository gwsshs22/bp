package club.sgen.bettingpop;

import club.sgen.network.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class PopupbettingresultActivity extends Activity {

	private ImageView ok;
	private ImageView cancel;
	private ImageView close;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.popup_bettingresult_accept);
	    
	    ok = (ImageView) findViewById(R.id.popup_bettingresult_success);
	    cancel = (ImageView) findViewById(R.id.popup_bettingresult_failure);
	    close = (ImageView) findViewById(R.id.popup_bettingresult_closebutton);
	    
	    // TODO Auto-generated method stub
	}

}
