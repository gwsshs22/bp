package club.sgen.bettingpop;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import club.sgen.network.AsyncCallback;
import club.sgen.network.R;

public class BettingmakeActivity extends Activity implements
		AsyncCallback<HashMap<String, Object>> {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.betting_make);

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
