package com.uestc.Indoorguider.history;

import java.io.File;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class MyDBHelper {
	/*
	 * HISTORY_TABLE
	 */	
	protected final static String HISTORY_TABLE = "history";
	public final static String FIELD_historyid = "historyid";
	public final static String FIELD_mapid = "mapid";
	public final static String FIELD_date = "date";
	public final static String FIELD_startTime = "starttime";
	public final static String FIELD_endTime = "endtime";		
	/*
	 * PATHPOINTS_TABLE
	 */
	private final static String PATHPOINTS_TABLE = "pathpoints";
	public final static String FIELD_xpoint = "xpoint";
	public final static String FIELD_ypoint = "ypoint";
	public final static String FIELD_zpoint = "zpoint";
	public final static String FIELD_pointid = "pointid";
	public final static String FIELD_time = "time";
	
	private final static String DATABASE_NAME = "history2.db3";

	protected static final String HISTORY_TABLE_DDL ="CREATE TABLE IF NOT EXISTS " + HISTORY_TABLE + "("
			+  FIELD_historyid + " INTEGER primary key autoincrement, "
			+" "+FIELD_mapid+" INTEGER, "
			+" " + FIELD_date + " CHAR(20), "
			+" " + FIELD_startTime + " CHAR(20), "
			+" " + FIELD_endTime  + " CHAR(20));";
			
	private static final String HISTORY_POINTS_TABLE_DDL ="CREATE TABLE IF NOT EXISTS " + PATHPOINTS_TABLE + " ("
			+ FIELD_pointid + " INTEGER primary key autoincrement, "
			+" "+ FIELD_historyid + " INTEGER NOT NULL, "
			+ " " +  FIELD_xpoint + " INTEGER, "
			+ " " + FIELD_ypoint + " INTEGER, "
			+ " " + FIELD_zpoint + " INTEGER, "
			+" " + FIELD_time + " CHAR(20));";
	
	//public static String SD_PATH = "/sdcard/";
	private final static String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static String HISTORY_PATH = SD_PATH + File.separator + "indorguider" +File.separator;
	private SQLiteDatabase db;
	
	String TAG = "DBHELPER";
	//throws SQLException
	//open database
	public MyDBHelper open()  {	
		if (db == null) {
			Log.v(TAG, "DB == null");
			String sqliteFilePath =HISTORY_PATH+ DATABASE_NAME;
			//String sqliteFilePath = DATABASE_NAME;
			Log.v(TAG, HISTORY_PATH);
			File dir = new File(HISTORY_PATH);
		     if(!dir.exists()) {	  
		    	boolean re =  dir.mkdirs();
		    	 Log.v(TAG, "file not exist"+" "+re);
		     }
		     else
		    	 Log.v(TAG, "file  exist");
			try{
			db = SQLiteDatabase.openOrCreateDatabase(sqliteFilePath, null);
			} catch (SQLException e) {				
				Log.v(TAG, e.getMessage());
			}
			}	
		Log.v(TAG, "open dbhelper");
		db.execSQL(HISTORY_TABLE_DDL);
		db.execSQL(HISTORY_POINTS_TABLE_DDL);
		Log.v(TAG, "init db over");
		return this;
	}
	
	public long insertPath(HistoryItem item) {		
		long historyID = 0;
		List<Site> path = item.getPath();
		db.beginTransaction();
		try {			
			ContentValues cv = new ContentValues();
			cv.put(FIELD_mapid, item.getMapId());
			cv.put(FIELD_date, item.getDate());
			cv.put(FIELD_startTime, item.getStartTime());
			cv.put(FIELD_endTime, item.getEndTime());
			historyID = db.insert(HISTORY_TABLE, null, cv);
			for(Site site : path) {
				cv = new ContentValues();
				cv.put(FIELD_historyid, historyID);
				cv.put(FIELD_xpoint, site.getX());
				cv.put(FIELD_ypoint, site.getY());
				cv.put(FIELD_zpoint, site.getZ());
				cv.put(FIELD_time, site.getTime());
				db.insert(PATHPOINTS_TABLE, null, cv);
			}	
		db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return historyID;
	}
	
	//delete 
	public boolean deleteHistory(long rowId) {
		int rowsAffected = 0;
		db.beginTransaction();
		try {
			rowsAffected = db.delete(PATHPOINTS_TABLE, FIELD_historyid + "=" + rowId, null);
			rowsAffected = db.delete(HISTORY_TABLE, FIELD_historyid + "=" + rowId, null);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return rowsAffected > 0;
	}
	
	//delete all 
	public boolean delAllHistory() {
		int rowsAffected = 0;
		db.beginTransaction();
		try {
			rowsAffected = db.delete(PATHPOINTS_TABLE, FIELD_historyid + ">=" + 0, null);
			rowsAffected = db.delete(HISTORY_TABLE, FIELD_historyid + ">=" + 0, null);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return rowsAffected > 0;
	}
	
	//get all history
	public Cursor getAllHistory() {
		return db.query(HISTORY_TABLE, new String[] {
				FIELD_historyid,
				FIELD_mapid, 
				FIELD_date,
				FIELD_startTime,
				FIELD_endTime},
				null, 
				null, 
				null, 
				null, 
				FIELD_historyid);
	}
	
	//返回特定的history
	public Cursor getHistory(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, HISTORY_TABLE, new String[] {
				FIELD_historyid,
				FIELD_mapid,
				FIELD_date,
				FIELD_startTime,
				FIELD_endTime
						}, 
						FIELD_historyid + "=" + rowId, 
						null,
						null, 
						null, 
						null, 
						null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	//返回特定path
	public Cursor getPathPoints(long rowId) throws SQLException {
		Cursor mCursor = db.query(false, PATHPOINTS_TABLE, new String[] {
						FIELD_historyid, 
						FIELD_xpoint,
						FIELD_ypoint,
						FIELD_zpoint,
						FIELD_time,
						}, 
						FIELD_historyid + "=" + rowId, 
						null,
						null, 
						null, 
						null, 
						null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	//get all points
	public Cursor getAllPathPoints() {
		
		Cursor mCursor = db.query(false, PATHPOINTS_TABLE, new String[] {
				FIELD_historyid, 
				FIELD_xpoint,
				FIELD_ypoint,
				FIELD_zpoint,
				FIELD_time,
				}, 
				FIELD_historyid + ">=" + 0, 
				null,
				null, 
				null, 
				null, 
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		
	}
		
	//check table
	public boolean checkIfDBHasHistory() {
		
		boolean result = false;
		Cursor mCursor = db.rawQuery("select count("+FIELD_historyid+")"+" from "+HISTORY_TABLE, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			result = mCursor.getFloat(0) > 0;
			mCursor.close();
		}		
		return result;
	}
	
	//close database
	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
}
