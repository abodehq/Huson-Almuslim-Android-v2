package com.isslam.husonmuslim;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isslam.husonmuslim.utils.GlobalConfig;

public class HadithActivity extends Activity {
	private HadithActivity _scope;

	ArrayList<HashMap<String, String>> surasList;
	ImageView img_fav;

	@TargetApi(14) public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ly_hadith_item);
		_scope = this;
		img_fav = (ImageView) findViewById(R.id.img_fav);

		String chapter_pre = getResources().getString(R.string.chapter);
		String semi_chapter_pre = getResources().getString(
				R.string.semi_chapter);

		TextView content = (TextView) findViewById(R.id.webkit);
		TextView txt_footer = (TextView) findViewById(R.id.txt_footer);
		txt_footer.setText(SearchActivity.sura.get("footer"));
		String html = SearchActivity.sura.get("content");
	//	TextView chapter_title = (TextView) findViewById(R.id.chapter_title);
		//chapter_title.setText(chapter_pre + " "
			//	+ SearchActivity.sura.get("chaptername"));
		ActionBar	actionbar = getActionBar();
	//	actionbar.setTitle(getResources().getString(R.string.search_result_pre)+" " +query);
		actionbar.setIcon(R.drawable.ic_action_semi_chapter);
		
		
		TextView semi_chapter_title = (TextView) findViewById(R.id.semi_chapter_title);
		semi_chapter_title.setText(semi_chapter_pre + " "
				+ SearchActivity.sura.get("semichaptername"));
		content.setTextColor(GlobalConfig.defaultFColor);
		content.setTextSize(GlobalConfig.defaultFontSize);
		content.setText(html);

		RelativeLayout btn_m_share = (RelativeLayout) findViewById(R.id.btn_m_share);

		btn_m_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);

				sendIntent.putExtra(Intent.EXTRA_TEXT,
						SearchActivity.sura.get("content"));
				sendIntent.setType("text/plain");

				startActivity(Intent.createChooser(sendIntent, getResources()
						.getText(R.string.share_hadith)));
			}
		});

		ToggleFav();
		RelativeLayout btn_m_fav = (RelativeLayout) findViewById(R.id.btn_m_fav);

		btn_m_fav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				GlobalConfig.GetmyDbHelper().toggle_fav(
						SearchActivity.sura.get("_id"));
				if (ToggleFav()) {
					GlobalConfig.ShowToast(_scope,
							getResources().getString(R.string.fav_add));
				} else {
					GlobalConfig.ShowToast(_scope,
							getResources().getString(R.string.fav_del));
				}

			}
		});
		RelativeLayout btn_m_read = (RelativeLayout) findViewById(R.id.btn_m_read);

		btn_m_read.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent(_scope.getApplicationContext(),
						com.isslam.husonmuslim.MainActivity.class);

				_scope.setResult(Activity.RESULT_OK, in);
				_scope.finish();
			}
		});

		RelativeLayout btn_m_copy = (RelativeLayout) findViewById(R.id.btn_m_copy);
		btn_m_copy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				android.content.ClipboardManager clipboard = (android.content.ClipboardManager) _scope
						.getSystemService(Context.CLIPBOARD_SERVICE);
				android.content.ClipData clip = android.content.ClipData
						.newPlainText("«·ÕœÌÀ",
								SearchActivity.sura.get("content"));
				clipboard.setPrimaryClip(clip);
				GlobalConfig.ShowToast(_scope,
						getResources().getString(R.string.copy_success));
			}
		});

	}

	Boolean ToggleFav() {
		boolean result = GlobalConfig.GetmyDbHelper().get_fav_count(
				SearchActivity.sura.get("_id"));

		if (result) {
			img_fav.setImageResource(R.drawable.ic_action_favorite_selected);

		} else {
			img_fav.setImageResource(R.drawable.ic_action_favorite_light);

		}
		return result;
	}
}
