package com.fuel_consumption.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	public static final String TAG = "DbHelper";
	
	public static final String DATABASE_NAME = "local_user_data";
	public static final String TABLE_NAME = "user_record";
	public static final int DATABASE_VERSION = 1;
	
	
	private static final String TABLE_USER_RECORD_CREATE = 
			"CREATE TABLE user_record("
			+ "_id integer primary key,"
			+ "range integer,"
			+ "totalprice integer,"
			+ "unitprice integer,"
			+ "amount integer,"
			+ "isempty integer,"
			+ "isfull integer,"
			+ "remark text)";
	

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("db create");
		db.execSQL(TABLE_USER_RECORD_CREATE);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public void insert(ContentValues value){
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TABLE_NAME, null, value);
		db.close(); 		
	}
	
	public boolean hasItem(int id){
		Cursor c = query(id);
		boolean r = c.moveToFirst();
		c.close();
		return r;
	}
	
	public Cursor query(){
		SQLiteDatabase db = getWritableDatabase();
		Cursor r = db.query(TABLE_NAME, null, null, null, null, null, "_id desc", null);
		return r;	
	}
	
	public Cursor query(int id){
		String[] temp = {Integer.toString(id)};
		SQLiteDatabase db = getWritableDatabase();
		Cursor r = db.query(TABLE_NAME, null, "_id = ?", temp, null, null, "_id desc", null);
		return r;
	}
	
	
	public Cursor queryPrev(int id ,int deleteid){
		String[] temp = {Integer.toString(id),Integer.toString(deleteid)};
		SQLiteDatabase db = getWritableDatabase();
		Cursor r = db.query(TABLE_NAME, null, "_id < ? and _id != ?", temp, null, null, "_id desc", "1");
		return r;
	}
	
	public Cursor queryFol(int id, int deleteid){
		String[] temp = {Integer.toString(id),Integer.toString(deleteid)};
		SQLiteDatabase db = getWritableDatabase();
		Cursor r = db.query(TABLE_NAME, null, "_id > ? and _id != ?", temp, null, null, "_id", "1");
		return r;
	}
	
	public void delete(int id){
		String[] temp = {Integer.toString(id)};
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, "_id = ?", temp);	
	}
}
