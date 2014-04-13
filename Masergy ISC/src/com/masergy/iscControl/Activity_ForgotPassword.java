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

public class Activity_ForgotPassword extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.forgot_password);
		
		final EditText editText = (EditText) findViewById(R.id.edt_emailaddress);
		
		ImageButton bt_cancel=((ImageButton)findViewById(R.id.bt_cancel));
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			finish();	
			}
		});
		

		ImageButton bt_submit=((ImageButton)findViewById(R.id.bt_submit));
		bt_submit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				//Check if any of the field is empty
				if (Validation.isEmpty(editText.getText().toString().trim())) {					
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(Activity_ForgotPassword.this);
					// Setting Dialog Title
					alertDialog.setTitle("Alert!");
					// Setting Dialog Message
					alertDialog.setMessage("Please enter user name.");

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
				Webservice_SendUidToServer instance = new Webservice_SendUidToServer (Activity_ForgotPassword.this, editText.getText().toString().trim(), true);
				instance.postData();
				}
			}
		});	
	}//onCreate
}
