package club.sgen.bettingpop;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
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
import club.sgen.crop.PictureSelector;
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
	private ImageView resultbutton;
	private ImageView sharebutton;

	private User user;
	private Betting betting;
	private Product product;

	private PictureSelector selector;
	private AlertDialog alertDialog;

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

		selector = new PictureSelector(this, false);
		selector.setAspect(276, 138);
		alertDialog = selector.createDialog();
		boolean isOwner = false;
		if (user.getId().equals(BettingpopApplication.getUser().getId()))
			isOwner = true;

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

		userterm.setText(DataRequester.dateToString(betting.getTerm_end()));
		username.setText(user.getId());
		usertitle.setText(betting.getName());
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
		if (betting.getResultImage() == null)
			downloader.download(product.getImage(), productimage,
					noImageDrawable);
		else
			downloader.download(betting.getResultImage(), productimage,
					noImageDrawable);
		closebutton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		if (!isOwner) {
			bettingbutton.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivityForResult(new Intent(
							BettingresultActivity.this,
							PopupbettingresultActivity.class), REQUEST_POPUP);
				}
			});
		} else {
			productimage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.show();
				}

			});
			resultbutton.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					DataRequester.updateResultImage(betting.getBetting_key(),
							selector.getFileName());
					Toast.makeText(BettingresultActivity.this, "사진을 업로드합니다.",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_POPUP) {
			if (resultCode == Activity.RESULT_OK) {
				DataRequester.distributeProduct(user.getId(),
						String.valueOf(product.getProduct_key()),
						BettingresultActivity.this);
			} else if (resultCode == Activity.RESULT_CANCELED) {
				DataRequester.distributeProduct(BettingpopApplication.getUser()
						.getId(), String.valueOf(product.getProduct_key()),
						BettingresultActivity.this);
			}
			return;
		}
		if (selector == null)
			return;
		selector.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = selector.getBitmap();
		if (bitmap != null)
			productimage.setImageBitmap(bitmap);
	}

	@Override
	public void exceptionOccured(Exception e) {

	}

	@Override
	public void cancelled() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (selector != null)
			selector.deleteTempFile();
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

}