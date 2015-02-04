package com.isslam.husonmuslim;

import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.isslam.husonmuslim.utils.GlobalConfig;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter with
 * few changes
 * 
 */
public class WidgetListProvider implements RemoteViewsFactory {
	private ArrayList<WidgetListItem> listItemList = new ArrayList<WidgetListItem>();
	private Context context = null;
	private int appWidgetId;

	public WidgetListProvider(Context context, Intent intent) {
		this.context = context;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);

		populateListItem();
	}

	private void populateListItem() {
		listItemList = new ArrayList<WidgetListItem>();
		if (GlobalConfig.widgetSemiChaptersList != null) {
			for (int i = 0; i < 1; i++) {
				WidgetListItem listItem = new WidgetListItem();
				listItem.heading = "Heading" + i;
				listItem.content = GlobalConfig.widgetSemiChaptersList.get(0)
						.get("content");
				listItemList.add(listItem);
			}
		}

	}

	@Override
	public int getCount() {
		return listItemList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * Similar to getView of Adapter where instead of Viewwe return RemoteViews
	 */
	@Override
	public RemoteViews getViewAt(int position) {

		final RemoteViews remoteView = new RemoteViews(
				context.getPackageName(), R.layout.ly_widget);
		WidgetListItem listItem = listItemList.get(position);
		remoteView.setTextViewText(R.id.webkit, listItem.content);
		// remoteView.setTextViewText(R.id.content, listItem.content);

		return remoteView;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDataSetChanged() {
		populateListItem();
	}

	@Override
	public void onDestroy() {
	}

}
