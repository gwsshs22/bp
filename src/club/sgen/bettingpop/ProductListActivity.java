package club.sgen.bettingpop;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import club.sgen.custom.ProductItemAdapter;
import club.sgen.entity.Product;
import club.sgen.entity.User;
import club.sgen.network.AsyncCallback;
import club.sgen.network.BettingpopApplication;
import club.sgen.network.DataRequester;
import club.sgen.network.R;

public class ProductListActivity extends Activity implements
		AsyncCallback<HashMap<String, Object>> {
	public static final int REQUEST_PRODUCT_KEY = 100;
	public static final String GET_MY_PRODUCTS = "GET_MY_PRODUCTS";
	public static final String GET_ALL_PRODUCTS = "GET_ALL_PRODUCTS";
	public static final String REQUEST_PRODUCT_TYPE = "REQUEST_PRODUCT_TYPE";

	private ProductItemAdapter itemAdapter;
	private ArrayList<Product> productItems;
	private ProgressBar progress;
	private User user;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.product_list_activity);
		Intent intent = this.getIntent();
		String type = intent.getStringExtra(REQUEST_PRODUCT_TYPE);

		productItems = new ArrayList<Product>();
		GridView gridView = (GridView) findViewById(R.id.product_list_grid);
		itemAdapter = new ProductItemAdapter(this, R.layout.view_grid2,
				productItems);
		progress = (ProgressBar) findViewById(R.id.product_list_progress);
		progress.setVisibility(View.VISIBLE);
		gridView.setAdapter(itemAdapter);
		user = BettingpopApplication.getUser();

		if (type == null || type.equals(GET_ALL_PRODUCTS)) {
			DataRequester.showAllproductlist(this);
		} else {
			DataRequester.showMyproductlist(user.getId(), this);
		}

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Product product = productItems.get(arg2);
				setResult(product.getProduct_key());
				finish();
			}

		});
	}

	@Override
	public void onResult(HashMap<String, Object> result) {
		String type = (String) result.get("type");
		Boolean errorOccured = (Boolean) result.get("error_occured");
		if (type.equals("showMyproductlist")) {
			if (!errorOccured) {
				productItems.clear();
				ArrayList<Product> tempItems = (ArrayList<Product>) result
						.get("products");
				if (tempItems != null)
					for (Product b : tempItems)
						productItems.add(b);
				progress.setVisibility(View.GONE);
				itemAdapter.notifyDataSetChanged();
			} else {

			}
		} else if (type.equals("showAllproductlist")) {
			if (!errorOccured) {
				productItems.clear();
				ArrayList<Product> tempItems = (ArrayList<Product>) result
						.get("products");
				if (tempItems != null)
					for (Product b : tempItems)
						productItems.add(b);
				progress.setVisibility(View.GONE);
				itemAdapter.notifyDataSetChanged();
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
