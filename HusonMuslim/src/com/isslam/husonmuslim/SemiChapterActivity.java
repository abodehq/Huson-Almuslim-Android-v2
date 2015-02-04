package com.isslam.husonmuslim;

import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import com.isslam.husonmuslim.controllers.SemiChapterManager;
import com.isslam.husonmuslim.model.SemiChapter;

public class SemiChapterActivity extends Activity {

	
	private List<SemiChapter> suras;
	private ListView suras_list;
	private SemiChapterAdapter surasItemAdapter;
	private SemiChapterActivity _scope;
	private Boolean isCompleted = false;

	private EditText inputSearch;
	private Boolean isFromPreferences = false;
	private Boolean showSettings = false;
	ActionBar actionbar;
	public static int dummyChapterId = 1;
	
	@TargetApi(14) @Override
	public void onCreate(Bundle savedInstanceState) {
		Log.e("semi", "semi");
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ly_semi_chapter);
		if (getLocalClassName()
				.equals("SurasActivityFragment"))
			showSettings = false;
		else
			showSettings = true;

		
		actionbar = getActionBar();
		actionbar.setTitle(getResources().getString(R.string.menu_semi_list));
		actionbar.setIcon(R.drawable.ic_action_semi_chapter);
		
		_scope = this;

		suras_list = (ListView) findViewById(R.id.suras_list);
		inputSearch = (EditText) findViewById(R.id.suras_Search);
		inputSearch.setText("");

		onRequestCompleted();

		// onRequestCompleted();
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				if (surasItemAdapter != null)
					surasItemAdapter.getFilter().filter(cs);
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
		
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock

		switch (item.getItemId()) {
		case 0:
			if (isCompleted) {
				if (suras_list != null) {
					suras_list.setAdapter(null);

					isCompleted = false;
					// surasInfoLoader.Execute();
				}
			}
			return true;
		case 1:
			isFromPreferences = true;
			Intent intent = new Intent(_scope,
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
		if (isFromPreferences) {
			isFromPreferences = false;
			// updateLocal();
			this.onCreate(null);

		}

	}

	private void setupUI() {
		inputSearch.setText("");
		SemiChapterManager suraslistManager = SemiChapterManager.getInstance();

		BookChaptersManager bookChaptersManager = BookChaptersManager
				.getInstance();
		suraslistManager.setDummyBookSemiChapter(String
				.valueOf(SemiChapterActivity.dummyChapterId));
		suras = suraslistManager.getDummySemiChapters();
	//	actionbar.setTitle(this.getResources().getString(R.string.chapter)
			//	+ " " + suras.get(0).getchapterName());
		surasItemAdapter = new SemiChapterAdapter(_scope, suras);
		suras_list.setAdapter(surasItemAdapter);
		suras_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View aView,
					int position, long arg3) {
				// SuraslistManager suraslistManager = SuraslistManager
				// .getInstance();

				int _position = surasItemAdapter
						.getSelectedFiltteredSuraId(position);
				if (_position == -1)
					_position = position;
				SemiChapterManager semiChapterManager = SemiChapterManager
						.getInstance();
				semiChapterManager.setselectedSemiChapterId(_position);

				BookChaptersManager bookChaptersManager = BookChaptersManager
						.getInstance();
				bookChaptersManager
						.setselectedChapterId(SemiChapterActivity.dummyChapterId);
				semiChapterManager.SetSemiChaptersList(_scope
						.getApplicationContext());

				Intent in = new Intent(_scope
						.getApplicationContext(), com.isslam.husonmuslim.MainActivity.class);

				_scope.setResult(Activity.RESULT_OK, in);
				_scope.finish();

			}

		});
		int top = suras_list.getTop();
		suras_list.setSelectionFromTop(
				suraslistManager.getCurrentSelectedSemiChapterPosition(), top);
		surasItemAdapter.setSelectedItem(suraslistManager
				.getCurrentSelectedSemiChapterPosition());
		isCompleted = true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {

		}

	}

}