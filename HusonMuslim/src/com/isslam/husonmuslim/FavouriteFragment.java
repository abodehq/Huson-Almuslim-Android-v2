package com.isslam.husonmuslim;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.isslam.husonmuslim.utils.GlobalConfig;

public class FavouriteFragment extends Fragment {

	MainActivity _FragmentActivity;
	ListView list;
	// TextView chapter_name;
	FavouriteAdapter favouriteAdapter;
	private FavouriteFragment _scope;
	private GetTask getTask;
	private ArrayList<HashMap<String, String>> semiChaptersList;
	View rootView;

	public FavouriteFragment() {
		Log.e("home", "home construct");
	}

	public void Refresh() {
		getTask.cancel(true);
		getTask = new GetTask();
		getTask.execute();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_FragmentActivity = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		_scope = this;
		if (GlobalConfig.GetmyDbHelper().get_fav_is_empty()) {
			rootView = inflater
					.inflate(R.layout.ly_fav_empty, container, false);
			return rootView;

		} else {
			rootView = inflater.inflate(R.layout.ly_fav, container, false);
		}
		list = (ListView) rootView.findViewById(R.id.list);

		RelativeLayout relativeclic1 = (RelativeLayout) rootView
				.findViewById(R.id.btn_m_refresh);
		relativeclic1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getTask.cancel(true);
				getTask = new GetTask();
				getTask.execute();
			}
		});

		RelativeLayout btn_m_deleteAll = (RelativeLayout) rootView
				.findViewById(R.id.btn_m_deleteAll);
		btn_m_deleteAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DeleteDialog();
			}
		});

		getTask = new GetTask();
		getTask.execute();
		return rootView;
	}

	private class GetTask extends AsyncTask<Void, Void, ReturnModel> {
		@Override
		protected ReturnModel doInBackground(Void... params) {
			return GetData();
		}

		@Override
		protected void onPostExecute(ReturnModel result) {

			semiChaptersList = result.get_semi_chapters();

			favouriteAdapter = new FavouriteAdapter(_FragmentActivity,
					semiChaptersList, _scope);
			// chapter_name.setText(getString(R.string.fav_list));
			list.setAdapter(favouriteAdapter);

			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					load_content(position);
					// setResult(200, in);
					// finish();
				}
			});
		}
	}

	public void load_content(int position) {

	}

	private ReturnModel GetData() {
		semiChaptersList = new ArrayList<HashMap<String, String>>();

		semiChaptersList = GlobalConfig.GetmyDbHelper().get_semi_chapters_fav();
		ReturnModel returnModel = new ReturnModel();
		returnModel.set_semi_chapters(semiChaptersList);
		return returnModel;
	}

	private class ReturnModel {
		private ArrayList<HashMap<String, String>> semiChaptersList;

		public ArrayList<HashMap<String, String>> get_semi_chapters() {
			return semiChaptersList;
		}

		public void set_semi_chapters(
				ArrayList<HashMap<String, String>> _semiChaptersList) {
			this.semiChaptersList = _semiChaptersList;
		}

	}

	private void DeleteDialog() {
		if (GlobalConfig.GetmyDbHelper().get_fav_is_empty()) {
			GlobalConfig.ShowToast(_FragmentActivity, _FragmentActivity
					.getResources().getString(R.string.fav_empty));

		} else {
			new AlertDialog.Builder(_FragmentActivity)
					.setTitle(
							_FragmentActivity.getResources().getString(
									R.string.fav_title))
					.setMessage(
							_FragmentActivity.getResources().getString(
									R.string.fav_all_deletion))
					.setPositiveButton(
							_FragmentActivity.getResources().getString(
									R.string.yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete

									// TODO Auto-generated method stub
									GlobalConfig.GetmyDbHelper()
											.delete_all_fav();
									GlobalConfig
											.ShowToast(
													_FragmentActivity,
													_FragmentActivity
															.getResources()
															.getString(
																	R.string.fav_all_del));
									getTask.cancel(true);
									getTask = new GetTask();
									getTask.execute();

								}
							})
					.setNegativeButton(
							_FragmentActivity.getResources().getString(
									R.string.no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		}
	}
}
