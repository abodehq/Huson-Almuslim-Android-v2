package com.isslam.husonmuslim.model;

public class Chapter {

	private int surahIndex;

	private int ayahFrom;

	private int ayahTo;

	private int pagesId;

	private String surah;

	private int chapterIndex;
	private String chapterName;
	private int orderId;

	public int getChapterId() {
		return chapterIndex;
	}

	public void setChapterId(int chapterIndex) {
		this.chapterIndex = chapterIndex;
	}

	public String getchapterName() {
		return chapterName;
	}

	public void setchapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public int getChapterOrder() {
		return orderId;
	}

	public void setChapterOrder(int orderId) {
		this.orderId = orderId;
	}

}