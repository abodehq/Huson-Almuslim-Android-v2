package com.isslam.husonmuslim;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.Time;

import com.isslam.husonmuslim.controllers.SharedPreferencesManager;

/**
 * context activity will be called when the alarm is triggered.
 * 
 * @author Michael Irwin
 */

public class AlarmReceiverActivity extends BroadcastReceiver {
	private MediaPlayer mMediaPlayer;
	ArrayList<HashMap<String, String>> semiChaptersList;

	public void onReceive(Context context, Intent intent) {

		Time now = new Time();
		now.setToNow();
		// Toast.makeText(context, now.format3339(true), Toast.LENGTH_SHORT)
		// .show();
		SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
				.getInstance(context);
		String saved_date = sharedPreferencesManager.GetStringPreferences(
				SharedPreferencesManager.current_date_key, "-1");
		if (!saved_date.equals(now.format3339(true))) {

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context);
			mBuilder.setSmallIcon(R.drawable.ico_actionbar);
			mBuilder.setContentTitle(" –ﬂÌ— !");
			mBuilder.setContentText("·«  ‰”Ï ﬁ—«¡… –ﬂ— «·ÌÊ„ „‰ Õ’‰ «·„”·„!!");
			// mBuilder.build();
			/* Creates an explicit intent for an Activity in your app */
			mBuilder.setAutoCancel(true);
			Intent resultIntent = new Intent(context,
					SplashScreenActivity.class);

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			stackBuilder.addParentStack(SplashScreenActivity.class);

			/* Adds the Intent that starts the Activity to the top of the stack */
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);

			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			/* notificationID allows you to update the notification later on. */
			mNotificationManager.notify(100, mBuilder.build());

		}
		// playSound(context, getAlarmUri());
	}
}