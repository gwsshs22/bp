package club.sgen.bettingpop;

//
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.Toast;
import android.widget.ToggleButton;
import club.sgen.custom.DrawerListAdapter;
import club.sgen.custom.FriendListAdapter;
import club.sgen.custom.MainGridItemAdapter;
import club.sgen.custom.MyPagerAdapter;
import club.sgen.entity.Betting;
import club.sgen.entity.User;
import club.sgen.network.AsyncCallback;
import club.sgen.network.DataRequester;
import club.sgen.network.R;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ToggleButton toggleButton;
	private ActionBar actionBar;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPageTitles;
	private int[] actionBackgrounds = { R.layout.actionbar_background,
			R.layout.actionbar_gift_box, R.layout.actionbar_background };

	ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mPageTitles = getResources().getStringArray(R.array.pages_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// drawer 열릴때 main content 위에 올라오는 그림자
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// drawer list view with items and click listener
		DrawerListAdapter drawerListAdapter = new DrawerListAdapter(this,
				R.layout.view_drawer_list, mPageTitles);
		mDrawerList.setAdapter(drawerListAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		setActionBar(0);
		
		if (savedInstanceState == null) {
			selectItem(0);
		}
	}
	
	public void setActionBar(int position){
		actionBar = getActionBar();
		int mPostion = position%actionBackgrounds.length;
		View view = getLayoutInflater().inflate(actionBackgrounds[mPostion],
				null);
		actionBar.setCustomView(view, new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT));
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		toggleButton = (ToggleButton) actionBar.getCustomView().findViewById(
				R.id.actionbar_toggle);
		toggleButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
					mDrawerLayout.openDrawer(Gravity.LEFT);
				} else {
					mDrawerLayout.closeDrawer(Gravity.LEFT);
				}
			}

		});


		// sliding drawer 랑 action bar app icon 이랑 잘 interact 하게 해주는 애
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.actionbar_btn_menu, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View view) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if(mPostion == 0){
			ImageButton button = (ImageButton) actionBar.getCustomView().findViewById(R.id.btn_add_betting);
			button.setOnClickListener(new OnClickListener(){
				public void onClick(View arg0) {
					Intent intent = new Intent(MainActivity.this,
							 AddBettingActivity.class);
							 startActivity(intent);
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// fragment를 새로 replace 하면서 main content를 업데이트
		Fragment fragment = new PageFragment();
		Bundle args = new Bundle();
		args.putInt(PageFragment.ARG_PAGE_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setActionBar(position);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * "content_frame"이라구 하는 Fragment. shows menu content."
	 */
	public static class PageFragment extends Fragment implements
	AsyncCallback<HashMap<String, Object>>{
		public static final String ARG_PAGE_NUMBER = "page_number";
		int fragmentPosition = -1;
		GridView gridView;
		ListView listView;
		View rootView;
		TabHost tabHost;
		ArrayList<Betting> bettingItems;
		ArrayList<User> bettingUsers;
		ArrayList<User> friendItems;
		User user;

		public PageFragment() {
			// Empty constructor
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			int i = getArguments().getInt(ARG_PAGE_NUMBER);
			fragmentPosition = i;

			// temporary bettings
			DataRequester.showAllbettinglist(this);
			
			bettingItems = new ArrayList<Betting>();

			// temporary users(friends)
			friendItems = new ArrayList<User>();
			friendItems.add(new User());
			friendItems.add(new User());
			friendItems.add(new User());

			switch (i) {
			case 0: // my page
				Log.d("A1", "my page입니당");
				rootView = inflater.inflate(R.layout.fragment_my_page,
						container, false);
				break;
			case 1:
				rootView = inflater.inflate(R.layout.fragment_betting_item,
						container, false);
				break;
			case 2:
				rootView = inflater.inflate(R.layout.fragment_friend,
						container, false);
				break;
			default:
				rootView = inflater.inflate(R.layout.fragment_my_page,
						container, false);
			}

			String page = getResources().getStringArray(R.array.pages_array)[i];

			getActivity().setTitle(page);

			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			Log.d("A1", "onActivityCreated started");
			super.onActivityCreated(savedInstanceState);
			switch (fragmentPosition) {
			case 0: // my page
				// View Pager
				ViewPager viewPager = (ViewPager) rootView
						.findViewById(R.id.viewPager);
				MyPagerAdapter myPagerAdapter = new MyPagerAdapter(
						getActivity());
				viewPager.setAdapter(myPagerAdapter);

				// Tabhost
				tabHost = (TabHost) rootView.findViewById(R.id.tabhost);
				tabHost.setup();
				TabSpec tabSpec = tabHost.newTabSpec("Tab1").setIndicator(
						"Tab1");
				tabSpec.setContent(R.id.grid_all_popple);
				tabSpec.setIndicator("",
						getResources().getDrawable(R.drawable.tab1));
				tabHost.addTab(tabSpec);

				tabSpec = tabHost.newTabSpec("Tab2").setIndicator("Tab2");
				tabSpec.setContent(R.id.grid_friend);
				tabSpec.setIndicator("",
						getResources().getDrawable(R.drawable.tab2));
				tabHost.addTab(tabSpec);
				tabHost.setCurrentTab(0);

				// GridView
				gridView = (GridView) rootView
						.findViewById(R.id.grid_all_popple);
				MainGridItemAdapter mainGridItemAdapater = new MainGridItemAdapter(
						getActivity(), R.layout.view_grid, bettingItems);
				gridView.setAdapter(mainGridItemAdapater);
				gridView = (GridView) rootView.findViewById(R.id.grid_friend);
				gridView.setAdapter(mainGridItemAdapater);
				break;
			case 1: // betting item
				gridView = (GridView) rootView
						.findViewById(R.id.grid_betting_item);
				MainGridItemAdapter mainGridItemAdapter = new MainGridItemAdapter(
						getActivity(), R.layout.view_grid2, bettingItems);
				gridView.setAdapter(mainGridItemAdapter);
				gridView.setOnItemClickListener(bettingItemListener);
				break;
			case 2: // friend list
				// Tabhost
				tabHost = (TabHost) rootView.findViewById(R.id.tabhost_friend);
				tabHost.setup();
				tabSpec = tabHost.newTabSpec("Tab1").setIndicator("Tab1");
				tabSpec.setContent(R.id.list_my_friend);
				tabSpec.setIndicator("",
						getResources().getDrawable(R.drawable.tab_my_pople));
				tabHost.addTab(tabSpec);

				tabSpec = tabHost.newTabSpec("Tab2").setIndicator("Tab2");
				tabSpec.setContent(R.id.list_find_friend);
				tabSpec.setIndicator("",
						getResources().getDrawable(R.drawable.tab_find_pople));
				tabHost.addTab(tabSpec);
				tabHost.setCurrentTab(0);

				// listView
				listView = (ListView) rootView
						.findViewById(R.id.list_my_friend);
				FriendListAdapter friendListAdapter = new FriendListAdapter(
						getActivity(), R.layout.view_list, friendItems);
				listView.setAdapter(friendListAdapter);
				listView.setOnItemClickListener(friendItemListener);

				listView = (ListView) rootView
						.findViewById(R.id.list_find_friend);
				listView.setAdapter(friendListAdapter);
				listView.setOnItemClickListener(friendItemListener);
			default:
			}
		}

		public void onTabChanged(String tabId) {
			Toast.makeText(getActivity(), "tab1", Toast.LENGTH_SHORT).show();

			TabWidget tabWidget = tabHost.getTabWidget();

			if (tabId == "Tab1") {
				Toast.makeText(getActivity(), "tab1", Toast.LENGTH_SHORT)
						.show();
			}
		}

		AdapterView.OnItemClickListener bettingItemListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				View popupView = View.inflate(getActivity(),
						R.layout.popup_betting_item, null);
				final PopupWindow popup = new PopupWindow(popupView, 500, 350,
						true);
				LinearLayout linear = (LinearLayout) rootView
						.findViewById(R.id.fragment_betting_item_linear);
				popup.showAtLocation(linear, Gravity.CENTER, 0, 0);
				Button button_close = (Button) popupView
						.findViewById(R.id.button_close_popup);
				button_close.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						popup.dismiss();
					}
				});
			}
		};

		AdapterView.OnItemClickListener friendItemListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(), "list click " + position,
						Toast.LENGTH_SHORT).show();
			}
		};

		@Override
		public void onResult(HashMap<String, Object> result) {
			String type = (String) result.get("type");
			Boolean errorOccured = (Boolean) result.get("error_occured");
			if (type.equals("showAllbettinglist")) {
				if (!errorOccured) {
					bettingItems = (ArrayList<Betting>) result.get("bettings");
					Toast.makeText(getActivity(), bettingItems.size()+"개 베팅", Toast.LENGTH_LONG).show();
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

}
