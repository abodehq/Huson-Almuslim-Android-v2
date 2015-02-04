package com.isslam.husonmuslim;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isslam.husonmuslim.controllers.BookChaptersManager;
import com.isslam.husonmuslim.controllers.SemiChapterManager;
import com.isslam.husonmuslim.utils.GlobalConfig;

public class SearchAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	// public ImageLoader imageLoader;

	public SearchAdapter(Activity a, ArrayList<HashMap<String, String>> _source) {
		activity = a;
		data = _source;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	HashMap<String, String> sura;
	TextView content;

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.ly_search_item, null);

		// TextView chapter = (TextView) vi.findViewById(R.id.chapter_title);
		TextView text_title = (TextView) vi.findViewById(R.id.text_title);
		// TextView content = (TextView) vi.findViewById(R.id.content);
		// ImageView thumb_image = (ImageView) vi
		// .findViewById(R.id.semi_chapter_img);
		// thumb_image.setImageResource(R.drawable.pageicon);
		// ImageView chapter_img = (ImageView)
		// vi.findViewById(R.id.chapter_img);
		// chapter_img.setImageResource(R.drawable.chaptericon);
		sura = new HashMap<String, String>();
		sura = data.get(position);
		String fontPath = "fonts/arabic.ttf";
		Typeface tf = Typeface.createFromAsset(activity.getAssets(), fontPath);
		// chapter.setTypeface(tf);
		// chapter.setText(sura.get("chaptername"));
		text_title.setTypeface(tf);
		// semichapter.setText(sura.get("semichaptername"));
		String chapter_pre = activity.getResources()
				.getString(R.string.chapter);
		String semi_chapter_pre = activity.getResources().getString(
				R.string.semi_chapter);
		text_title.setText(" " + chapter_pre + " " + sura.get("chaptername")
				+ " , " + semi_chapter_pre + " " + sura.get("semichaptername"));
		// fontPath = "fonts/" +
		// AppManager.fonts[AppManager.selected_font_index]
		// + ".ttf";

		RelativeLayout nafigation_container = (RelativeLayout) vi
				.findViewById(R.id.nafigation_container);
		nafigation_container.setTag(position);
		nafigation_container.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sura = data.get(Integer.parseInt(String.valueOf(v.getTag())));

				BookChaptersManager bookChaptersManager = BookChaptersManager
						.getInstance();
				bookChaptersManager.setselectedChapterId(Integer.parseInt(sura
						.get("chapter_id")));
				SemiChapterManager semiChapterManager = SemiChapterManager
						.getInstance();
				semiChapterManager.setselectedSemiChapterId(Integer
						.parseInt(sura.get("semi_chapter_id")));
				semiChapterManager.SetSemiChaptersList(activity
						.getApplicationContext());
				semiChapterManager.setSelectedContentId(Integer.parseInt(sura
						.get("_id")));
				SearchActivity.sura = sura;
				Intent i = new Intent();
				i.setClass(activity, HadithActivity.class);
				i.putExtra("selectedTab", "0");
				activity.startActivityForResult(i, 1);
			}
		});
		tf = Typeface.createFromAsset(activity.getAssets(), fontPath);
		// content.setTypeface(tf);
		// content.setText(sura.get("content"));
		// String font_color = String.format("#%06X", (0xFFFFFF & Color.BLACK));
		// String font_type = AppManager.fonts[AppManager.selected_font_index];

		String html = sura.get("content");
		// html = html.replace("[1]", font_type);
		String font_color = String.format("#%06X", (0xFFFFFF & Color.BLACK));
		String font_type = "font1";// AppManager.fonts[AppManager.selected_font_index];

		String result = GlobalConfig.htmlContentStructure;

		result = result.replace("[5]", "height: 60px; overflow: hidden;");
		result = result.replace("[4]", html);
		result = result.replace("[1]", font_type);
		result = result.replace("[2]", "" + 14);
		result = result.replace("[3]", font_color);

		content = (TextView) vi.findViewById(R.id.webkit);
		content.setTextColor(GlobalConfig.defaultFColor);
		// content.setBackgroundColor(GlobalConfig.defaultBgColor);
		content.setTextSize(GlobalConfig.defaultFontSize);
		content.setEllipsize(TextUtils.TruncateAt.END);
		content.setMaxLines(4);
		content.setText(html);
		return vi;
	}
}