package com.isslam.husonmuslim;

import java.util.List;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.isslam.husonmuslim.controllers.BookChaptersManager;
import com.isslam.husonmuslim.model.Chapter;

public class ChaptersActivity extends Fragment {

	private View view;
	private List<Chapter> chapters;
	private ListView chapters_list;
	private ChaptersItemAdapter chaptersItemAdapter;
	private ChaptersActivity _scope;
	private Boolean isCompleted = false;

	private EditText inputSearch;
	private Boolean isFromPreferences = false;
	private Boolean showSettings = false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("chapter", "chapters");
		if (this.getActivity().getLocalClassName()
				.equals("chaptersActivityFragment"))
			showSettings = false;
		else
			showSettings = true;
		ActionBar actionbar;
		actionbar = this.getActivity().getActionBar();
		actionbar.setTitle(getResources().getString(R.string.chapters));
		setHasOptionsMenu(true);
		view = inflater.inflate(R.layout.ly_chapters, container, false);
		_scope = this;

		chapters_list = (ListView) view.findViewById(R.id.chapters_list);
		inputSearch = (EditText) view.findViewById(R.id.chapters_Search);
		inputSearch.setText("");

		// onRequestCompleted();
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (chaptersItemAdapter != null)
					chaptersItemAdapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		inputSearch.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (v == inputSearch) {
					if (hasFocus) {
						inputSearch.setBackgroundColor(Color.WHITE);

					}
				}

			}
		});

		onRequestCompleted();

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock

		switch (item.getItemId()) {
		case 0:
			if (isCompleted) {
				if (chapters_list != null) {
					chapters_list.setAdapter(null);
					isCompleted = false;
				}
			}
			return true;
		case 1:
			isFromPreferences = true;
			Intent intent = new Intent(view.getContext(),
					PreferenceActivity.class);
			intent.putExtra("callerId", "pref");
			startActivityForResult(intent, 1);
			// Do search
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void onRequestCompleted() {
		setupUI();
	}

	@Override
	public void onResume() {

		super.onResume();
		// CheckchaptersList();
		if (isFromPreferences) {
			isFromPreferences = false;

			this.onCreate(null);

		}

	}

	private void setupUI() {
		inputSearch.setText("");
		BookChaptersManager chapterslistManager = BookChaptersManager
				.getInstance();
		chapters = chapterslistManager.getChapters();
		chaptersItemAdapter = new ChaptersItemAdapter(_scope, chapters);
		chapters_list.setAdapter(chaptersItemAdapter);
		chapters_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View aView,
					int position, long arg3) {
				int _position = chaptersItemAdapter
						.getSelectedFiltteredChapterId(position);
				if (_position == -1)
					_position = position;
				SemiChapterActivity.dummyChapterId = _position;
				((BookTabsActivity) getActivity()).setSelectedTab(1);
			}

		});
		int top = chapters_list.getTop();
		chapters_list.setSelectionFromTop(
				chapterslistManager.getCurrentSelectedChapterPosition(), top);
		chaptersItemAdapter.setSelectedItem(chapterslistManager
				.getCurrentSelectedChapterPosition());
		isCompleted = true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {

		}

	}

}