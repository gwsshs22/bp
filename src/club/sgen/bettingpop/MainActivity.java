package club.sgen.bettingpop;

//
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ToggleButton;
import club.sgen.custom.DrawerListAdapter;
import club.sgen.custom.FriendListAdapter;
import club.sgen.custom.MainGridItemAdapter;
import club.sgen.custom.MyPagerAdapter;
import club.sgen.custom.ProductItemAdapter;
import club.sgen.entity.FriendUser;
import club.sgen.entity.Pop;
import club.sgen.entity.Product;
import club.sgen.entity.User;
import club.sgen.network.AsyncCallback;
import club.sgen.network.BettingpopApplication;
import club.sgen.network.DataRequester;
import club.sgen.network.ImageDownloader;
import club.sgen.network.R;
import club.sgen.network.cache.ImageCacheFactory;

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
			R.layout.actionbar_background, R.layout.actionbar_gift_box,
			R.layout.actionbar_pople, R.layout.actionbar_pople };

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

	public void setActionBar(int position) {
		actionBar = getActionBar();
		int mPostion = position % actionBackgrounds.length;
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

		if (mPostion == 0 || mPostion == 1) {
			ImageButton button = (ImageButton) actionBar.getCustomView()
					.findViewById(R.id.btn_add_betting);
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					Intent intent = new Intent(MainActivity.this,
							BettingmakeActivity.class);
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
		PageFragment fragment = new PageFragment();
		fragment.setContext(this);
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
			AsyncCallback<HashMap<String, Object>>, OnClickListener {
		public static final String ARG_PAGE_NUMBER = "page_number";
		private Context context;
		private int fragmentPosition = -1;
		private GridView gridView;
		private ListView listView;
		private View rootView;
		private TabHost tabHost;
		private ArrayList<Pop> myPopItems;
		private ArrayList<Pop> allPopItems;
		private ArrayList<Product> productItems;
		private ArrayList<User> friendRequestItems;
		private ArrayList<FriendUser> friendItems;
		private MyPagerAdapter myPagerAdapter;
		private ProductItemAdapter productItemAdapter;
		private MainGridItemAdapter mainGridItemAdapterAll;
		private MainGridItemAdapter mainGridItemAdapterMyPople;
		private FriendListAdapter friendListAdapter;
		private FriendListAdapter friendRequestListAdapter;
		private String friendId;
		private User user;
		private EditText editText;
		private View tabView1;
		private View tabView2;
		private View tabView3;
		private View tabView4;
		private View tabView5;
		private View tabView6;

		private ArrayList<Pop> parPopItems;
		private ArrayList<Pop> makedPopItems;
		private MainGridItemAdapter mainGridItemAdapterPar;
		private MainGridItemAdapter mainGridItemAdapterMaked;

		private ProgressBar allProgress;
		private ProgressBar myProgress;
		private ProgressBar makedProgress;
		private ProgressBar parProgress;

		private ProgressBar productProgress;

		public PageFragment() {
			myPopItems = new ArrayList<Pop>();
			allPopItems = new ArrayList<Pop>();
			parPopItems = new ArrayList<Pop>();
			makedPopItems = new ArrayList<Pop>();

			productItems = new ArrayList<Product>();
			friendItems = new ArrayList<FriendUser>();
			friendRequestItems = new ArrayList<User>();
		}

		public void setContext(Context context) {
			this.context = context;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			int i = getArguments().getInt(ARG_PAGE_NUMBER);
			fragmentPosition = i;

			switch (i) {
			case 0: // main page
				Log.d("A1", "my page입니당");
				rootView = inflater.inflate(R.layout.fragment_main_page,
						container, false);
				break;
			case 1: // my page
				rootView = inflater.inflate(R.layout.fragment_my_page,
						container, false);
				break;
			case 2:
				rootView = inflater.inflate(R.layout.fragment_betting_item,
						container, false);
				break;
			case 3:
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

			user = BettingpopApplication.getUser();
			switch (fragmentPosition) {
			case 0: // main page
				// View Pager
				ViewPager viewPager = (ViewPager) rootView
						.findViewById(R.id.viewPager);
				myPagerAdapter = new MyPagerAdapter(getActivity());
				viewPager.setAdapter(myPagerAdapter);

				allProgress = (ProgressBar) rootView
						.findViewById(R.id.grid_all_popple_progress);
				allProgress.setVisibility(View.VISIBLE);

				myProgress = (ProgressBar) rootView
						.findViewById(R.id.grid_friend_progress);
				myProgress.setVisibility(View.VISIBLE);

				// GridView
				gridView = (GridView) rootView
						.findViewById(R.id.grid_all_popple);
				mainGridItemAdapterAll = new MainGridItemAdapter(getActivity(),
						R.layout.view_grid, allPopItems);
				gridView.setAdapter(mainGridItemAdapterAll);

				gridView = (GridView) rootView.findViewById(R.id.grid_friend);
				mainGridItemAdapterMyPople = new MainGridItemAdapter(
						getActivity(), R.layout.view_grid, myPopItems);
				gridView.setAdapter(mainGridItemAdapterMyPople);

				tabView1 = (View) rootView
						.findViewById(R.id.grid_all_popple_linear);
				tabView2 = (View) rootView
						.findViewById(R.id.grid_friend_linear);
				tabView1.setVisibility(View.VISIBLE);
				tabView2.setVisibility(View.INVISIBLE);

				RadioButton _imageButton = (RadioButton) rootView
						.findViewById(R.id.btn_tab1);
				_imageButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						tabView1.setVisibility(View.VISIBLE);
						tabView2.setVisibility(View.INVISIBLE);
					}

				});
				_imageButton = (RadioButton) rootView
						.findViewById(R.id.btn_tab2);
				_imageButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						tabView1.setVisibility(View.INVISIBLE);
						tabView2.setVisibility(View.VISIBLE);
					}

				});

				DataRequester.showAllbettinglist(this);
				DataRequester.showFriendbettinglist(user.getId(), this);
				break;
			case 1: // my page
				makedProgress = (ProgressBar) rootView
						.findViewById(R.id.grid_all_popple_progress);
				makedProgress.setVisibility(View.VISIBLE);

				parProgress = (ProgressBar) rootView
						.findViewById(R.id.grid_friend_progress);
				parProgress.setVisibility(View.VISIBLE);

				// GridView
				gridView = (GridView) rootView
						.findViewById(R.id.grid_made_betting);
				mainGridItemAdapterMaked = new MainGridItemAdapter(
						getActivity(), R.layout.view_grid, makedPopItems);
				gridView.setAdapter(mainGridItemAdapterMaked);

				gridView = (GridView) rootView
						.findViewById(R.id.grid_in_betting);
				mainGridItemAdapterPar = new MainGridItemAdapter(getActivity(),
						R.layout.view_grid, parPopItems);
				gridView.setAdapter(mainGridItemAdapterPar);

				ImageView userImage = (ImageView) rootView
						.findViewById(R.id.friend_user_image);
				ImageDownloader down = new ImageDownloader(context,
						ImageCacheFactory.DEFAULT_CACHE_NAME);
				BitmapDrawable bitmapDrawable = new BitmapDrawable(
						getResources(), BitmapFactory.decodeResource(
								getResources(), R.drawable.user1));
				down.download(user.getImage(), userImage, bitmapDrawable);
				TextView userName = (TextView) rootView
						.findViewById(R.id.mypage_name);
				userName.setText((String) user.getId());

				// Tabhost
				tabView3 = (View) rootView
						.findViewById(R.id.grid_made_betting_linear);
				tabView4 = (View) rootView.findViewById(R.id.grid_in_linear);
				tabView3.setVisibility(View.VISIBLE);
				tabView4.setVisibility(View.INVISIBLE);

				RadioButton _imageButton2 = (RadioButton) rootView
						.findViewById(R.id.btn_tab3);
				_imageButton2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						tabView3.setVisibility(View.VISIBLE);
						tabView4.setVisibility(View.INVISIBLE);
					}

				});
				_imageButton2 = (RadioButton) rootView
						.findViewById(R.id.btn_tab4);
				_imageButton2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						tabView3.setVisibility(View.INVISIBLE);
						tabView4.setVisibility(View.VISIBLE);
					}

				});

				DataRequester.showMakebettinglist(user.getId(), this);
				DataRequester.showJoinbettinglist(user.getId(), this);
				break;
			case 2: // betting item list
				productItemAdapter = new ProductItemAdapter(getActivity(),
						R.layout.view_grid2, productItems);
				gridView = (GridView) rootView
						.findViewById(R.id.grid_betting_item);
				gridView.setAdapter(productItemAdapter);
				productProgress = (ProgressBar) rootView
						.findViewById(R.id.grid_betting_item_progres);
				productProgress.setVisibility(View.VISIBLE);

				DataRequester.showMyproductlist(user.getId(), this);
				break;
			case 3:
				// Tabhost
				tabView5 = (View) rootView
						.findViewById(R.id.list_my_friend_linear);
				tabView6 = (View) rootView.findViewById(R.id.find_friend);
				tabView5.setVisibility(View.VISIBLE);
				tabView6.setVisibility(View.INVISIBLE);

				RadioButton _imageButton3 = (RadioButton) rootView
						.findViewById(R.id.btn_tab5);
				_imageButton3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						tabView5.setVisibility(View.VISIBLE);
						tabView6.setVisibility(View.INVISIBLE);
					}

				});
				_imageButton3 = (RadioButton) rootView
						.findViewById(R.id.btn_tab6);
				_imageButton3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						tabView5.setVisibility(View.INVISIBLE);
						tabView6.setVisibility(View.VISIBLE);
					}

				});
				// listView
				listView = (ListView) rootView
						.findViewById(R.id.list_my_friend);
				friendListAdapter = new FriendListAdapter(getActivity(),
						R.layout.view_list, friendItems, user);
				listView.setAdapter(friendListAdapter);
				// listView.setOnItemClickListener(friendItemListener);

				editText = (EditText) rootView.findViewById(R.id.search_name);
				ImageButton imageButton = (ImageButton) rootView
						.findViewById(R.id.btn_find_friend);
				imageButton.setOnClickListener(this);

				friendItems.clear();
				DataRequester.showFriendlist(user.getId(), this);
				DataRequester.showFriendrequestlist(user.getId(), this);
				break;
			case 4:
				getActivity().finish();
			default:
			}
		}

		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_find_friend:
				friendId = editText.getText().toString();
				DataRequester.requestFriend(user.getId(), friendId, this);
				break;
			default:
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
			}
		};

		@Override
		public void onResult(HashMap<String, Object> result) {
			String type = (String) result.get("type");
			Boolean errorOccured = (Boolean) result.get("error_occured");
			if (type.equals("showAllbettinglist")) {
				if (!errorOccured) {
					allPopItems.clear();
					ArrayList<Pop> tempItems = (ArrayList<Pop>) result
							.get("pops");
					if (tempItems != null)
						for (Pop b : tempItems)
							allPopItems.add(b);
					allProgress.setVisibility(View.GONE);
					mainGridItemAdapterAll.notifyDataSetChanged();
				} else {

				}
			} else if (type.equals("showFriendbettinglist")) {
				if (!errorOccured) {
					myPopItems.clear();
					ArrayList<Pop> tempItems = (ArrayList<Pop>) result
							.get("pops");
					if (tempItems != null)
						for (Pop b : tempItems)
							myPopItems.add(b);
					myProgress.setVisibility(View.GONE);
					mainGridItemAdapterMyPople.notifyDataSetChanged();
				}
			} else if (type.equals("showMakebettinglist")) {
				if (!errorOccured) {
					makedPopItems.clear();
					ArrayList<Pop> tempItems = (ArrayList<Pop>) result
							.get("pops");
					if (tempItems != null)
						for (Pop b : tempItems)
							makedPopItems.add(b);
					makedProgress.setVisibility(View.GONE);
					mainGridItemAdapterMaked.notifyDataSetChanged();
				}
			} else if (type.equals("showJoinbettinglist")) {
				if (!errorOccured) {
					parPopItems.clear();
					ArrayList<Pop> tempItems = (ArrayList<Pop>) result
							.get("pops");
					if (tempItems != null)
						for (Pop b : tempItems)
							parPopItems.add(b);
					parProgress.setVisibility(View.GONE);
					mainGridItemAdapterPar.notifyDataSetChanged();
				}
			} else if (type.equals("showMyproductlist")) {
				if (!errorOccured) {
					productItems.clear();
					ArrayList<Product> tempItems = (ArrayList<Product>) result
							.get("products");
					if (tempItems != null)
						for (Product b : tempItems)
							productItems.add(b);
					productProgress.setVisibility(View.GONE);
					productItemAdapter.notifyDataSetChanged();
				}
			} else if (type.equals("showFriendlist")) {
				if (!errorOccured) {
					ArrayList<User> tempItems = (ArrayList<User>) result
							.get("users");
					if (tempItems != null)
						for (User b : tempItems) {
							FriendUser friendUser = new FriendUser(b);
							friendItems.add(friendUser);
						}
					friendListAdapter.notifyDataSetChanged();
				}
			} else if (type.equals("showFriendrequestlist")) {
				if (!errorOccured) {
					ArrayList<User> tempItems = (ArrayList<User>) result
							.get("users");
					if (tempItems != null)
						for (User b : tempItems) {
							FriendUser friendUser = new FriendUser(b);
							friendUser.setIsRequestTrue();
							friendItems.add(0, friendUser);
						}
					friendListAdapter.notifyDataSetChanged();
				}
			} else if (type.equals("requestFriend")) {
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
