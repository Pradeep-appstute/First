package com.masergy.iscControl;

import com.masergy.iscControl.R;
import com.masergy.iscControl.utility.Validation;
import com.masergy.iscControl.utility.Webservice_SendUidToServer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class Activity_ForgotUsername extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.forget_username);
		
		final EditText editText = (EditText) findViewById(R.id.edt_emailaddress);
		
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
				if (Validation.isEmpty(editText.getText().toString().trim())) {					
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(Activity_ForgotUsername.this);
					// Setting Dialog Title
					alertDialog.setTitle("Alert!");
					// Setting Dialog Message
					alertDialog.setMessage("Please enter email address.");

					// Setting Icon to Dialog
					alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

					alertDialog.setNeutralButton(
							getResources().getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});

					// Showing Alert Dialog
					alertDialog.show();
				}
				else
				{
				//Web-service call
				Webservice_SendUidToServer instance = new Webservice_SendUidToServer (Activity_ForgotUsername.this, editText.getText().toString().trim(), false);
				instance.postData();
				}
			}
		});
		
	}
}
