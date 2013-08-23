package club.sgen.custom;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import club.sgen.entity.FriendUser;
import club.sgen.entity.User;
import club.sgen.network.AsyncCallback;
import club.sgen.network.DataRequester;
import club.sgen.network.R;

public class FriendListAdapter extends BaseAdapter implements 
AsyncCallback<HashMap<String, Object>>, OnClickListener{
	Context context;
	LayoutInflater inflater;
	ArrayList<FriendUser> friendSrc;
	User user;
	User friend;
	int layout;

	public FriendListAdapter(Context context, int layout,
			ArrayList<FriendUser> friendSrc, User user) {
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.friendSrc = friendSrc;
		this.layout = layout;
		this.user = user;
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
		friend = friendUser.getUser();
		
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
		textView.setText(friend.getName());
		
		ImageButton imageButton_add = (ImageButton) listView.findViewById(R.id.btn_add_friend);
		ImageButton imageButton_reject = (ImageButton) listView.findViewById(R.id.btn_reject_friend);
		
		if(!friendUser.getIsRequest()){			
			imageButton_add.setVisibility(View.INVISIBLE);
			imageButton_reject.setVisibility(View.INVISIBLE);
		}else{
			imageButton_add.setVisibility(View.VISIBLE);
			imageButton_reject.setVisibility(View.VISIBLE);
			imageButton_add.setOnClickListener(this);
			imageButton_reject.setOnClickListener(this);
		}
		return listView;
	}
	
	public void onClick(View view){
		switch(view.getId()){
		case R.id.btn_add_friend: 
			Toast.makeText(context, "친구를 추가했습니다", Toast.LENGTH_LONG).show();
			DataRequester.acceptFriend(user.getId(), friend.getId(),"T" ,this);
			break;
		case R.id.btn_reject_friend:
			Toast.makeText(context, "요청을 거절했습니다", Toast.LENGTH_LONG).show();
			break;
		default:
		}
	}

	@Override
	public void onResult(HashMap<String, Object> result) {
		String type = (String) result.get("type");
		Boolean errorOccured = (Boolean) result.get("error_occured");
		if (type.equals("acceptFriend")){
			Toast.makeText(context, "add?"+(String)result.get("success"), Toast.LENGTH_SHORT).show();
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
