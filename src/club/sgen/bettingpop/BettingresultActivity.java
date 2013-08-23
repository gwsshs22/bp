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

public class BettingresultActivity extends Activity implements
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
	private ImageView resultbutton;
	private ImageView sharebutton;

	private User user;
	private Betting betting;
	private Product product;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.betting_result);

		Pop pop = Pop.getPopByIntent(getIntent());
		user = pop.getUser();
		product = pop.getProduct();
		betting = pop.getBetting();

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

		username.setText(user.getId());
		usertitle.setText(betting.getDescription());
		productname.setText(product.getName());
		productprice.setText(String.valueOf(product.getPrice()));
		bettingfailnumber.setText(String.valueOf(pop.getDisagree()));

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

			}
		});

	}

	public class PopupDialog extends Dialog implements OnClickListener {
		private Button success;
		private Button fail;
		private Button close;

		public PopupDialog(Context context) {
			super(context);
			setContentView(R.layout.popup_bettingresult_accept);
			success = (Button) findViewById(R.id.popup_bettingresult_success);
			fail = (Button) findViewById(R.id.popup_bettingresult_failure);
			close = (Button) findViewById(R.id.popup_bettingresult_closebutton);
			close.setOnClickListener(this);
			fail.setOnClickListener(this);
			success.setOnClickListener(this);
		}

		@Override
		public void onClick(View arg0) {
			if (success == arg0) {
				DataRequester.distributeProduct(user.getId(),
						String.valueOf(product.getProduct_key()),
						BettingresultActivity.this);
			} else if (fail == arg0) {
				DataRequester.distributeProduct(BettingpopApplication.getUser()
						.getId(), String.valueOf(product.getProduct_key()),
						BettingresultActivity.this);
			}
			this.dismiss();
		}
	}

	@Override
	public void onResult(HashMap<String, Object> result) {
		String type = (String) result.get("type");
		Boolean errorOccured = (Boolean) result.get("error_occured");
		if (type.equals("distributeProduct")) {
			if (!errorOccured) {
				finish();
			}
		}
	}

	@Override
	public void exceptionOccured(Exception e) {

	}

	@Override
	public void cancelled() {
		// TODO Auto-generated method stub

	}
}