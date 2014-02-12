package com.masergy.iscticket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.masergy.iscticket.utility.NetworkConnection;
import com.masergy.iscticket.utility.Send_to_Web;
import com.masergy.iscticket.utility.Validation;

public class Activity_Login extends Activity {

	EditText edt_UserName, edt_Password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Init
		edt_UserName = (EditText) findViewById(R.id.editTextUserName);
		edt_Password = (EditText) findViewById(R.id.editTextPassword);
	}

	public void loginButtonTapped(View v) {
		/*
		 * Intent intentHome; intentHome = new Intent(this,
		 * Activity_SliderMenu.class); startActivity(intentHome);
		 */
		
		//Check if any of the field is empty
		if (Validation.isEmpty(edt_UserName.getText().toString())
				|| Validation.isEmpty(edt_Password.getText().toString())) {
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			// Setting Dialog Title
			alertDialog.setTitle("Alert!");
			// Setting Dialog Message
			alertDialog
					.setMessage("Username or Password field is empty");

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

			//Check if email address entered is valid
			if (Validation.validEmail(edt_UserName.getText().toString()) == false) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
				// Setting Dialog Title
				alertDialog.setTitle("Alert!");
				// Setting Dialog Message
				alertDialog
						.setMessage("Please enter valid email address in username field");

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
				//Check network connectivity
				if(NetworkConnection.isNetworkAvailable(Activity_Login.this)==false)
				{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
					// Setting Dialog Title
					alertDialog.setTitle("Alert!");
					// Setting Dialog Message
					alertDialog
							.setMessage("Internet connectivity not found");

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
					Log.d("TAG", "Calling webservice");
					//Call login web-service
					Send_to_Web send_to_Web = new Send_to_Web(Activity_Login.this, edt_UserName.getText().toString(), edt_Password.getText().toString());
					            send_to_Web.postData();
				}
			}
		}// if-else
	}
}
