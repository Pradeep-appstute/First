package com.masergy.iscControl.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebView;

import com.masergy.iscControl.Activity_Home;
import com.masergy.iscControl.Activity_Login;
import com.masergy.iscControl.Activity_SliderMenu;

/*
 To edit data from sharedpreference

 SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
 editor.putString("text", mSaved.getText().toString());
 editor.putInt("selection-start", mSaved.getSelectionStart());
 editor.putInt("selection-end", mSaved.getSelectionEnd());
 editor.commit();

 To retrieve data from shared preference

 SharedPreferences prefs = getPreferences(MODE_PRIVATE); 
 String restoredText = prefs.getString("text", null);
 if (restoredText != null) 
 {
 //mSaved.setText(restoredText, TextView.BufferType.EDITABLE);
 int selectionStart = prefs.getInt("selection-start", -1);
 int selectionEnd = prefs.getInt("selection-end", -1);
 //if (selectionStart != -1 && selectionEnd != -1)
 //{
 //  mSaved.setSelection(selectionStart, selectionEnd);
 //}
 }

 */

public class Webservice_Login {

	SharedPreferences.Editor sharedPrefEditor;

	String webServiceLink = CommonResources.prefixLink + "auth";
	public static String fileName = "Login";
	String name, password;
	Context mContext;
	ProgressDialog mpProgress;

	public Webservice_Login(Context context, String name, String password) {
		this.name = name;
		this.password = password;
		this.mContext = context;

		sharedPrefEditor = ((Activity) context).getSharedPreferences(fileName,
				context.MODE_PRIVATE).edit();
	}

	public void postData() {
		if (isNetworkAvailable()) {
			post_data post = new post_data();
			post.execute();
		} else {
			// Toast.makeText(mContext, "No network availble", 1000).show();
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					Activity_SliderMenu.context);

			// set title
			alertDialogBuilder.setTitle("Server Error");

			// set dialog message
			alertDialogBuilder
					.setMessage(
							"Unable to connect, please check your internet connection.")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
	}

	class post_data extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mpProgress = ProgressDialog.show(mContext, "Logging in",
							"Please wait for a moment...");
				}
			});

		}

		@Override
		protected String doInBackground(Void... params) {
			// Variable declaration
			StringBuilder sb = new StringBuilder();
/*
			// ==========================User-Agent==========================
			// Detecting Operating System name and version
			StringBuilder builder = new StringBuilder();
			builder.append("OS: android : ").append(Build.VERSION.RELEASE);

			Field[] fields = Build.VERSION_CODES.class.getFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				int fieldValue = -1;

				try {
					fieldValue = field.getInt(new Object());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

				if (fieldValue == Build.VERSION.SDK_INT) {
					builder.append(" : ").append(fieldName).append(" : ");
					builder.append("sdk=").append(fieldValue);
				}
			}

			//Log.d("tag", "OS: " + builder.toString());
			
			// Detecting Device Type
			DisplayMetrics dm = new DisplayMetrics();
			((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
			int width=dm.widthPixels;
			int height=dm.heightPixels;
			int dens=dm.densityDpi;
			double wi=(double)width/(double)dens;
			double hi=(double)height/(double)dens;
			double x = Math.pow(wi,2);
			double y = Math.pow(hi,2);
			double screenInches = Math.sqrt(x+y) ;
			//Log.d("tag", "screenInches: " + screenInches);
			Log.d("tag", ""+builder.append(" screensize: " + format(screenInches)));
			// ==============================================================
*/
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(webServiceLink);
			httppost.setHeader("Content-Type", "application/json");
			httppost.setHeader("User-Agent", Activity_Login.userAgentString);
			try {

				// Prepare string entity using JSON string to be posted to
				// server
				JSONObject jsonObj = new JSONObject();
				try {
					jsonObj.put("username", name);
					jsonObj.put("password", password);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				StringEntity strEntity = new StringEntity(jsonObj.toString());
				strEntity.setContentType("application/json");
				// Set string entity
				httppost.setEntity(strEntity);

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				// Read server's response
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);

				}

			} catch (ClientProtocolException e) {
				if (mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			} catch (IOException e) {
				if (mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			}

			return sb.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (mpProgress.isShowing())
				mpProgress.dismiss();
			super.onPostExecute(result);
			if (result != null) {

				// Toast.makeText(mContext, "Response-"+result, 1000).show();
				// System.out.println("Response="+result);
				if (result.contains("authToken")
						&& result.contains("contactId")
						&& result.contains("userId")) {
					try {
						JSONObject jObj = new JSONObject(result);
						String authToken = jObj.getString("authToken");
						String contactId = jObj.getString("contactId");
						String userId = jObj.getString("userId");
						String firstName = jObj.getString("firstName");
						String lastName = jObj.getString("lastName");
						String email = (jObj.getString("email") == null || jObj
								.getString("email").equalsIgnoreCase("null")) ? ""
								: jObj.getString("email");
						String phone = (jObj.getString("phone") == null || jObj
								.getString("phone").equalsIgnoreCase("null")) ? ""
								: jObj.getString("phone");

						String permViewTicket = jObj
								.getString("permViewTicket");
						String permSubmitTicket = jObj
								.getString("permSubmitTicket");
						String permViewServiceDetails = jObj
								.getString("permViewServiceDetails");
						String permModifyTierNetworkAccess = jObj
								.getString("permModifyTierNetworkAccess");
						String permViewVnoc = jObj.getString("permViewVnoc");
						String custId = jObj.getString("custId");
						String custName = jObj.getString("custName");

						// System.out.println(authToken);
						// System.out.println(contactId);
						// System.out.println(userId);
						// System.out.println(firstName);

						sharedPrefEditor.putString("authToken", authToken);
						sharedPrefEditor.putString("contactId", contactId);
						sharedPrefEditor.putString("userId", userId);
						sharedPrefEditor.putString("firstName", firstName);
						sharedPrefEditor.putString("lastName", lastName);
						sharedPrefEditor.putString("email", email);
						sharedPrefEditor.putString("phone", phone);
						sharedPrefEditor.putString("permViewTicket",
								permViewTicket);
						sharedPrefEditor.putString("permSubmitTicket",
								permSubmitTicket);
						sharedPrefEditor.putString("permViewServiceDetails",
								permViewServiceDetails);
						sharedPrefEditor.putString(
								"permModifyTierNetworkAccess",
								permModifyTierNetworkAccess);
						sharedPrefEditor
								.putString("permViewVnoc", permViewVnoc);
						sharedPrefEditor.putString("custId", custId);
						sharedPrefEditor.putString("custName", custName);
						sharedPrefEditor.commit();

						// Start Activity_SliderMenu activity
						Intent intentHome;
						intentHome = new Intent(mContext, Activity_Home.class);
						mContext.startActivity(intentHome);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}// inner-if
				else {
					// display invalid user name or password
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							mContext);

					// set title
					alertDialogBuilder.setTitle("Invalid Username or Password");

					// set dialog message
					alertDialogBuilder.setCancelable(false).setPositiveButton(
							"OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
				}
			} else {
				// Toast.makeText(mContext, "No response from server",
				// 1000).show();
				// System.out.println("No response from server");
			}
		}

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static String format(Number n) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(2);
        return format.format(n);
    }

}