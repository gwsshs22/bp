package club.sgen.bettingpop;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import club.sgen.entity.Betting;
import club.sgen.entity.Pop;
import club.sgen.entity.Product;
import club.sgen.entity.User;
import club.sgen.network.DataRequester;
import club.sgen.network.ImageDownloader;
import club.sgen.network.R;
import club.sgen.network.cache.ImageCacheFactory;

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

	private User user;
	private Product product;
	private Betting betting;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.donation_start);

		Pop pop = Pop.getPopByIntent(getIntent());
		user = pop.getUser();
		product = pop.getProduct();
		betting = pop.getBetting();

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

		username.setText(user.getId());
		usertitle.setText(betting.getName());
		productname.setText(product.getName());
		productprice.setText(String.valueOf(product.getPrice()));

		donationsuccessnumber.setText(String.valueOf(pop.getAgree()));
		donationfailnumber.setText(String.valueOf(pop.getDisagree()));

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

		donationbutton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});

		// TODO Auto-generated method stub
	}

}
