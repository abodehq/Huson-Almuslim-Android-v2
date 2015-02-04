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

import com.isslam.husonmuslim.controllers.BookChaptersManager;
import com.isslam.husonmuslim.model.Chapter;
import com.isslam.husonmuslim.utils.GlobalConfig;

public class ChaptersItemAdapter extends BaseAdapter implements Filterable {

	private ChaptersActivity activity;
	private List<Chapter> data;
	private List<Chapter> mOriginalValues;
	private static LayoutInflater inflater = null;
	private Chapter chapter;
	private String fontPath;
	private Typeface tf;
	private String txt_chapter = " txt_chapter ";
	private BookChaptersManager bookChaptersManager;

	public ChaptersItemAdapter(ChaptersActivity _scope,
			List<Chapter> recitersList) {
		activity = _scope;
		fontPath = GlobalConfig.fontPath;
		tf = Typeface.createFromAsset(activity.getActivity().getAssets(),
				fontPath);
		data = recitersList;
		inflater = (LayoutInflater) activity.getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		txt_chapter = activity.getResources().getString(R.string.chapter) + " ";
		bookChaptersManager = BookChaptersManager.getInstance();
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
			vi = inflater.inflate(R.layout.ly_chapters_item, null);

		}
		TextView chapter_name = (TextView) vi.findViewById(R.id.chapter_name);
		chapter_name.setTypeface(tf);
		if (chapter_name != null) {
			chapter = data.get(position);
			chapter_name.setText(txt_chapter + " " + chapter.getchapterName());
		}
		if (chapter.getChapterId() == bookChaptersManager
				.getselectedChapterId()) {

			vi.setBackgroundResource(R.color.list_background_pressed2);
		} else {
			vi.setBackgroundResource(R.drawable.list_selector2);
		}
		return vi;
	}

	private int selectedItem;

	public void setSelectedItem(int position) {
		selectedItem = position;
	}

	public int getSelectedFiltteredChapterId(int position) {
		Log.e("Position", position + "");
		if (data == null || data.get(position) == null)
			return -1;
		Log.e("Position", data.get(position).getchapterName() + "");
		return data.get(position).getChapterId();
	}

	List<Chapter> FilteredArrList = null;

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				data = (List<Chapter>) results.values; // has the filtered
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
				FilteredArrList = new ArrayList<Chapter>();

				if (mOriginalValues == null) {
					mOriginalValues = new ArrayList<Chapter>(data); // saves
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
						String data = mOriginalValues.get(i).getchapterName();
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