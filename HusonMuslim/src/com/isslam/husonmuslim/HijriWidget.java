package com.isslam.husonmuslim;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.isslam.husonmuslim.model.DataBaseHelper;
import com.isslam.husonmuslim.model.DataBaseHelper.DataBaseHelperInterface;

public class HijriWidget extends AppWidgetProvider implements
		DataBaseHelperInterface {
	private DataBaseHelper myDbHelper;
	DataBaseHelperInterface dataBaseHelperInterface;
	RemoteViews remoteViews;
	AppWidgetManager _appWidgetManager;
	ComponentName watchWidget;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		DateFormat format = SimpleDateFormat.getTimeInstance(
				SimpleDateFormat.MEDIUM, Locale.getDefault());

		remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.ly_widget);
		_appWidgetManager = appWidgetManager;
		dataBaseHelperInterface = this;
		try {
			if (myDbHelper == null)
				myDbHelper = new DataBaseHelper(dataBaseHelperInterface);
			myDbHelper.InitDB();
		} catch (Exception e) {

		} // content.setBackgroundColor(AppManager.bg_color);

		watchWidget = new ComponentName(context, HijriWidget.class);
		remoteViews.setTextViewText(R.id.webkit, "ww");
		onRequestCompleted1();
		appWidgetManager.updateAppWidget(watchWidget, remoteViews);
	}

	@Override
	public void onRequestCompleted() {

	}

	public void onRequestCompleted1() {
		// TODO Auto-generated method stub

		ArrayList<HashMap<String, String>> semiChaptersList = myDbHelper
				.getRandomContent();
		String html = semiChaptersList.get(0).get("content");

		remoteViews.setTextViewText(R.id.webkit, html);
		// content.loadDataWithBaseURL(null, result, "text/html", "UTF-8",
		// "UTF-8");

		// list = (ListView) remoteViews.findViewById(R.id.list);
		_appWidgetManager.updateAppWidget(watchWidget, remoteViews);
	}
}
