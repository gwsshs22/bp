package club.sgen.custom;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import club.sgen.entity.Betting;
import club.sgen.entity.Pop;
import club.sgen.entity.Product;
import club.sgen.entity.User;
import club.sgen.network.ImageDownloader;
import club.sgen.network.R;
import club.sgen.network.cache.ImageCacheFactory;

public class ProductItemAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Product> productSrc;
	private int layout;
	private ImageDownloader imageDownloader;

	public ProductItemAdapter(Context context, int layout,
			ArrayList<Product> productSrc) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.productSrc = productSrc;
		this.layout = layout;
		this.imageDownloader = new ImageDownloader(context,
				ImageCacheFactory.DEFAULT_CACHE_NAME);
	}

	@Override
	public int getCount() {
		return productSrc.size();
	}

	@Override
	public Product getItem(int position) {
		return productSrc.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View gridView;

		if (convertView == null) {
			gridView = inflater.from(context).inflate(layout, null);

		} else {
			gridView = convertView;
		}
		BitmapDrawable noImageDrawable = new BitmapDrawable(
				context.getResources(), BitmapFactory.decodeResource(
						context.getResources(), R.drawable.product_1));
		ImageView productImage = (ImageView) gridView
				.findViewById(R.id.product_image);
		Product product = productSrc.get(position);
		imageDownloader.download(product.getImage(), productImage,
				noImageDrawable);

		TextView productName = (TextView) gridView
				.findViewById(R.id.product_name);
		productName.setText(product.getName());

		TextView productIntro = (TextView) gridView
				.findViewById(R.id.product_intro);
		productIntro.setText(product.getDescription());

		TextView price = (TextView) gridView.findViewById(R.id.product_price);
		price.setText(String.valueOf(product.getPrice()));
		return gridView;
	}
}
