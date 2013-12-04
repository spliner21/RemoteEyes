package com.example.remoteeyes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	static final String dbName="remoteEyes";
	static final String configTable="config";
	static final String colID="ID";
	static final String colName="configKey";
	static final String colValue="value";
	
	static final String msgTable="Messages";
	static final String colMsgID="ID";
	static final String colMsgRefID="refID";
	static final String colMsgAnswerPath="answerPath";
	static final String colMsgDate="date";
	static final String colMsgFile1="file1path";
	static final String colMsgFile2="file2path";
	static final String colMsgGps="gpsdata";
	
	static final String viewEmps="ViewEmps";
	
	public DBHelper(Context context) {
		super(context, dbName, null,1); 
		
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor c=db.query(msgTable, new String[]{colMsgID,colMsgRefID,colMsgDate,colMsgFile1,colMsgFile2,colMsgGps},
				null, null, null, null, null);
		if(c.getCount() > 0) {
			c.moveToFirst();
			while(!c.isAfterLast()) {
			Log.v("DBHelper",c.getString(c.getColumnIndex(colMsgID))+
					": refID="+c.getString(c.getColumnIndex(colMsgRefID))+
					", date="+c.getString(c.getColumnIndex(colMsgDate))+
					", file1="+c.getString(c.getColumnIndex(colMsgFile1))+
					", file2="+c.getString(c.getColumnIndex(colMsgFile2))+
					", gps="+c.getString(c.getColumnIndex(colMsgGps)));
				c.moveToPosition(c.getPosition()+1);
			}
			
		}
		db.close();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE "+configTable+" ("+
			colID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
			colName+ " TEXT, "+colValue+ " TEXT);");
			  
		db.execSQL("CREATE TABLE "+msgTable+" ("+
			colMsgID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
			colMsgRefID+" INTEGER, "+colMsgAnswerPath+" INTEGER, "+
			colMsgDate+" TEXT, "+colMsgFile1+" TEXT, " +
			colMsgFile2+" TEXT, "+colMsgGps+" TEXT);");
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+configTable);
		db.execSQL("DROP TABLE IF EXISTS "+msgTable);
		onCreate(db);
	}
	
	public String getConfigValue(String name) {
		String returnable = "";
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor c=db.query(configTable, new String[]{colValue+" as _val",colName},
				colName+"=?", new String[]{name}, null, null, null);
		if(c.getCount() > 0) {
			c.moveToLast();
			returnable = c.getString(c.getColumnIndex("_val"));
		}
		db.close();
		return returnable;
	}

	public void insertConfigValue(String name, String val) {
		SQLiteDatabase db=this.getReadableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(colName, name);
		cv.put(colValue, val);
		db.insert(configTable, null, cv);
		
			db.close();
	}

	public void insertMessage(String msgRefID, DataContainer dc) {
		Calendar c = Calendar.getInstance();

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String formattedDate = df.format(c.getTime());
		
		SQLiteDatabase db=this.getReadableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(colMsgRefID, Integer.parseInt(msgRefID));
		cv.put(colMsgFile1, dc.getFile1path());
		cv.put(colMsgFile2, dc.getFile2path());
		cv.put(colMsgGps, dc.getGpsdata());
		cv.put(colMsgDate, formattedDate);
		db.insert(msgTable, null, cv);
		
			db.close();
	}
}
