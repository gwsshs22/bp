package club.sgen.bettingpop;

import club.sgen.network.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class PopupbettingstartActivity extends Activity {

	private ImageView ok;
	private ImageView cancel;
	private ImageView close;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.popup_bettingstart_start);

		ok = (ImageView) findViewById(R.id.popup_bettingstart_start_ok);
		cancel = (ImageView) findViewById(R.id.popup_bettingstart_start_cancel);
		close = (ImageView) findViewById(R.id.popup_bettingstart_start_close);

		// TODO Auto-generated method stub
	}

}
