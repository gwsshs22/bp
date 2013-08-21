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
import club.sgen.entity.User;
import club.sgen.network.AsyncCallback;
import club.sgen.network.R;
import club.sgen.network.DataRequester;

public class MainGridItemAdapter extends BaseAdapter {
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
		View gridView;

		if (convertView == null) {
			gridView = inflater.from(context).inflate(layout, null);

		} else {
			gridView = convertView;
		}

		ImageView imageView = (ImageView) gridView
				.findViewById(R.id.betting_image);
		//imageView.setImageResource(Integer.parseInt(bettingSrc.get(position).getResultImage()));

		imageView = (ImageView) gridView.findViewById(R.id.better_image);
		// imageView set image

		TextView textView = (TextView) gridView.findViewById(R.id.better_name);
		textView.setText(bettingSrc.get(position).getUserId());

		textView = (TextView) gridView.findViewById(R.id.betting_title);
		textView.setText(bettingSrc.get(position).getName());

		return gridView;
	}


}
