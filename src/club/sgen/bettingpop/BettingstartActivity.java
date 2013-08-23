package club.sgen.bettingpop;

import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import club.sgen.entity.Betting;
import club.sgen.entity.Pop;
import club.sgen.entity.Product;
import club.sgen.entity.User;
import club.sgen.network.AsyncCallback;
import club.sgen.network.BettingpopApplication;
import club.sgen.network.DataRequester;
import club.sgen.network.ImageDownloader;
import club.sgen.network.R;
import club.sgen.network.cache.ImageCacheFactory;

public class BettingstartActivity extends Activity implements
		AsyncCallback<HashMap<String, Object>> {

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

	private User user;
	private Betting betting;
	private Product product;

	private PopupDialog popDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.betting_start);

		Pop pop = Pop.getPopByIntent(getIntent());
		user = pop.getUser();
		product = pop.getProduct();
		betting = pop.getBetting();

		popDialog = new PopupDialog(this);

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

		username.setText(user.getId());
		usertitle.setText(betting.getName());
		productname.setText(product.getName());
		productprice.setText(String.valueOf(product.getPrice()));
		bettingfailnumber.setText(String.valueOf(pop.getDisagree()));
		userterm.setText(DataRequester.dateToString(betting.getTerm_end()));
		ImageDownloader downloader = new ImageDownloader(this,
				ImageCacheFactory.DEFAULT_CACHE_NAME);
		BitmapDrawable noImageDrawable = new BitmapDrawable(
				this.getResources(), BitmapFactory.decodeResource(
						this.getResources(), R.drawable.user1));
		downloader.download(user.getImage(), userimg, noImageDrawable);
		noImageDrawable = new BitmapDrawable(this.getResources(),
				BitmapFactory.decodeResource(this.getResources(),
						R.drawable.product_3));
		downloader.download(product.getImage(), productimage, noImageDrawable);
		closebutton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		bettingbutton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				popDialog.show();
			}
		});

	}

	public class PopupDialog extends Dialog implements OnClickListener {
		private ImageView ok;

		public PopupDialog(Context context) {
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.popup_bettingstart_start);
			ok = (ImageView) findViewById(R.id.popup_bettingstart_start_ok);
			ok.setOnClickListener(this);
			ImageView btn = (ImageView) findViewById(R.id.popup_bettingstart_start_cancel);
			btn.setOnClickListener(this);
			btn = (ImageView) findViewById(R.id.popup_bettingstart_start_close);
			btn.setOnClickListener(this);
		}

		@Override
		public void onClick(View arg0) {
			if (arg0 == ok) {
				DataRequester.joinBetting(
						String.valueOf(betting.getBetting_key()),
						BettingpopApplication.getUser().getId(), "F",
						BettingstartActivity.this);
			}
			this.dismiss();
		}
	}

	@Override
	public void onResult(HashMap<String, Object> result) {
		String type = (String) result.get("type");
		Boolean errorOccured = (Boolean) result.get("error_occured");
		if (type.equals("joinBetting")) {
			if (!errorOccured) {
				Toast.makeText(this, "배팅에 참여하셨습니다.", Toast.LENGTH_LONG).show();
				finish();
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
