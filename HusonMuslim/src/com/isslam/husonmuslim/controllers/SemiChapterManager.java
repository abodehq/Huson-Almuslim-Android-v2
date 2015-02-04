package com.isslam.husonmuslim.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.isslam.husonmuslim.model.BookSemiChapter;
import com.isslam.husonmuslim.model.DataBaseHelper;
import com.isslam.husonmuslim.model.SemiChapter;
import com.isslam.husonmuslim.utils.GlobalConfig;

public class SemiChapterManager {

	private static SemiChapterManager instance = null;
	private BookSemiChapter BookSemiChapter = null;
	private BookSemiChapter dummyBookSemiChapter = null;
	private int selectedSemiChapterId = 1;
	private int selectedContentId = 1;

	public static SemiChapterManager getInstance() {
		if (instance == null) {
			instance = new SemiChapterManager();
		}
		return instance;
	}

	public void setBookSemiChapter(String chapterId) {
		ArrayList<HashMap<String, String>> chaptersList = new ArrayList<HashMap<String, String>>();
		DataBaseHelper myDbHelper = GlobalConfig.GetmyDbHelper();
		chaptersList = myDbHelper.get_semi_chapter_by_id(chapterId);
		List<SemiChapter> semichapters = new ArrayList<SemiChapter>();
		for (int i = 0; i < chaptersList.size(); i++) {

			SemiChapter chapter = new SemiChapter();

			chapter.setChapterId(Integer.parseInt(chaptersList.get(i).get(
					"chapter_id")));
			chapter.setchapterName(chaptersList.get(i).get("chapter_name"));
			chapter.setSemiChapterId(Integer.parseInt(chaptersList.get(i).get(
					"_id")));
			chapter.setSemichapterName(chaptersList.get(i).get("name"));
			// chapter.setChapterOrder(Integer.parseInt(chaptersList.get(i).get(
			// "order_id")));
			semichapters.add(chapter);
		}

		BookSemiChapter.setSemiChapters(semichapters);

	}

	public void setDummyBookSemiChapter(String chapterId) {
		ArrayList<HashMap<String, String>> chaptersList = new ArrayList<HashMap<String, String>>();
		DataBaseHelper myDbHelper = GlobalConfig.GetmyDbHelper();
		chaptersList = myDbHelper.get_semi_chapter_by_id(chapterId);
		List<SemiChapter> semichapters = new ArrayList<SemiChapter>();
		for (int i = 0; i < chaptersList.size(); i++) {

			SemiChapter chapter = new SemiChapter();

			chapter.setChapterId(Integer.parseInt(chaptersList.get(i).get(
					"chapter_id")));
			chapter.setchapterName(chaptersList.get(i).get("chapter_name"));
			chapter.setSemiChapterId(Integer.parseInt(chaptersList.get(i).get(
					"_id")));
			chapter.setSemichapterName(chaptersList.get(i).get("name"));
			// chapter.setChapterOrder(Integer.parseInt(chaptersList.get(i).get(
			// "order_id")));
			semichapters.add(chapter);
		}

		dummyBookSemiChapter.setSemiChapters(semichapters);

	}

	public List<SemiChapter> getDummySemiChapters() {

		return dummyBookSemiChapter.getSemiChapters();
	}

	public List<SemiChapter> getSemiChapters() {

		return BookSemiChapter.getSemiChapters();
	}

	// Constructor
	public SemiChapterManager() {
		BookSemiChapter = new BookSemiChapter();
		dummyBookSemiChapter = new BookSemiChapter();
	}

	public void SetBookSemiChapter(BookSemiChapter _BookSemiChapter) {
		BookSemiChapter = _BookSemiChapter;
		dummyBookSemiChapter = _BookSemiChapter;

	}

	// ///user Sura options
	public int getselectedSemiChapterId() {
		return selectedSemiChapterId;
	}

	public int getSelectedContentId() {
		return selectedContentId;
	}

	public void setSelectedContentId(int contentId) {

		selectedContentId = contentId;

	}

	public void SetNextSemiChapter() {
		int _position = getCurrentSelectedSemiChapterPosition();
		_position = _position + 1;
		if (_position == BookSemiChapter.getSemiChapters().size())
			_position = 0;
		SemiChapter _sura = BookSemiChapter.getSemiChapters().get(_position);

		selectedSemiChapterId = _sura.getSemiChapterId();

	}

	public void SetPrevSemiChapter() {
		int _position = getCurrentSelectedSemiChapterPosition();
		_position = _position - 1;
		if (_position < 0)
			_position = BookSemiChapter.getSemiChapters().size() - 1;
		SemiChapter _sura = BookSemiChapter.getSemiChapters().get(_position);

		selectedSemiChapterId = _sura.getSemiChapterId();

	}

	public void setselectedSemiChapterId(int suraId) {
		GlobalConfig.Log("semiManager : setselectedSemi", "" + suraId);
		selectedSemiChapterId = suraId;

	}

	public int getCurrentSelectedSemiChapterPosition() {
		if (BookSemiChapter.getSemiChapters().size() == 0) {
			BookChaptersManager bookChaptersManager = BookChaptersManager
					.getInstance();
			setBookSemiChapter(String.valueOf(bookChaptersManager
					.getselectedChapterId()));
		}
		SemiChapter _sura = null;

		for (int i = 0; i < BookSemiChapter.getSemiChapters().size(); i++) {
			_sura = BookSemiChapter.getSemiChapters().get(i);
			if (_sura.getSemiChapterId() == selectedSemiChapterId) {
				return i;
			}
		}
		return 0;
	}

	public void SetSemiChaptersList(Context context) {

		BookChaptersManager bookChaptersManager = BookChaptersManager
				.getInstance();
		SemiChapterManager semiChapterManager = SemiChapterManager
				.getInstance();

		Log.e("getselectedChapterId()",
				bookChaptersManager.getselectedChapterId() + "");
		Log.e("getselectedSemiChapterId",
				semiChapterManager.getselectedSemiChapterId() + "");
		GlobalConfig.semiChaptersList = GlobalConfig.GetmyDbHelper()
				.get_semi_chapter_content(
						bookChaptersManager.getselectedChapterId() + "",
						semiChapterManager.getselectedSemiChapterId() + "");
		setBookSemiChapter(String.valueOf(bookChaptersManager
				.getselectedChapterId()));

		SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
				.getInstance(context);
		sharedPreferencesManager.savePreferences(
				SharedPreferencesManager.chapter_sel_key,
				bookChaptersManager.getselectedChapterId());
		sharedPreferencesManager.savePreferences(
				SharedPreferencesManager.semi_chapter_sel_key,
				semiChapterManager.getselectedSemiChapterId());
	}

	public int getPageCurrentItem() {
		int position = 0;
		int Count = GlobalConfig.getSemiChapterList(null).size();
		for (int j = 0; j < Count; j++) {
			HashMap<String, String> sura = GlobalConfig
					.getSemiChapterList(null).get(j);

			if (selectedContentId == Integer.parseInt(sura.get("hadith_id"))) {
				position = j;
				break;
			}
		}
		return position;
	}

	public int getCurrentSelectedChaptersPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

}
