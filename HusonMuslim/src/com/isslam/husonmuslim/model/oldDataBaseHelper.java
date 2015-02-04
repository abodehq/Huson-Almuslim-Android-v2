package com.isslam.husonmuslim.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.isslam.husonmuslim.utils.GlobalConfig;

public class oldDataBaseHelper extends SQLiteOpenHelper {

	public interface DataBaseHelperInterface {
		public void onRequestCompleted();
	}

	private DataBaseHelperInterface mCallback;

	private static String DB_PATH = "/data/data/" + GlobalConfig._context
			+ "/databases/";
	private static String DB_NAME = "book_db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private int searccTopResult = 30;
	private static final int DATABASE_VERSION = 1;

	public oldDataBaseHelper(Context context, DataBaseHelperInterface callback) {

		super(context, DB_NAME, null, DATABASE_VERSION);
		this.myContext = context;
		mCallback = callback;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (deleteDataBase())
			InitDB();
	}

	public void InitDB()// Call when the application Run
	{
		try {

			boolean result = createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {

			openDataBase();

		} catch (SQLException sqle) {

			throw sqle;
		}
		mCallback.onRequestCompleted();
	}

	public boolean createDataBase() throws IOException {
		myDataBase = null;
		boolean dbExist = checkDataBase();// check if we DB SQLlite Exist or not

		if (dbExist) {
			return false;
		} else {
			try {
				myDataBase = this.getReadableDatabase();
				myDataBase.close();
				copyDataBase();// //Copy the External DB to the application

				return true;
			} catch (IOException e) {
				return false;

			}
		}

	}

	public boolean checkDataBase() {
		try {
			String myPath = DB_PATH + DB_NAME;
			File dbFile = new File(myPath);
			return dbFile.exists();
		} catch (SQLiteException e) {
		}
		return false;
	}

	public boolean deleteDataBase()// Delere on Upgrade
	{
		try {
			String myPath = DB_PATH + DB_NAME;
			File dbFile = new File(myPath);
			if (dbFile.exists()) {
				dbFile.delete();
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
		openDataBase();
		creatVirtualDB();
	}

	public void openDataBase() throws SQLException { // Open the DB

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		// myDataBase = this.getReadableDatabase();
		if (myDataBase != null) {
			if (myDataBase.isOpen()) {
				myDataBase.close();
			}
		}

		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}

	public void creatVirtualDB() {
		myDataBase
				.execSQL("CREATE VIRTUAL TABLE  vcontents USING fts3(_id INTEGER PRIMARY KEY, content TEXT,chapter_id TEXT ,semi_chapter_id INTEGER ,vname TEXT );");
		copyVirtual();
	}

	public void copyVirtual() {

		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("insert into vcontents select contents._id,contents.content ,contents.chapter_id,contents.semi_chapter_id,semi_chapters.name from contents,semi_chapters WHERE contents.semi_chapter_id=semi_chapters._id ;");

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();
		SQLiteDatabase db = this.getReadableDatabase();
		db.close();
		super.close();

	}

	public ArrayList<HashMap<String, String>> get_all_chapters()// Get all Books
																// Chapters
	{
		ArrayList<HashMap<String, String>> contentList = new ArrayList<HashMap<String, String>>();

		// Select All Query
		String selectQuery = "SELECT * FROM chapters";
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("_id", cursor.getString(0));
				map.put("name", cursor.getString(1));
				// map.put("img", cursor.getString(2));
				map.put("order_id", cursor.getString(2));

				contentList.add(map);
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();
		db.close();

		// returning lables
		return contentList;
	}

	public ArrayList<HashMap<String, String>> get_content_by_id(
			String content_id)// /Get SemiChapter Content
	{
		ArrayList<HashMap<String, String>> contentList = new ArrayList<HashMap<String, String>>();

		// Select All Query
		String selectQuery = "SELECT contents._id,contents.content,contents.chapter_id,contents.semi_chapter_id,chapters.name,semi_chapters.name FROM contents,chapters,semi_chapters where contents._id = "
				+ content_id
				+ " AND chapters._id = contents.chapter_id AND semi_chapters.chapter_id = contents.chapter_id AND semi_chapters._id = contents.semi_chapter_id";
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("_id", cursor.getString(0));
				map.put("content", cursor.getString(1));
				map.put("chapter_id", cursor.getString(2));
				map.put("semi_chapter_id", cursor.getString(3));
				map.put("chapter_name", cursor.getString(4));
				map.put("semi_chapter_name", cursor.getString(5));
				contentList.add(map);
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();
		db.close();

		// returning lables
		return contentList;
	}

	public ArrayList<HashMap<String, String>> get_semi_chapter_by_id(
			String chapter_id)// get allsemichapters
	{
		ArrayList<HashMap<String, String>> contentList = new ArrayList<HashMap<String, String>>();

		// Select All Query
		String selectQuery = "SELECT semi_chapters._id,semi_chapters.name,semi_chapters.chapter_id,chapters.name FROM semi_chapters,chapters where chapter_id ="
				+ chapter_id + " AND semi_chapters.chapter_id = chapters._id";
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("_id", cursor.getString(0));
				map.put("name", cursor.getString(1));
				map.put("chapter_id", cursor.getString(2));
				map.put("chapter_name", cursor.getString(3));
				contentList.add(map);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return contentList;
	}

	public ArrayList<HashMap<String, String>> get_semi_chapter_content(
			String chapter_id, String semi_chapter_id)// get Each semichapter
														// with content-id
	{
		ArrayList<HashMap<String, String>> contentList = new ArrayList<HashMap<String, String>>();

		// Select All Query
		String selectQuery = "SELECT semi_chapters._id,semi_chapters.name,semi_chapters.chapter_id,contents._id FROM semi_chapters,contents where semi_chapters._id = "
				+ semi_chapter_id
				+ " AND  semi_chapters.chapter_id ="
				+ chapter_id
				+ " AND semi_chapters.chapter_id = contents.chapter_id AND semi_chapters._id = contents.semi_chapter_id";
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {

				HashMap<String, String> map = new HashMap<String, String>();

				map.put("_id", cursor.getString(0));
				map.put("name", cursor.getString(1));
				map.put("chapter_id", cursor.getString(2));
				map.put("hadith_id", cursor.getString(3));
				contentList.add(map);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return contentList;
	}

	public ArrayList<HashMap<String, String>> getSearch(String value) {

		ArrayList<HashMap<String, String>> contentList = new ArrayList<HashMap<String, String>>();

		// Select All Query
		String selectQuery = "SELECT  vcontents._id,vcontents.content,vcontents.chapter_id,vcontents.semi_chapter_id,chapters.name,semi_chapters.name FROM vcontents,chapters,semi_chapters WHERE vcontents MATCH '*"
				+ value
				+ "*' AND chapters._id = vcontents.chapter_id AND semi_chapters.chapter_id = vcontents.chapter_id AND semi_chapters._id = vcontents.semi_chapter_id  LIMIT "
				+ searccTopResult;

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				HashMap<String, String> map = new HashMap<String, String>();

				map.put("_id", cursor.getString(0));
				map.put("content", cursor.getString(1));
				map.put("chapter_id", cursor.getString(2));
				map.put("semi_chapter_id", cursor.getString(3));
				map.put("chaptername", cursor.getString(4));
				map.put("semichaptername", cursor.getString(5));

				contentList.add(map);
			} while (cursor.moveToNext());
		}
		// closing connection
		cursor.close();
		db.close();
		return contentList;
	}

	public ArrayList<HashMap<String, String>> getRandomContent() {

		ArrayList<HashMap<String, String>> contentList = new ArrayList<HashMap<String, String>>();

		// Select All Query
		String selectQuery = "SELECT  vcontents._id,vcontents.content,vcontents.chapter_id,vcontents.semi_chapter_id,chapters.name,semi_chapters.name FROM vcontents,chapters,semi_chapters  WHERE chapters._id = vcontents.chapter_id AND semi_chapters.chapter_id = vcontents.chapter_id AND semi_chapters._id = vcontents.semi_chapter_id  ORDER BY RANDOM() LIMIT 1 ";

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				HashMap<String, String> map = new HashMap<String, String>();

				map.put("_id", cursor.getString(0));
				map.put("content", cursor.getString(1));
				map.put("chapter_id", cursor.getString(2));
				map.put("semi_chapter_id", cursor.getString(3));
				map.put("chapter_name", cursor.getString(4));
				map.put("semi_chapter_name", cursor.getString(5));

				contentList.add(map);
			} while (cursor.moveToNext());
		}
		// closing connection
		cursor.close();
		db.close();
		return contentList;
	}

	public ArrayList<HashMap<String, String>> get_semi_chapters_fav()// get
																		// allsemichapters
	{
		ArrayList<HashMap<String, String>> contentList = new ArrayList<HashMap<String, String>>();

		// Select All Query
		String selectQuery = "SELECT semi_chapters._id,semi_chapters.name,semi_chapters.chapter_id,favourite._id,content,contents._id FROM favourite,semi_chapters,contents where favourite.content_id = contents._id AND semi_chapters._id = contents.semi_chapter_id ";
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("_id", cursor.getString(0));
				map.put("name", cursor.getString(1));
				map.put("chapter_id", cursor.getString(2));
				map.put("chapter_name", "");
				map.put("favourite_id", cursor.getString(3));
				map.put("content", cursor.getString(4));
				map.put("content_id", cursor.getString(5));
				contentList.add(map);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return contentList;
	}

	public boolean get_fav_count(String content_id) {

		boolean result = false;

		// Select All Query
		String selectQuery = "SELECT favourite._id FROM favourite where favourite.content_id = "
				+ content_id;
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				result = true;
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return result;

	}

	public boolean get_fav_is_empty() {
		boolean result = false;

		// Select All Query
		String selectQuery = "SELECT favourite._id FROM favourite  ";
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				result = true;
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return !result;
	}

	public ArrayList<HashMap<String, String>> delete_fav(String fav_id)// get
																		// allsemichapters
	{
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete("favourite", "_id=" + fav_id, null);
		db.close();
		return get_semi_chapters_fav();
	}

	public void delete_all_fav()// get
	// allsemichapters
	{
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete("favourite", null, null);
		db.close();

	}

	public ArrayList<HashMap<String, String>> delete_fav_by_content_id(
			String content_id)// get allsemichapters
	{
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete("favourite", "content_id=" + content_id, null);
		db.close();
		return get_semi_chapters_fav();
	}

	public void insert_fav(String content_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("content_id", content_id);

		// Inserting Row
		db.insert("favourite", null, values);

		db.close(); // Closing database connection
	}

	public void toggle_fav(String content_id) {
		if (get_fav_count(content_id)) {
			delete_fav_by_content_id(content_id);
		} else {
			insert_fav(content_id);
		}
	}

}