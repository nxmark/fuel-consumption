package com.nxmark.fuel_consumption.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "local_user_data";
	public static final int DATABASE_VERSION = 1;
	
	private static final String TABLE_USER_RECORD_CREATE = 
			"CREATE TABLE user_record("
			+ "time integer primary key,"
			+ "totalprice text,"
			+ "unitprice text,"
			+ "amount text,"
			+ "isempty integer,"
			+ "isfull integer)";
	

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TABLE_USER_RECORD_CREATE);		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
