package com.fuel_consumption.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.fuel_consumption.db.DbHelper;
import com.fuel_consumption.widget.DateTimePicker;
import com.fuel_consumption.widget.DateTimePicker.OnDateTimeSetListener;
import com.fuel_consumption.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
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
import android.widget.Toast;

abstract class RecordActivity extends Activity {

	abstract protected void setRange();
	abstract protected void setTotalPrice();
	abstract protected void setUnitPrice();
	abstract protected void setAmount();
	abstract protected void setDateTime();
	abstract protected void setIsEmpty();
	abstract protected void setIsFull();
	abstract protected void setRemark();

	private static final String TAG = "RecordActivity";
	
	protected Button confirm, back;
	protected EditText inputrange, inputtotalprice, inputunitprice, disamount,
			date, remark;
	protected CheckBox isempty, isfull;
	protected DateTimePicker datetimepicker;
	protected String range, totalprice, unitprice, amount, datetime, sqlremark;
	protected int sqldatetime, sqlisempty, sqlisfull;
	protected int sqlrange, sqltotalprice, sqlunitprice, sqlamount;
	protected int second;
	protected int deleteid; 
	protected Calendar mCalendar;
	SharedPreferences lastdata;
	SimpleDateFormat df;
	protected InputMethodManager imm;

	@SuppressLint("SimpleDateFormat")
	private void initUI() {
		confirm = (Button) findViewById(R.id.confirm);
		back = (Button) findViewById(R.id.back);
		inputrange = (EditText) findViewById(R.id.inputrange);
		inputtotalprice = (EditText) findViewById(R.id.inputtotalprice);
		inputunitprice = (EditText) findViewById(R.id.inputunitprice);
		disamount = (EditText) findViewById(R.id.disamount);
		date = (EditText) findViewById(R.id.date);
		isempty = (CheckBox) findViewById(R.id.isempty);
		isfull = (CheckBox) findViewById(R.id.isfull);
		remark = (EditText) findViewById(R.id.remark);
		datetimepicker = (DateTimePicker) findViewById(R.id.datetimepicker);
		mCalendar = Calendar.getInstance();
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		adjustitemSize();
		setRange();
		setTotalPrice();
		setUnitPrice();
		setAmount();
		setDateTime();
		setIsEmpty();
		setIsFull();
		setRemark();

	}

	private void initListener() {
		// clicklistener

		date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				datetimepicker.setVisibility(View.GONE);
				datetimepicker.setIsShow(false);
				date.clearFocus();
			}
		});

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DbHelper helper = new DbHelper(getApplicationContext());			
				ContentValues values = new ContentValues();
				boolean dataIsOK = checkinput(values);
				if (dataIsOK) {
					if (helper.hasItem(deleteid)){
						helper.delete(deleteid);
					}
					helper.insert(values);
					Toast.makeText(RecordActivity.this, "保存成功",
							Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		// textchanged listener
		TextWatcher priceWatcher = new TextWatcher() {
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
		};
		inputtotalprice.addTextChangedListener(priceWatcher);
		inputunitprice.addTextChangedListener(priceWatcher);

		// 焦点监听
		OnFocusChangeListener priceFocusListenser = new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					if (!isTotalpriceApplicable() | !isUnitpriceApplicable()) {
						return;
					}
					disamount
							.setBackgroundResource(R.drawable.bg_record_disamount_edittext_input);
				} else {
					disamount
							.setBackgroundResource(R.drawable.bg_record_disamount_edittext);
				}
			}
		};
		inputtotalprice.setOnFocusChangeListener(priceFocusListenser);
		inputunitprice.setOnFocusChangeListener(priceFocusListenser);

		date.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					datetimepicker.setVisibility(View.VISIBLE);
					datetimepicker.setIsShow(true);
					// 初始化datetimepicker的时间
					datetimepicker.setDateTime(datetime);
					Animation datetiminAnimation = AnimationUtils
							.loadAnimation(RecordActivity.this,
									R.anim.datetimein);
					datetimepicker.startAnimation(datetiminAnimation);
				} else {
					datetimepicker.setVisibility(View.GONE);
					datetimepicker.setIsShow(false);
				}

			}
		});

		// 日期控件
		datetimepicker.setOnDateTimeSetListener(new OnDateTimeSetListener() {

			@Override
			public void onDateTimeSet(int year, int monthOfYear,
					int dayOfMonth, int hour, int minute) {
				// TODO Auto-generated method stub
				mCalendar.set(year, monthOfYear - 1, dayOfMonth, hour, minute);
				datetime = df.format(mCalendar.getTime());
				date.setText(datetime);
				date.clearFocus();
			}

			@Override
			public void onDateTimeChange(int year, int monthOfYear,
					int dayOfMonth, int hour, int minute) {
				// TODO Auto-generated method stub
				mCalendar.set(year, monthOfYear - 1, dayOfMonth, hour, minute);
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
		lastdata = this.getSharedPreferences("lastdata", MODE_PRIVATE);
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

	private void adjustitemSize() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		datetimepicker.setFontSize(width);
		remark.setHeight(height / 4);
	}

	private boolean isRangeApplicable() {
		range = inputrange.getText().toString();
		return !(range.length() == 0 || range.equals("."));
	}

	private boolean isTotalpriceApplicable() {
		totalprice = inputtotalprice.getText().toString();
		return !(totalprice.length() == 0 || totalprice.equals("."));
	}

	private boolean isUnitpriceApplicable() {
		unitprice = inputunitprice.getText().toString();
		return !(unitprice.length() == 0 || unitprice.equals(".") || Float
				.parseFloat(unitprice) == 0);
	}

	private boolean isRemarkApplicable() {
		sqlremark = remark.getText().toString();
		return !(sqlremark.length() == 0);
	}

	private boolean isRangeCorrect() {
		int prevrange = -1;
		int folrange = -1;
		DbHelper helper = new DbHelper(getApplicationContext());
		Cursor prev = helper.queryPrev(sqldatetime, deleteid);
		Cursor fol = helper.queryFol(sqldatetime, deleteid);
		if (prev.moveToFirst()) {
			prevrange = prev.getInt(prev.getColumnIndex("range"));
		}
		if (fol.moveToFirst()) {
			folrange = fol.getInt(fol.getColumnIndex("range"));
		}
		if (prevrange != -1 && folrange == -1) {
			if (sqlrange <= prevrange) {
				Toast.makeText(RecordActivity.this,
						"行车里程需大于前一次记录(" + sql2show(prevrange) + ")", Toast.LENGTH_LONG)
						.show();
				return false;
			}
		}
		if (prevrange != -1 && folrange != -1) {
			if (sqlrange <= prevrange || sqlrange >= folrange) {
				Toast.makeText(
						RecordActivity.this,
						"行车里程需要大于前一次记录(" + sql2show(prevrange) + ")且小于后一次记录("
								+ sql2show(folrange) + ")!", Toast.LENGTH_LONG).show();
				return false;
			}
		}
		if (prevrange == -1 && folrange != -1) {
			if (sqlrange >= folrange) {
				Toast.makeText(RecordActivity.this,
						"首次记录需要小于后一次记录数(" + sql2show(folrange) + ")", Toast.LENGTH_LONG)
						.show();
				return false;
			}
		}
		return true;
	}

	private int getSqlDateTime(String datetime) {
		int year = Integer.parseInt(datetime.substring(0, 4));
		int month = Integer.parseInt(datetime.substring(5, 7)) - 1;
		int day = Integer.parseInt(datetime.substring(8, 10));
		int hour = Integer.parseInt(datetime.substring(11, 13));
		int minute = Integer.parseInt(datetime.substring(14, 16));
		mCalendar.set(year, month, day, hour, minute, second);
		return (int) (mCalendar.getTime().getTime() / 1000);
	}

	private void requestFocusAndInput(EditText e) {
		e.requestFocus();
		imm.showSoftInput(e, 0);
	}
	
	private int show2sql(String show){
		return (int) (Float.parseFloat(show) * 100);
	}
	
	protected String sql2show(int sql){
		return String.format("%.2f", (float)sql/100);
	}

	private void autoinput() {
		disamount
				.setBackgroundResource(R.drawable.bg_record_disamount_edittext);
		RecordActivity.this.amount = "N/A";
		disamount.setText(RecordActivity.this.amount);

		if (!isTotalpriceApplicable() | !isUnitpriceApplicable()) {
			return;
		}
		disamount
				.setBackgroundResource(R.drawable.bg_record_disamount_edittext_input);
		float ftotalprice = Float.parseFloat(totalprice);
		float funitprice = Float.parseFloat(unitprice);
		RecordActivity.this.amount = String.format("%.2f", ftotalprice
				/ funitprice);
		sqlamount = show2sql(RecordActivity.this.amount);
		disamount.setText(RecordActivity.this.amount + " 升");
	}

	private boolean checkinput(ContentValues values) {
		// check range
		if (!isRangeApplicable()) {
			Toast.makeText(RecordActivity.this, "请输入正确的行车里程",
					Toast.LENGTH_SHORT).show();
			requestFocusAndInput(inputrange);
			return false;
		} else
			sqlrange = show2sql(range);
		System.out.println("range" + ":" + sqlrange);

		// check price
		if (!isTotalpriceApplicable()) {
			Toast.makeText(RecordActivity.this, "请输入正确的支付金额",
					Toast.LENGTH_SHORT).show();
			requestFocusAndInput(inputtotalprice);
			return false;
		} else
			sqltotalprice = show2sql(totalprice);
		System.out.println("totalprice" + ":" + sqltotalprice);

		if (!isUnitpriceApplicable()) {
			Toast.makeText(RecordActivity.this, "请输入正确的燃油价格",
					Toast.LENGTH_SHORT).show();
			requestFocusAndInput(inputunitprice);
			return false;
		} else
			sqlunitprice = show2sql(unitprice);
		System.out.println("unitprice" + ":" + sqlunitprice);

		// check amount
		System.out.println("amount" + ":" + sqlamount);

		// check datetime
		sqldatetime = getSqlDateTime(datetime);
		
		System.out.println("datetime" + ":" + datetime);
		System.out.println("datetime" + ":" + sqldatetime);

		// check full and empty
		if (isempty.isChecked()) {
			sqlisempty = 1;
		} else
			sqlisempty = 0;
		if (isfull.isChecked()) {
			sqlisfull = 1;
		} else
			sqlisfull = 0;
		System.out.println("isempty" + ":" + sqlisempty);
		System.out.println("isfull" + ":" + sqlisfull);

		// check remark
		if (!isRemarkApplicable()) {
			sqlremark = "加油";
		}
		System.out.println("remark" + ":" + sqlremark);
		if (!isRangeCorrect()) {
			requestFocusAndInput(inputrange);
			return false;
		}
		values.put("_id", sqldatetime);
		values.put("range", sqlrange);
		values.put("totalprice", sqltotalprice);
		values.put("unitprice", sqlunitprice);
		values.put("amount", sqlamount);
		values.put("isempty", sqlisempty);
		values.put("isfull", sqlisfull);
		values.put("remark", sqlremark);
		Editor editor = lastdata.edit();
		editor.putInt("lastunitprice", sqlunitprice);
		editor.commit();
		return true;
	}
}
