package club.sgen.custom;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import club.sgen.network.R;

public class MyPagerAdapter extends PagerAdapter {
	private LayoutInflater inflater;
	private final int COUNT = 3;

	public MyPagerAdapter(Context context) {
		super();
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return COUNT;
	}

	public Object instantiateItem(View pager, int position) {
		View view;

		if (position == 0) {
			view = inflater.inflate(R.layout.fragment_banner, null);
		} else {
			view = inflater.inflate(R.layout.fragment_banner, null);
		}

		((ViewPager) pager).addView(view, 0);
		return view;
	}

	@Override
	public void destroyItem(View pager, int position, Object view) {
		((ViewPager) pager).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}
