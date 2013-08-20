package club.sgen.custom;

import java.util.ArrayList;

import club.sgen.entity.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FriendListAdapter extends BaseAdapter{
	Context context;
	LayoutInflater inflater;
	ArrayList<User> friendSrc;
	int layout;
	
	public FriendListAdapter(Context context, int layout, ArrayList<User> friendSrc){
		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.friendSrc = friendSrc;
		this.layout = layout;
	}

	@Override
	public int getCount() {
		return friendSrc.size();
	}

	@Override
	public User getItem(int position) {
		return friendSrc.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listView;
		
		if(convertView == null){
			listView = inflater.inflate(layout, parent, false);
		}else{
			listView = convertView;
		}
		
		return listView;
	}
}
