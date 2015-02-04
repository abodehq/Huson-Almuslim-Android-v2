package com.isslam.husonmuslim.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.isslam.husonmuslim.R;
import com.isslam.husonmuslim.controllers.BookChaptersManager;
import com.isslam.husonmuslim.controllers.SemiChapterManager;
import com.isslam.husonmuslim.model.DataBaseHelper;

public class GlobalConfig extends Activity {
	public static String _context = "";
	public static String local = "ar";
	public static int fontSize = 18;
	public static int fontColor = -16777216;
	public static int bgColor = -1;
	public static boolean showBg = false;
	// default values
	public static int defaultFontSize = 18;
	public static int defaultFColor = -16777216;
	public static int defaultBgColor = -1;
	public static boolean isSearchActive = false;
	// ////
	public static int titleFontSize = 18;
	public static String fontPath = "fonts/arabic.ttf";
	public static String storageDomainRoot = "http://s3.amazonaws.com";
	public static String shareAppPath = "https://play.google.com/store/apps/details?id=eqratech.fatiha";
	public static String htmlContentStructure = "";
	public static int screenWidth = 750;// 570;// 240;// 750;
	public static int screenHeight = 1177;// 816;// 320;// 1177;
	public static ArrayList<HashMap<String, String>> semiChaptersList = null;
	public static ArrayList<HashMap<String, String>> widgetSemiChaptersList;

	public static ArrayList<HashMap<String, String>> getSemiChapterList(
			Context context) {

		if (semiChaptersList==null && context != null) {
			BookChaptersManager bookChaptersManager = BookChaptersManager
					.getInstance();
			bookChaptersManager.setselectedChapterId(1);
			SemiChapterManager semiChapterManager = SemiChapterManager
					.getInstance();
			semiChapterManager.setselectedSemiChapterId(1);
			semiChapterManager.SetSemiChaptersList(context
					.getApplicationContext());
			semiChapterManager.setSelectedContentId(1);

		}
		return semiChaptersList;
	}

	// log file..
	public static Boolean showLog = true;

	public static void Log(String tag, String msg) {
		if (showLog)
			try {
				android.util.Log.e(tag, msg);
			} catch (Exception e) {

			}
	}

	private static DataBaseHelper myDbHelper = null;

	public static DataBaseHelper GetmyDbHelper() {
		try {
			if (myDbHelper == null) {
				myDbHelper = new DataBaseHelper(null);
				myDbHelper.InitDB();
			}
		} catch (Exception e) {

		}
		return myDbHelper;
	}

	public static void SetmyDbHelper(DataBaseHelper _myDbHelper) {
		myDbHelper = _myDbHelper;
	}

	public static void ShowLongToast(Context context, String msg) {

		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_LONG);

		LayoutInflater inflater1 = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater1.inflate(R.layout.ly_toast, null);
		TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
		textMessage.setText(msg);
		toast.setGravity(Gravity.TOP | Gravity.LEFT | Gravity.FILL_HORIZONTAL,
				0, ((Activity) context).getActionBar().getHeight());

		toast.setView(view);
		toast.show();
	}

	public static void ShowToast(Context context, String msg) {
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);

		LayoutInflater inflater1 = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater1.inflate(R.layout.ly_toast, null);
		TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
		textMessage.setText(msg);
		toast.setGravity(Gravity.TOP | Gravity.LEFT | Gravity.FILL_HORIZONTAL,
				0, ((Activity) context).getActionBar().getHeight());

		toast.setView(view);
		toast.show();

	}
}