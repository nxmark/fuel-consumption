package com.nxmark.fuel_consumption.activity;

import com.nxmark.fuel_consumption.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button record;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initUI();
		initClickListener();
	}
	

	private void initUI(){
		record = (Button) findViewById(R.id.record);
	}
	
	private void initClickListener(){
		record.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, RecordActivity.class);
				startActivity(intent);
				
			}
		});
		
	}

}
