package com.isslam.husonmuslim.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.isslam.husonmuslim.model.BookChapters;
import com.isslam.husonmuslim.model.Chapter;
import com.isslam.husonmuslim.model.DataBaseHelper;
import com.isslam.husonmuslim.utils.GlobalConfig;

public class BookChaptersManager {

	private static BookChaptersManager instance = null;
	private BookChapters bookChapters = null;
	private BookChapters originbookChapters = null;
	private int selectedChapterId = 1;

	public static BookChaptersManager getInstance() {
		if (instance == null) {
			instance = new BookChaptersManager();
		}
		return instance;
	}

	public void setBookChapters() {
		ArrayList<HashMap<String, String>> chaptersList = new ArrayList<HashMap<String, String>>();
		DataBaseHelper myDbHelper = GlobalConfig.GetmyDbHelper();
		chaptersList = myDbHelper.get_all_chapters();
		List<Chapter> chapters = new ArrayList<Chapter>();
		for (int i = 0; i < chaptersList.size(); i++) {

			Chapter chapter = new Chapter();

			chapter.setChapterId(Integer.parseInt(chaptersList.get(i)
					.get("_id")));
			chapter.setchapterName(chaptersList.get(i).get("name"));
			chapter.setChapterOrder(Integer.parseInt(chaptersList.get(i).get(
					"order_id")));
			chapters.add(chapter);
		}

		bookChapters.setChapters(chapters);

	}

	public List<Chapter> getChapters() {

		return bookChapters.getChapters();
	}

	// Constructor
	public BookChaptersManager() {
		bookChapters = new BookChapters();
		originbookChapters = new BookChapters();
	}

	public void SetBookChapters(BookChapters _bookChapters) {
		bookChapters = _bookChapters;
		originbookChapters = _bookChapters;

	}

	public void SetSuras(BookChapters _bookChapters) {
		bookChapters = _bookChapters;
		originbookChapters = _bookChapters;

	}

	public void AddNewSura(Chapter sura) {
		bookChapters.getChapters().add(sura);

	}

	public void AddNewSuraAt(int index, Chapter sura) {
		bookChapters.getChapters().add(index, sura);

	}

	public void deletAllSuras() {
		bookChapters.getChapters().clear();
	}

	public List<Chapter> getSuras() {

		return bookChapters.getChapters();
	}

	public Chapter getCurrentSura() {
		Chapter _sura;
		for (int i = 0; i < bookChapters.getChapters().size(); i++) {
			_sura = bookChapters.getChapters().get(i);
			if (_sura.getChapterId() == getselectedChapterId()) {
				return _sura;
			}
		}
		return bookChapters.getChapters().get(0);
	}

	public void CombineSameSura() {
		BookChapters _surasList = new BookChapters();
		int _suraIndex = -1;
		int lastPosition = -1;
		Chapter _sura = new Chapter();
		for (int i = 0; i < bookChapters.getChapters().size(); i++) {
			_sura = bookChapters.getChapters().get(i);
			if (_sura.getChapterId() == _suraIndex) {
				Chapter __sura = _surasList.getChapters().get(lastPosition);
				// __sura.setAyahTo(_sura.getAyahTo());

			} else {

				_surasList.getChapters().add(_sura);
				_suraIndex = _sura.getChapterId();
				lastPosition++;
			}

		}
		bookChapters = _surasList;
		GlobalConfig.Log("SuraslistManager : Start", "-------------");

		for (int i = 0; i < bookChapters.getChapters().size(); i++) {
			_sura = bookChapters.getChapters().get(i);
			GlobalConfig.Log("_sura", _sura.getchapterName());
		}
		GlobalConfig.Log("originbookChapters : End", "-------------");

	}

	public Chapter getSuraById1(int suraId) {
		Chapter _sura = null;
		for (int i = 0; i < bookChapters.getChapters().size(); i++) {
			_sura = bookChapters.getChapters().get(i);

			if (_sura.getChapterId() == suraId)
				return _sura;
		}
		GlobalConfig.Log("SuraslistManager : getChapters().size()", ""
				+ bookChapters.getChapters().size() + "");
		return _sura;
	}

	public Chapter getSuraById(int suraId) {
		Log.e("suraId", suraId + "");
		Chapter _sura = null;
		// AudioInfoManager audioInfoManager = AudioInfoManager.getInstance();
		int selectedVerseId = 0;// audioInfoManager.getSelectedVerseId();
		for (int i = originbookChapters.getChapters().size() - 1; i >= 0; i--) {
			_sura = originbookChapters.getChapters().get(i);
			if (_sura.getChapterId() == suraId)
				return _sura;
		}
		Log.e("suraId", _sura.getChapterId() + "");
		return _sura;
	}

	// ///user Sura options
	public int getselectedChapterId() {
		return selectedChapterId;
	}

	public void setselectedChapterIdByPosition(int position) {
		Chapter _sura = bookChapters.getChapters().get(position);
		GlobalConfig
				.Log("SuraslistManager : sura", "" + _sura.getchapterName());
		selectedChapterId = _sura.getChapterId();
	}

	public void setselectedChapterId(int suraId) {
		GlobalConfig
				.Log("SuraslistManager : setselectedChapterId", "" + suraId);
		selectedChapterId = suraId;
	}

	public int getCurrentSelectedChapter() {
		Chapter _sura = getSuraById(getselectedChapterId());
		return _sura.getChapterId();
	}

	public int getCurrentSelectedChapterPosition() {
		Chapter _sura = null;
		for (int i = 0; i < bookChapters.getChapters().size(); i++) {
			_sura = bookChapters.getChapters().get(i);
			if (_sura.getChapterId() == getselectedChapterId())
				return i;
		}
		return 0;
	}

}
