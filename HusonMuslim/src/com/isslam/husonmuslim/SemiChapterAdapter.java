package com.isslam.husonmuslim;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.isslam.husonmuslim.controllers.SemiChapterManager;
import com.isslam.husonmuslim.model.SemiChapter;
import com.isslam.husonmuslim.utils.GlobalConfig;

public class SemiChapterAdapter extends BaseAdapter implements Filterable {

	private SemiChapterActivity activity;
	private List<SemiChapter> data;
	private List<SemiChapter> mOriginalValues;
	private static LayoutInflater inflater = null;
	private SemiChapter sura;
	private String fontPath;
	private Typeface tf;
	private String Verse = " verse ";
	String extraChar="Ü";
	private SemiChapterManager semiChapterManager;

	public SemiChapterAdapter(SemiChapterActivity _scope,
			List<SemiChapter> suras) {
		activity = _scope;
		fontPath = GlobalConfig.fontPath;
		tf = Typeface.createFromAsset(activity.getAssets(),
				fontPath);
		data = suras;
		inflater = (LayoutInflater) activity.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		Verse = activity.getResources().getString(R.string.semi_chapter) + " ";
		semiChapterManager = SemiChapterManager.getInstance();
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

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		if (convertView == null) {
			vi = inflater.inflate(R.layout.ly_semi_chapter_item, null);

		}// imageLoader.DisplayImage(sura.get("reciterImage"), thumb_image);
		TextView sura_name = (TextView) vi.findViewById(R.id.sura_name);
		TextView verses_count = (TextView) vi.findViewById(R.id.verses_count);
		sura_name.setTypeface(tf);
		verses_count.setTypeface(tf);
		if (sura_name != null) {
			sura = data.get(position);
			sura_name.setText(Verse + " " + sura.getSemichapterName());
			if (sura.getSemiChapterId() == semiChapterManager
					.getselectedSemiChapterId()) {

				vi.setBackgroundResource(R.color.list_background_pressed2);
			} else {
				vi.setBackgroundResource(R.drawable.list_selector2);
			}
			// verses_count.setText(sura.getchapterName() + " " + Verse);
		}

		return vi;
	}

	private int selectedItem;

	public void setSelectedItem(int position) {
		selectedItem = position;
	}

	public int getSelectedFiltteredSuraId(int position) {
		if (data == null || data.get(position) == null)
			return -1;
		Log.e("Position", data.get(position).getSemiChapterId() + "");
		return data.get(position).getSemiChapterId();
	}

	List<SemiChapter> FilteredArrList = null;

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				data = (List<SemiChapter>) results.values; // has the filtered
															// values
				notifyDataSetChanged(); // notifies the data with new filtered
										// values
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults(); // Holds the
																// results of a
																// filtering
																// operation in
																// values
				FilteredArrList = new ArrayList<SemiChapter>();

				if (mOriginalValues == null) {
					mOriginalValues = new ArrayList<SemiChapter>(data); // saves
					// the
					// original
					// data
					// in
					// mOriginalValues
				}

				/********
				 * 
				 * If constraint(CharSequence that is received) is null returns
				 * the mOriginalValues(Original) values else does the Filtering
				 * and returns FilteredArrList(Filtered)
				 * 
				 ********/
				if (constraint == null || constraint.length() == 0) {

					// set the Original result to return
					results.count = mOriginalValues.size();
					results.values = mOriginalValues;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < mOriginalValues.size(); i++) {
						String data = mOriginalValues.get(i)
								.getSemichapterName();
						data=data.replace(extraChar, "");
						if (data.toLowerCase().contains(constraint.toString())) {
							FilteredArrList.add(mOriginalValues.get(i));
						}
					}
					// set the Filtered result to return
					results.count = FilteredArrList.size();
					results.values = FilteredArrList;
				}
				return results;
			}
		};
		return filter;
	}

}