package com.isslam.husonmuslim.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.isslam.husonmuslim.utils.GlobalConfig;

public class SharedPreferencesManager {

	private static SharedPreferencesManager instance = null;
	private Context context;
	public static String _first_run = "_first_run";
	public static String bg_color_key = "bg_color";
	public static String show_bg = "show_bg";
	public static String font_color_key = "font_color";
	public static String font_size_key = "font_size";
	public static String show_nafigation_key = "show_navigation";
	public static String show_menu_key = "show_menu";
	public static String show_menu_always_key = "show_menu_always";

	public static String chapter_sel_key = "chapter_sel_key";
	public static String semi_chapter_sel_key = "semi_chapter_sel_key";
	public static String current_date_key = "current_date";
	public static String current_haidth_id_key = "current_haidth_id";

	public static SharedPreferencesManager getInstance(Context _context) {
		if (instance == null) {
			instance = new SharedPreferencesManager();
		}
		instance.setContext(_context);
		return instance;
	}

	private void setContext(Context _context) {
		context = _context;
	}

	public void SetupPreferences() {
		GlobalConfig.bgColor = GetIntegerPreferences(bg_color_key, -1);
		GlobalConfig.fontColor = GetIntegerPreferences(font_color_key,
				-16777216);
		GlobalConfig.fontSize = GetIntegerPreferences(font_size_key, 18);
		GlobalConfig.showBg=getBooleanPreferences(show_bg, true);
		BookChaptersManager bookChaptersManager = BookChaptersManager
				.getInstance();

		bookChaptersManager.setselectedChapterId(GetIntegerPreferences(
				chapter_sel_key, 1));
		SemiChapterManager semiChapterManager = SemiChapterManager
				.getInstance();
		semiChapterManager.setselectedSemiChapterId(GetIntegerPreferences(
				semi_chapter_sel_key, 1));

	}

	public Boolean getBooleanPreferences(String key, Boolean defaultValue) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			return sharedPreferences.getBoolean(key, defaultValue);
		}
		return false;
	}

	public String GetStringPreferences(String key, String defaultValue) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			return sharedPreferences.getString(key, defaultValue);
		}
		return "";
	}

	public int GetIntegerPreferences(String key, int defaultValue) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			return sharedPreferences.getInt(key, defaultValue);
		}
		return -1;
	}

	public void savePreferences(String key, String value) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			editor.commit();
		}

	}

	public void savePreferences(String key, boolean value) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			Editor editor = sharedPreferences.edit();
			editor.putBoolean(key, value);
			editor.commit();
		}

	}

	public void savePreferences(String key, int value) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			Editor editor = sharedPreferences.edit();
			editor.putInt(key, value);
			editor.commit();
		}

	}

}
