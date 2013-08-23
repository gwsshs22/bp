package club.sgen.custom;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import club.sgen.entity.FriendUser;
import club.sgen.entity.User;
import club.sgen.network.R;

public class FriendListAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<FriendUser> friendSrc;
	int layout;

	public FriendListAdapter(Context context, int layout,
			ArrayList<FriendUser> friendSrc) {
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.friendSrc = friendSrc;
		this.layout = layout;
	}

	@Override
	public int getCount() {
		return friendSrc.size();
	}

	@Override
	public FriendUser getItem(int position) {
		return friendSrc.get(position);
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

		FriendUser friendUser = friendSrc.get(position);
		User user = friendUser.getUser();
		
		LinearLayout linearLayout = (LinearLayout)listView.findViewById(R.id.friend_list_background);		
		if(friendSrc.size()==1){
			linearLayout.setBackgroundResource(R.drawable.result_1);
		}else if(position == 0){
			linearLayout.setBackgroundResource(R.drawable.result_top);
		}else if(position == friendSrc.size()){
			linearLayout.setBackgroundResource(R.drawable.result_bottom);
		}else{
			linearLayout.setBackgroundResource(R.drawable.result_mid);
		}
		
		ImageView imageView = (ImageView) listView
				.findViewById(R.id.friend_image);
		BitmapDrawable noImage = new BitmapDrawable(context.getResources(),
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.user1));
		
		TextView textView = (TextView) listView.findViewById(R.id.friend_name);
		textView.setText(user.getName());
		
		ImageButton imageButton_add = (ImageButton) listView.findViewById(R.id.btn_add_friend);
		ImageButton imageButton_reject = (ImageButton) listView.findViewById(R.id.btn_reject_friend);
		
		if(!friendUser.getIsRequest()){
			imageButton_add.setVisibility(View.GONE);
			imageButton_reject.setVisibility(View.GONE);
		}else{
			imageButton_add.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v){
					Toast.makeText(context, "ģ���� �߰��߽��ϴ�", Toast.LENGTH_LONG).show();
				}
			});
			imageButton_reject.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v){
					Toast.makeText(context, "��û�� �����߽��ϴ�", Toast.LENGTH_LONG).show();
				}
			});
		}
		return listView;
	}
}
