package club.sgen.custom;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import club.sgen.bettingpop.BettingresultActivity;
import club.sgen.bettingpop.BettingstartActivity;
import club.sgen.bettingpop.DonationresultActivity;
import club.sgen.bettingpop.DonationstartActivity;
import club.sgen.entity.Betting;
import club.sgen.entity.Pop;
import club.sgen.entity.Product;
import club.sgen.entity.User;
import club.sgen.network.BitmapDrawRingOnOuter;
import club.sgen.network.ImageDownloader;
import club.sgen.network.R;
import club.sgen.network.cache.ImageCacheFactory;

public class MainGridItemAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Pop> popSrc;
	private int layout;
	private ImageDownloader imageDownloader;

	public MainGridItemAdapter(Context context, int layout,
			ArrayList<Pop> popSrc) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.popSrc = popSrc;
		this.layout = layout;
		this.imageDownloader = new ImageDownloader(context,
				ImageCacheFactory.DEFAULT_CACHE_NAME);
	}

	@Override
	public int getCount() {
		return popSrc.size();
	}

	@Override
	public Pop getItem(int position) {
		return popSrc.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View gridView;

		if (convertView == null) {
			gridView = inflater.from(context).inflate(layout, null);

		} else {
			gridView = convertView;
		}

		final Betting betting = popSrc.get(position).getBetting();
		User user = popSrc.get(position).getUser();
		Product product = popSrc.get(position).getProduct();

		LinearLayout linear = (LinearLayout) gridView
				.findViewById(R.id.view_grid_linear);
		TextView tv = (TextView) gridView.findViewById(R.id.textView1);
		if (betting.getType() == Betting.TYPE.D)
			linear.setBackgroundResource(R.drawable.plate_donation);
		else
			tv.setText(String.valueOf(popSrc.get(position).getDisagree()));

		ImageView imageView = (ImageView) gridView.findViewById(R.id.black);
		if (betting.getIs_end().equals("T")) {
			imageView.setVisibility(View.VISIBLE);
		}

		imageView = (ImageView) gridView.findViewById(R.id.betting_image);
		BitmapDrawable noImage = new BitmapDrawable(context.getResources(),
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.product_3));
		imageDownloader.download(product.getImage(), imageView, noImage);

		imageView = (ImageView) gridView.findViewById(R.id.better_image);
		noImage = new BitmapDrawable(context.getResources(),
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.user1));
		imageDownloader.download(user.getImage(), imageView, noImage,
				new BitmapDrawRingOnOuter(0x00037eba, 3, 3));

		TextView textView = (TextView) gridView.findViewById(R.id.better_name);
		textView.setText(betting.getUserId());

		textView = (TextView) gridView.findViewById(R.id.betting_title);
		textView.setText(betting.getName());

		gridView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = null;
				if (betting.getType() == Betting.TYPE.B) {
					if (!betting.getIs_end().equals("T")) {
						intent = new Intent(context, BettingstartActivity.class);
					} else {
						intent = new Intent(context,
								BettingresultActivity.class);
					}
				} else {
					if (!betting.getIs_end().equals("T")) {
						intent = new Intent(context,
								DonationstartActivity.class);
					} else {
						intent = new Intent(context,
								DonationresultActivity.class);
					}
				}
				Pop.putPopToIntent(intent, popSrc.get(position));
				context.startActivity(intent);
			}

		});

		return gridView;
	}

}
