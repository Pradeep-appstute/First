package com.masergy.iscticket;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Activity_ForgotUsername extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.forget_username);
		
		ImageButton bt_cancel=((ImageButton)findViewById(R.id.bt_cancel));
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		

		ImageButton bt_submit=((ImageButton)findViewById(R.id.bt_submit));
		bt_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//Web-service call
				
			finish();	
			}
		});
		
	}
}
