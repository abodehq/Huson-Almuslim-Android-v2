package com.isslam.husonmuslim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.isslam.husonmuslim.controllers.BookChaptersManager;
import com.isslam.husonmuslim.controllers.SemiChapterManager;
import com.isslam.husonmuslim.controllers.UndoManager;
import com.isslam.husonmuslim.utils.GlobalConfig;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener, SearchView.OnQueryTextListener,
		SearchView.OnCloseListener {

	// **new code
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private MainActivity _scope;
	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter1;
	// end code

	// action bar
	private ActionBar actionBar;

	// Refresh menu item
	private MenuItem refreshMenuItem;

	@TargetApi(14) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		_scope = this;
		actionBar = getActionBar();

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
	
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		// What's hot, We will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
				.getResourceId(7, -1)));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons
				.getResourceId(8, -1)));
		// navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
		// .getResourceId(3, -1), true, ""));
		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter1 = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter1);

		// enabling action bar app icon and behaving it as toggle button
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					displayViewRun(0);

				}
			}, 200);
		}
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		// new code
		// getMenuInflater().inflate(R.menu.main, menu);
		setupSearchView();
		// end
		return super.onCreateOptionsMenu(menu);
	}

	SearchView searchView;

	private void setupSearchView() {

		searchView.setIconifiedByDefault(true);
		int searchPlateId = searchView.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		EditText searchPlate = (EditText) searchView
				.findViewById(searchPlateId);
		searchPlate.setTextColor(getResources().getColor(
				R.color.main_theme_color));
		// searchPlate.setBackgroundResource(R.color.main_theme_color);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if (searchManager != null) {
			List<SearchableInfo> searchables = searchManager
					.getSearchablesInGlobalSearch();

			// Try to use the "applications" global search provider
			SearchableInfo info = searchManager
					.getSearchableInfo(getComponentName());
			for (SearchableInfo inf : searchables) {
				if (inf.getSuggestAuthority() != null
						&& inf.getSuggestAuthority().startsWith("applications")) {
					info = inf;
				}
			}
			// searchView.setSearchableInfo(info);
		}

		searchView.setOnQueryTextListener(_scope);
		searchView.setOnCloseListener(_scope);
	}

	public boolean onQueryTextChange(String newText) {
		Log.e("Query = ", newText);
		return false;
	}

	public boolean onQueryTextSubmit(String query) {
		Log.e("Query = ", query + " : submitted");
		return false;
	}

	public boolean onClose() {
		Log.e("Closed!", "close");
		return false;
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			// display view for selected nav drawer item
			displayView(position);
		}
	}

	public void displayView(int position) {

		setTitle(getString(R.string.loading_text));
		int delay = 400;
		final int _position = position;
		switch (position) {
		case 0:
		case 1:
		case 3:
		case 4:
			Fragment fragment = null;
			fragment = new LoadingFragment();
			FragmentManager fragmentManager = getFragmentManager();

			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			break;
		case 2:
		case 5:
		case 6:
		case 7:
		case 8:
			delay = 100;
			break;
		default:
			delay = 400;
			break;
		}
		mDrawerLayout.closeDrawer(mDrawerList);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				displayViewRun(_position);

			}
		}, delay);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	// public void OnPageViewClickListener() {

	// ((HomeFragment) (fragment)).OnPageViewClickListener();
	// }

	Fragment fragment = null;
	int currentPosition = 0;
	int dummyChapter = -1;
	int dummySemiChapter = 0;
	boolean add_to_undo = true;
	public void displayViewRun(int position) {
		// update the main content by replacing fragments
		UndoManager undoManager = UndoManager.getInstance();
		if (position != 2&& position != 5 && position != 6 && position != 7) {
			if (fragment != null) {
				getFragmentManager().beginTransaction().remove(fragment)
						.commit();

			}
		}
		fragment = null;
		
		
		Intent i = null;
		
		switch (position) {
		case 0:
		case 1:		
		case 3:
		case 4:
		
			if (add_to_undo)
				undoManager.addUndoAction(currentPosition, dummyChapter, dummySemiChapter);
			add_to_undo = true;
			break;

		}
		
		switch (position) {
		case 0:
			
			fragment = new MainFragment();

			break;
		case 1:

			mDrawerLayout.closeDrawer(mDrawerList);
			BookChaptersManager bookChaptersManager = BookChaptersManager
					.getInstance();
			SemiChapterManager semiChapterManager = SemiChapterManager
					.getInstance();
			dummyChapter = bookChaptersManager.getselectedChapterId();
			dummySemiChapter = semiChapterManager.getselectedSemiChapterId();
			fragment = new HomeFragment();
			
			break;
		case 2:
			i = new Intent();
			i.setClass(_scope, SemiChapterActivity.class);
			i.putExtra("selectedTab", "1");
			mDrawerList.setItemChecked(position, false);
			setTitle(navMenuTitles[0]);
			mDrawerLayout.closeDrawer(mDrawerList);
			startActivityForResult(i, 1);
			break;
		case 3:
			
			fragment = new FavouriteFragment();

			break;
		case 4:
			
			fragment = new NotesFragment();

			break;
		case 5:
			Intent sharingIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String shareBody = getString(R.string.share_body) + "\n"
					+ "http://play.google.com/store/apps/details?id="
					+ _scope.getPackageName();
			;
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					getString(R.string.share_title));
			sharingIntent
					.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			startActivity(Intent.createChooser(sharingIntent,
					getString(R.string.share_title)));

			break;
		case 6:
			i = new Intent();

			i.setClass(_scope, SittingsActivity.class);
			mDrawerList.setItemChecked(position, false);
			setTitle(navMenuTitles[0]);
			mDrawerLayout.closeDrawer(mDrawerList);
			startActivity(i);
			break;
		case 7:

			mDrawerList.setItemChecked(position, false);
			setTitle(navMenuTitles[0]);
			mDrawerLayout.closeDrawer(mDrawerList);
			exitByBackKey();
			break;

		default:
			break;
		}

		if (fragment != null) {

			FragmentManager fragmentManager = getFragmentManager();

			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			currentPosition = position;
		} else {
			// error in creating fragment
			Log.e("MainActivity", "------");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_search:
			// search action
			return true;
		case R.id.action_read:
			displayView(1);
			return true;
		case R.id.action_semi_chapters:
			// refresh
			displayView(2);
			return true;

		case R.id.action_settings:
			displayView(6);
			return true;
		case R.id.exit:
			exitByBackKey();
			return true;
		case R.id.fav:
			displayView(3);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

		// toggle nav drawer on selecting action bar app icon/title

	}

	/**
	 * Launching new activity
	 * */

	/*
	 * Actionbar navigation item select listener
	 */
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// Action to be taken after selecting a spinner item
		return false;
	}

	/**
	 * Async task to load the data from server
	 * **/
	private class SyncData extends AsyncTask<String, Void, String> {
		@TargetApi(14) @Override
		protected void onPreExecute() {
			// set the progress bar view
			refreshMenuItem.setActionView(R.layout.action_progressbar);

			refreshMenuItem.expandActionView();
		}

		@Override
		protected String doInBackground(String... params) {
			// not making real request in this demo
			// for now we use a timer to wait for sometime
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@TargetApi(14) @Override
		protected void onPostExecute(String result) {
			refreshMenuItem.collapseActionView();
			// remove the progress bar view
			refreshMenuItem.setActionView(null);
		}
	};

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		menu.findItem(R.id.exit).setVisible(!drawerOpen);
		menu.findItem(R.id.fav).setVisible(!drawerOpen);
		MenuItem searchViewMenuItem = menu.findItem(R.id.action_search);
		SearchView mSearchView = (SearchView) searchViewMenuItem
				.getActionView();
		int searchImgId = getResources().getIdentifier(
				"android:id/search_button", null, null);
		ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
		v.setImageResource(R.drawable.ic_action_search);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("result", "result from main activity");
		if (requestCode == 1) {

			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				displayView(1);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (GlobalConfig.isSearchActive) {

			GlobalConfig.isSearchActive = false;
			displayView(1);
		}
		Log.e("onresume", "onresume");
	}

	@Override
	public void onBackPressed() {
		UndoManager undoManager = UndoManager.getInstance();
		HashMap<String, Integer> map = undoManager.getUndoAction();
		if (map.get("_id") == -1)
			exitByBackKey();
		else {
			if (map.get("_id") == 1) {
				BookChaptersManager bookChaptersManager = BookChaptersManager
						.getInstance();
				SemiChapterManager semiChapterManager = SemiChapterManager
						.getInstance();
				bookChaptersManager.setselectedChapterId(map.get("val_1"));
				semiChapterManager.setselectedSemiChapterId(map.get("val_2"));
				semiChapterManager.SetSemiChaptersList(_scope);
			}
			add_to_undo = false;
			displayView(map.get("_id"));
		}

	}

	private void exitByBackKey() {

		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.exit))
				.setMessage(getResources().getString(R.string.app_exit))
				.setPositiveButton(
						getResources().getString(R.string.app_exit_confirm),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
								System.exit(0);

							}
						})
				.setNegativeButton(
						getResources().getString(R.string.app_exit_cancle),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();

	}

}
