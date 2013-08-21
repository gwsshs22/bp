package club.sgen.custom;

import java.util.ArrayList;

import club.sgen.entity.User;
import club.sgen.network.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class DrawerListAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	String[] mPageTitles;
	int layout;

	public DrawerListAdapter(Context context, int layout, String[] mPageTitles) {
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mPageTitles = mPageTitles;
		this.layout = layout;
	}

	@Override
	public int getCount() {
		return mPageTitles.length;
	}

	@Override
	public String getItem(int position) {
		return mPageTitles[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listView;

		if (convertView == null) {
			listView = inflater.inflate(layout, parent, false);
		} else {
			listView = convertView;
		}

		LinearLayout linearLayout = (LinearLayout) listView
				.findViewById(R.id.drawer_list_back);
		if(position == 0){
			linearLayout.setBackgroundResource(R.drawable.slide_2);
		}else if(position == 1){
			linearLayout.setBackgroundResource(R.drawable.slide_3);
		}else if(position == 2){
			linearLayout.setBackgroundResource(R.drawable.slide_4);
		}else if(position == 3){
			linearLayout.setBackgroundResource(R.drawable.slide_5);
		}  
		
		return listView;
	}
}
