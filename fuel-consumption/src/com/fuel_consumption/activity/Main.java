package com.fuel_consumption.activity;

import com.fuel_consumption.R;
import com.fuel_consumption.db.DbHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class Main extends Activity {
	
	private static final String TAG = "Main";
	
	private Button record;
	private ListView listview;
	private DbHelper helper;
	private MyCursorAdapter mAdapter;
	private Cursor mCursor;
	private long exitTime;
	
	LayoutInflater layoutInflater;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initUI();
		initClickListener();
		layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		helper = new DbHelper(getApplicationContext());
		mCursor= helper.query();
		mAdapter = new MyCursorAdapter(this, mCursor, true);
		listview.setAdapter(mAdapter);
		startManagingCursor(mCursor);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i(TAG, "onRestart");
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
		helper.close();
		Log.i(TAG, "onDestroy");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	        	finish();
	        	android.os.Process.killProcess(android.os.Process.myPid());
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void initUI(){
		record = (Button) findViewById(R.id.record);
		listview = (ListView) findViewById(R.id.test);
	}
	
	private void initClickListener(){
		record.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Main.this, Record.class);
				startActivity(intent);
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				for (int i = 0 ;i < arg0.getCount();i++){
					LinearLayout afterlongpress = (LinearLayout)arg0.getChildAt(i).findViewById(R.id.afterlongpress);
					afterlongpress.setVisibility(View.GONE);
				}
				Intent intent = new Intent(Main.this, EditRecord.class);   
				Bundle bundle = new Bundle();                             
				bundle.putInt("time", (int)arg3);  
				intent.putExtras(bundle);                              
				startActivity(intent);                                 
			}
		});
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub	
				final LinearLayout afterlongpress = (LinearLayout) arg1.findViewById(R.id.afterlongpress);
				final int id = (int) arg3;
				afterlongpress.setVisibility(View.VISIBLE);
				afterlongpress.setOnClickListener(new OnClickListener() {		
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						afterlongpress.setVisibility(View.GONE);
					}
				});
		
				Button delete = (Button) afterlongpress.findViewById(R.id.delete);
				delete.setOnClickListener(new OnClickListener() {	
					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						helper.delete(id);
						mCursor.requery();
						afterlongpress.setVisibility(View.GONE);
					}
				});
				return true;
			}
		});
	}
}
