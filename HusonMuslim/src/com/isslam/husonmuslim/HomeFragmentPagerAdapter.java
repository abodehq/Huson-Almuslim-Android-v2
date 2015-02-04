package com.isslam.husonmuslim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.isslam.husonmuslim.utils.GlobalConfig;

public class HomeFragmentPagerAdapter extends FragmentStatePagerAdapter {

	int PAGE_COUNT = 2;
	private Map<Integer, HomeFragmentPagerContent> mPageReferenceMap = new HashMap<Integer, HomeFragmentPagerContent>();
	private HomeFragment homeFragment;

	/**
	 * Constructor of the class
	 * 
	 * @param homeFragment
	 */

	public HomeFragmentPagerAdapter(FragmentManager fm,
			HomeFragment _homeFragment) {
		// TODO Auto-generated constructor stub

		// super(fm);

		super(fm);
		homeFragment = _homeFragment;
		PAGE_COUNT = GlobalConfig.getSemiChapterList(
				_homeFragment.getActivity()).size();

	}

	/** This method will be invoked when a page is requested to create */
	@Override
	public Fragment getItem(int arg0) {
		HomeFragmentPagerContent myFragment = new HomeFragmentPagerContent();
		Bundle data = new Bundle();
		data.putInt("current_page", arg0 + 1);
		// data.p
		ArrayList<HashMap<String, String>> containerList = GlobalConfig
				.GetmyDbHelper().get_content_by_id(
						GlobalConfig
								.getSemiChapterList(myFragment.getActivity())
								.get(arg0).get("hadith_id"));
		data.putString("content", containerList.get(0).get("content"));
		data.putString("footer", containerList.get(0).get("footer"));
		myFragment.setArguments(data);

		mPageReferenceMap.put(arg0, myFragment);
		return myFragment;
	}

	/** Returns the number of pages */
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return homeFragment.getResources().getString(R.string.hadithNum)
				+ (position + 1);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;

	}

	public HomeFragmentPagerContent getFragment(int key) {

		return mPageReferenceMap.get(key);
	}

	@Override
	public void destroyItem(ViewGroup viewPager, int position, Object object) {

		super.destroyItem(viewPager, position, object);

		mPageReferenceMap.remove(Integer.valueOf(position));
	}

}
