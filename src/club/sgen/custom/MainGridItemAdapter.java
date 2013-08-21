package club.sgen.custom;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import club.sgen.entity.Betting;
import club.sgen.network.AsyncCallback;
import club.sgen.network.R;
import club.sgen.network.DataRequester;

public class MainGridItemAdapter extends BaseAdapter implements
		AsyncCallback<HashMap<String, Object>> {
	Context context;
	LayoutInflater inflater;
	ArrayList<Betting> bettingSrc;
	int layout;

	public MainGridItemAdapter(Context context, int layout,
			ArrayList<Betting> bettingSrc) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.bettingSrc = bettingSrc;
		this.layout = layout;
		DataRequester.showAllbettinglist(this);
	}

	@Override
	public int getCount() {
		return bettingSrc.size();
	}

	@Override
	public Betting getItem(int position) {
		return bettingSrc.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ImageView imageView =
		// (ImageView)convertView.findViewById(R.id.betting_image);
		// ImageView imageView =
		// (ImageView)tempView.findViewById(R.id.betting_image);

		Log.d("A1", "1");

		View gridView;

		if (convertView == null) {
			Log.d("A1", "2");
			gridView = inflater.from(context).inflate(layout, null);

		} else {
			gridView = convertView;
		}

		ImageView imageView = (ImageView) gridView
				.findViewById(R.id.betting_image);
		// imageView.setImageResource(bettingSrc.get(position).getBettingImage());

		TextView textView = (TextView) gridView
				.findViewById(R.id.betting_title);
		// textView.setText(bettingSrc.get(position).getTitle());
		// TextView textView = (TextView) gridView.
		// if(convertView == null){
		// //imageView = new ImageView(context);
		// imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
		// imageView.setAdjustViewBounds(false);
		// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// imageView.setPadding(8, 8, 8, 8);
		// }else{
		// imageView = (ImageView)convertView;
		// }

		return gridView;
	}
	
	@Override
	public void onResult(HashMap<String, Object> result) {
		String type = (String) result.get("type");
		Boolean errorOccured = (Boolean) result.get("error_occured");
		if (type.equals("showAllbettinglist")) {
			if (!errorOccured) {
				int x = 0;
				x += 10;
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
