package com.nxmark.fuel_consumption.activity;

import com.nxmark.fuel_consumption.R;
import com.nxmark.fuel_consumption.db.DbHelper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

public class RecordActivity extends Activity {
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_activity);
		DbHelper dbhelper= new DbHelper(getApplicationContext());
		dbhelper.getWritableDatabase();
		System.out.println(dbhelper.getDatabaseName());
	}

}
