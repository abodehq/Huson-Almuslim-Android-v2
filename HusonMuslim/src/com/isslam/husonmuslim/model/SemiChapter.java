package com.isslam.husonmuslim.model;

public class SemiChapter {

	private int surahIndex;

	private int ayahFrom;

	private int ayahTo;

	private int pagesId;

	private String surah;

	private int semichapterIndex;
	private String semichapterName;
	private String chapterName;
	private int chapterId;

	public int getSemiChapterId() {
		return semichapterIndex;
	}

	public void setSemiChapterId(int semichapterIndex) {
		this.semichapterIndex = semichapterIndex;
	}

	public String getSemichapterName() {
		return semichapterName;
	}

	public void setSemichapterName(String semichapterName) {
		this.semichapterName = semichapterName;
	}

	public String getchapterName() {
		return chapterName;
	}

	public void setchapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

}