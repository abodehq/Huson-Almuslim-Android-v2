package com.isslam.husonmuslim;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.isslam.husonmuslim.controllers.BookChaptersManager;

public class BookTabsActivity extends Activity {
	public static Context appContext;
	ActionBar.Tab PlayerTab;
	ActionBar.Tab StationsTab;
	ActionBar actionbar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ly_book_tabs);
		appContext = getApplicationContext();

		// ActionBar
		actionbar = getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		PlayerTab = actionbar.newTab().setText(R.string.chapters);
		StationsTab = actionbar.newTab().setText(R.string.semi_chapters);

		Fragment PlayerFragment = new ChaptersActivity();
		Fragment StationsFragment = new ChaptersActivity();

		PlayerTab.setTabListener(new MyTabsListener(PlayerFragment));
		StationsTab.setTabListener(new MyTabsListener(StationsFragment));

		actionbar.addTab(PlayerTab);
		actionbar.addTab(StationsTab);

		Intent i = getIntent();
		String selectedTab = i.getStringExtra("selectedTab");
		if (selectedTab.equals("1")) {
			actionbar.selectTab(StationsTab);
		}

	}

	public void setSelectedTab(int index) {
		actionbar.selectTab(StationsTab);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return false;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public void onBackPressed() {
		BookChaptersManager bookChaptersManager = BookChaptersManager
				.getInstance();
		// bookChaptersManager.setselectedChapterId(_position);

		SemiChapterActivity.dummyChapterId = bookChaptersManager
				.getselectedChapterId();
		finish();

	}

}

class MyTabsListener implements ActionBar.TabListener {
	public Fragment fragment;

	public MyTabsListener(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Toast.makeText(BookTabsActivity.appContext, "Reselected!",
		// Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// Log.e("fragment:", fragment + "");
		ft.replace(R.id.fragment_container, fragment);

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// Log.e("unf:", fragment + "");
		ft.remove(fragment);
	}

}