package com.isslam.husonmuslim;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.isslam.husonmuslim.model.DataBaseHelper;
import com.isslam.husonmuslim.model.DataBaseHelper.DataBaseHelperInterface;
import com.isslam.husonmuslim.utils.GlobalConfig;

public class WidgetProvider extends AppWidgetProvider {

	public static DataBaseHelper myDbHelper;
	DataBaseHelperInterface dataBaseHelperInterface;

	public static AppWidgetManager _appWidgetManager;
	public static int[] _appWidgetIds;
	public static int N = 0;

	/*
	 * this method is called every 30 mins as specified on widgetinfo.xml this
	 * method is also called on every phone reboot
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		N = appWidgetIds.length;
		_appWidgetManager = appWidgetManager;
		_appWidgetIds = appWidgetIds;
		Log.e("update2", "updte2");

		Update(context);
		/*
		 * int[] appWidgetIds holds ids of multiple instance of your widget
		 * meaning you are placing more than one widgets on your homescreen
		 */

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	public static void Update(Context context) {

		ComponentName name = new ComponentName(context, WidgetProvider.class);
		_appWidgetManager = AppWidgetManager.getInstance(context);
		_appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
				name);
		N = _appWidgetIds.length;

		try {
			if (myDbHelper == null) {
				Log.e("db", "null");
				myDbHelper = new DataBaseHelper(null);
				myDbHelper.InitDB();
			}
		} catch (Exception e) {
			Log.e("error", "error");
			e.printStackTrace();

		}
		GlobalConfig.widgetSemiChaptersList = myDbHelper.getRandomContent();

		for (int i = 0; i < N; ++i) {
			RemoteViews remoteViews = updateWidgetListView(context,
					_appWidgetIds[i]);
			_appWidgetManager.updateAppWidget(_appWidgetIds[i], remoteViews);
		}
	}

	private static RemoteViews updateWidgetListView(Context context,
			int appWidgetId) {

		// which layout to show on widget
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);

		// RemoteViews Service needed to provide adapter for ListView
		Intent svcIntent = new Intent(context, WidgetService.class);
		// passing app widget id to that RemoteViews Service
		svcIntent.setData(Uri.fromParts("content",
				String.valueOf(_appWidgetIds), null));

		// svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		// setting a unique Uri to the intent
		// don't know its purpose to me right now
		// svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

		// setting adapter to listview of the widget
		//remoteViews.setTextViewText(R.id.chapter_title,
			//	GlobalConfig.widgetSemiChaptersList.get(0).get("chapter_name"));
		remoteViews.setTextViewText(
				R.id.semi_chapter_title,
				GlobalConfig.widgetSemiChaptersList.get(0).get(
						"semi_chapter_name"));
		remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
				svcIntent);

		// register for button event
		remoteViews.setOnClickPendingIntent(R.id.btn_delete,
				buildButtonPendingIntent(context, appWidgetId));
		// setting an empty view in case of no data
		Log.e("---", "llll");
		remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
		_appWidgetManager.notifyAppWidgetViewDataChanged(_appWidgetIds,
				R.id.listViewWidget);

		return remoteViews;
	}

	private static PendingIntent buildButtonPendingIntent(Context context2,
			int appWidgetId) {
		// ++MyWidgetIntentReceiver.clickCount;
		// PendingIntent nextButtonPendingIntent =
		// PendingIntent.getBroadcast(context, widgetId, nextButtonIntent,
		// PendingIntent.FLAG_UPDATE_CURRENT);
		Log.e("click", "click");
		// initiate widget update request
		Intent intent = new Intent();
		intent.setAction("com.isslam.husonmuslim.UPDATE_WIDGET");
		intent.putExtra("appWidgetId", appWidgetId);
		return PendingIntent.getBroadcast(context2, appWidgetId, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		// TODO Auto-generated method stub

	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context,
				WidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}
}
