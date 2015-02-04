package com.isslam.husonmuslim;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isslam.husonmuslim.controllers.BookChaptersManager;
import com.isslam.husonmuslim.controllers.SemiChapterManager;
import com.isslam.husonmuslim.controllers.SharedPreferencesManager;
import com.isslam.husonmuslim.utils.GlobalConfig;

public class MainFragment extends Fragment {
	MainActivity _FragmentActivity;

	public MainFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_FragmentActivity = (MainActivity) activity;
	}

	ArrayList<HashMap<String, String>> semiChaptersList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// GlobalConfig.myDbHelper.getRandomContent();
		View rootView = inflater.inflate(R.layout.ly_main_fragment, container,
				false);

		TextView chapter_title = (TextView) rootView
				.findViewById(R.id.chapter_title);

		Time now = new Time();
		now.setToNow();
		SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
				.getInstance(_FragmentActivity);
		String saved_date = sharedPreferencesManager.GetStringPreferences(
				SharedPreferencesManager.current_date_key, "-1");
		if (!saved_date.equals(now.format3339(true))) {
			semiChaptersList = GlobalConfig.GetmyDbHelper().getRandomContent();
			sharedPreferencesManager.savePreferences(
					SharedPreferencesManager.current_date_key,
					now.format3339(true));
			sharedPreferencesManager.savePreferences(
					SharedPreferencesManager.current_haidth_id_key,
					semiChaptersList.get(0).get("_id"));

		} else {
			// current_haidth_id_key
			semiChaptersList = GlobalConfig
					.GetmyDbHelper()
					.get_content_by_id(
							sharedPreferencesManager
									.GetStringPreferences(
											SharedPreferencesManager.current_haidth_id_key,
											"1"));
		}

		TextView text_semi_title = (TextView) rootView
				.findViewById(R.id.text_semi_title);

		String semi_chapter_txt = " √ﬂ„· ﬁ—«¡… "
				+ " "
				+ GlobalConfig.getSemiChapterList(_FragmentActivity).get(0)
						.get("name");
		text_semi_title.setText(semi_chapter_txt);
		RelativeLayout reading_container = (RelativeLayout) rootView
				.findViewById(R.id.reading_container);
		reading_container.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_FragmentActivity.displayView(1);
			}
		});
		RelativeLayout rl_today_hadith = (RelativeLayout) rootView
				.findViewById(R.id.rl_today_hadith);
		rl_today_hadith.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BookChaptersManager bookChaptersManager = BookChaptersManager
						.getInstance();
				bookChaptersManager.setselectedChapterId(Integer
						.parseInt(semiChaptersList.get(0).get("chapter_id")));
				SemiChapterManager semiChapterManager = SemiChapterManager
						.getInstance();
				semiChapterManager.setselectedSemiChapterId(Integer
						.parseInt(semiChaptersList.get(0)
								.get("semi_chapter_id")));
				semiChapterManager.SetSemiChaptersList(_FragmentActivity);
				_FragmentActivity.displayView(1);

			}
		});

		RelativeLayout ry_chapters = (RelativeLayout) rootView
				.findViewById(R.id.ry_chapters);
		ry_chapters.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_FragmentActivity.displayView(2);
			}
		});
		RelativeLayout ry_share = (RelativeLayout) rootView
				.findViewById(R.id.ry_share);
		ry_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_FragmentActivity.displayView(5);
			}
		});
		RelativeLayout ly_settings = (RelativeLayout) rootView
				.findViewById(R.id.ly_settings);
		ly_settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_FragmentActivity.displayView(6);
			}
		});
		RelativeLayout ly_read = (RelativeLayout) rootView
				.findViewById(R.id.ly_read);
		ly_read.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_FragmentActivity.displayView(1);
			}
		});
		RelativeLayout ry_fav = (RelativeLayout) rootView
				.findViewById(R.id.ry_fav);
		ry_fav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_FragmentActivity.displayView(3);
			}
		});

		String chapter_pre = getResources().getString(R.string.chapter);
		String semi_chapter_pre = getResources().getString(
				R.string.semi_chapter);
		chapter_title.setText(" –ﬂ— «·ÌÊ„ „‰ " + chapter_pre + " "
				+ semiChaptersList.get(0).get("chapter_name") + " , "
				+ semi_chapter_pre + " "
				+ semiChaptersList.get(0).get("semi_chapter_name"));

		String html = semiChaptersList.get(0).get("content");

		TextView content = (TextView) rootView.findViewById(R.id.webkit);
		content.setEllipsize(TextUtils.TruncateAt.END);
		content.setMaxLines(6);
		content.setText(html);
		content.setTextColor(GlobalConfig.defaultFColor);
		content.setTextSize(GlobalConfig.defaultFontSize);

		return rootView;
	}
}
