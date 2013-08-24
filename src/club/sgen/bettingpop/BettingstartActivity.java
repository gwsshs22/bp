package club.sgen.bettingpop;

import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
	private static final int REQUEST_POPUP = 18;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.betting_start);

		Pop pop = Pop.getPopByIntent(getIntent());
		user = pop.getUser();
		product = pop.getProduct();
		betting = pop.getBetting();

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
				startActivityForResult(new Intent(BettingstartActivity.this,
						PopupbettingstartActivity.class), REQUEST_POPUP);
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_POPUP) {
			if (resultCode == Activity.RESULT_OK) {
				DataRequester.joinBetting(
						String.valueOf(betting.getBetting_key()),
						BettingpopApplication.getUser().getId(), "F",
						BettingstartActivity.this);
			}
			return;
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
