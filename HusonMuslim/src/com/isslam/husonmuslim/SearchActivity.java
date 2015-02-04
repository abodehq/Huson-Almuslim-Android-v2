package com.isslam.husonmuslim;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.isslam.husonmuslim.controllers.BookChaptersManager;
import com.isslam.husonmuslim.controllers.SemiChapterManager;
import com.isslam.husonmuslim.utils.GlobalConfig;

public class SearchActivity extends Activity {
	public static HashMap<String, String> sura;

	@TargetApi(14) public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ly_search);
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES",
				new String[] {});

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);
			// doMySearch(query);
		}
		

		ActionBar	actionbar = getActionBar();
		actionbar.setTitle(getResources().getString(R.string.search_result_pre)+" " +query);
		actionbar.setIcon(R.drawable.ic_action_search);
		
		
		list = (ListView) findViewById(R.id.list);
		_scope = this;
		getTask = new GetTask();
		getTask.execute();

	}

	private SearchActivity _scope;
	private GetTask getTask;
	String query = "";
	ArrayList<HashMap<String, String>> surasList;
	ListView list;
	SearchAdapter reciterItemAdapter;

	private void doMySearch(String query) {
		ArrayList<HashMap<String, String>> surasList = GlobalConfig
				.GetmyDbHelper().getSearch(query);
		String res = query;
		if (!surasList.isEmpty())
			res = query + "-" + surasList.size();
		// Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}

	private class GetTask extends AsyncTask<Void, Void, ReturnModel> {
		@Override
		protected ReturnModel doInBackground(Void... params) {
			return GetData();
		}

		@Override
		protected void onPostExecute(ReturnModel result) {

			surasList = result.getheadlines();

			reciterItemAdapter = new SearchAdapter(_scope, surasList);
			// chapter_name.setText(getString(R.string.search_result));
			String res = query;
			if (!surasList.isEmpty())
				res = query + "-" + surasList.size();
			// Toast.makeText(_scope, res, Toast.LENGTH_SHORT).show();
			list.setAdapter(reciterItemAdapter);

			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

				}
			});

		}
	}

	private ReturnModel GetData() {

		surasList = new ArrayList<HashMap<String, String>>();

		surasList = GlobalConfig.GetmyDbHelper().getSearch(query);

		ReturnModel returnModel = new ReturnModel();
		returnModel.setheadlines(surasList);
		return returnModel;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			// Make sure the request was successful
			if (resultCode == RESULT_OK) {

				BookChaptersManager bookChaptersManager = BookChaptersManager
						.getInstance();
				bookChaptersManager.setselectedChapterId(Integer
						.parseInt(SearchActivity.sura.get("chapter_id")));
				SemiChapterManager semiChapterManager = SemiChapterManager
						.getInstance();
				semiChapterManager.setselectedSemiChapterId(Integer
						.parseInt(SearchActivity.sura.get("semi_chapter_id")));
				semiChapterManager.SetSemiChaptersList(_scope
						.getApplicationContext());
				semiChapterManager.setSelectedContentId(Integer
						.parseInt(SearchActivity.sura.get("_id")));
				GlobalConfig.isSearchActive = true;
				finish();
			}
		}
	}

	private class ReturnModel {
		private ArrayList<HashMap<String, String>> surasList;

		public ArrayList<HashMap<String, String>> getheadlines() {
			return surasList;
		}

		public void setheadlines(ArrayList<HashMap<String, String>> _songsList) {
			this.surasList = _songsList;
		}

	}

}
