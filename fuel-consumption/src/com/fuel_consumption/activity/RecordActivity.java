package com.fuel_consumption.activity;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fuel_consumption.db.DbHelper;
import com.fuel_consumption.widget.DateTimePicker;
import com.fuel_consumption.widget.DateTimePicker.OnDateTimeSetListener;
import com.fuel_consumption.widget.RecordEditText;
import com.fuel_consumption.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

abstract class RecordActivity extends Activity {

	abstract protected void setRange();

	abstract protected void setTotalPrice();

	abstract protected void setUnitPrice();

	abstract protected void setDateTime();

	abstract protected void setIsEmpty();

	abstract protected void setIsFull();

	private static final String TAG = "RecordActivity";

	protected Button confirm, back;
	protected RecordEditText inputrange;
	protected EditText inputtotalprice, inputunitprice, disamount, date, remark;
	protected CheckBox isempty, isfull;
	protected DateTimePicker datetimepicker;
	protected String range, totalprice, unitprice, amount, datetime, sqlremark;
	protected int sqldatetime, sqlisempty, sqlisfull;
	protected float sqlrange, sqltotalprice, sqlunitprice, sqlamount;
	private Calendar mCalendar;
		

	private void initUI() {
		confirm = (Button) findViewById(R.id.confirm);
		back = (Button) findViewById(R.id.back);
		inputrange = (RecordEditText) findViewById(R.id.inputrange);
		inputtotalprice = (EditText) findViewById(R.id.inputtotalprice);
		inputunitprice = (EditText) findViewById(R.id.inputunitprice);
		disamount = (EditText) findViewById(R.id.disamount);
		date = (EditText) findViewById(R.id.date);
		isempty = (CheckBox) findViewById(R.id.isempty);
		isfull = (CheckBox) findViewById(R.id.isfull);
		remark = (EditText) findViewById(R.id.remark);
		datetimepicker = (DateTimePicker) findViewById(R.id.datetimepicker);
		mCalendar = Calendar.getInstance();
		adjustdatetimepickererTextSize();
		setRange();
		setTotalPrice();
		setUnitPrice();
		setDateTime();
		setIsEmpty();
		setIsFull();
	}

	private void initListener() {
		
		
		
		inputtotalprice.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				autoinput();
			}
		});

		inputunitprice.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				autoinput();
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		date.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!datetimepicker.isshow()){
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					datetimepicker.setVisibility(View.VISIBLE);
					datetimepicker.setIsShow(true);
					//初始化datetimepicker的时间
					datetimepicker.setDateTime(datetime);
					Animation datetiminAnimation = AnimationUtils.loadAnimation(
							RecordActivity.this, R.anim.datetimein);
					datetimepicker.startAnimation(datetiminAnimation);			
				}else{
					datetimepicker.setVisibility(View.GONE);
					datetimepicker.setIsShow(false);
				}
				
			}
		});

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContentValues value = checkinput();
				// DbHelper helper = new DbHelper(getApplicationContext());
				// helper.insert(value);
				// finish();
			}
		});

		datetimepicker.setOnDateTimeSetListener(new OnDateTimeSetListener() {

			@Override
			public void onDateTimeSet(int year, int monthOfYear,
					int dayOfMonth, int hour, int minute) {
				// TODO Auto-generated method stub
				mCalendar.set(year, monthOfYear-1, dayOfMonth, hour, minute);
				SimpleDateFormat df=new SimpleDateFormat("yyy-MM-dd HH:mm");
				Date daytime = mCalendar.getTime();
				sqldatetime = (int)(daytime.getTime()/1000);
				datetime = df.format(daytime);
				date.setText(datetime);
				date.clearFocus();
			}

			@Override
			public void onDateTimeChange(int year, int monthOfYear,
					int dayOfMonth, int hour, int minute) {
				// TODO Auto-generated method stub
				mCalendar.set(year, monthOfYear-1, dayOfMonth, hour, minute);
				SimpleDateFormat df=new SimpleDateFormat("yyy-MM-dd HH:mm");
				datetime = df.format(mCalendar.getTime());
				date.setText(datetime);
			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record_activity);
		initUI();
		initListener();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}
	
	private void adjustdatetimepickererTextSize(){
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		datetimepicker.setFontSize(width);
		remark.setHeight(height/4);
	}

	private void autoinput() {
		RecordActivity.this.amount = null;
		disamount.setText(RecordActivity.this.amount);
		totalprice = inputtotalprice.getText().toString();
		unitprice = inputunitprice.getText().toString();
		if (totalprice.length() != 0 && unitprice.length() != 0) {
			float ftotalprice = Float.parseFloat(totalprice);
			float funitprice = Float.parseFloat(unitprice);
			if (funitprice != 0) {
				float amount = ftotalprice / funitprice;
				RecordActivity.this.amount = String.format("%.2f", amount);
				disamount.setText(RecordActivity.this.amount);
			}
		}
	}

	private ContentValues checkinput() {

		return null;
	}

}
