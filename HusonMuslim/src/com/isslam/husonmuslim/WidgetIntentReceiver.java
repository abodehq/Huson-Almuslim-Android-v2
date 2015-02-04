package com.isslam.husonmuslim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WidgetIntentReceiver extends BroadcastReceiver {
	public static int clickCount = 0;
	private String msg[] = null;
	int appWidgetId = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		appWidgetId = intent.getIntExtra("appWidgetId", 0);
		if (intent.getAction().equals("com.isslam.husonmuslim.UPDATE_WIDGET")) {
			updateWidgetPictureAndButtonListener(context);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {

		// GlobalConfig.widgetSemiChaptersList = WidgetProvider.myDbHelper
		// .getRandomContent();

		WidgetProvider.Update(context);
		// WidgetProvider.pushWidgetUpdate();
		// updating view

	}

}
