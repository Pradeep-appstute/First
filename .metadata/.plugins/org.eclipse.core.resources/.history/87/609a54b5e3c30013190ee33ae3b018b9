package com.masergy.iscticket.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import com.masergy.iscticket.Activity_Home;
import com.masergy.iscticket.Activity_Login;
import com.masergy.iscticket.Activity_SliderMenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
  * 
  * 
  
You can try using JSONArray as JSON is light-weight also, you can create a JSONArray and write it to SharedPreference as String.

To write,

       SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(1);
        jsonArray.put(2);
        Editor editor = prefs.edit();
        editor.putString("key", jsonArray.toString());
        System.out.println(jsonArray.toString());
        editor.commit();

To Read,

        try {
            JSONArray jsonArray2 = new JSONArray(prefs.getString("key", "[]"));
            for (int i = 0; i < jsonArray2.length(); i++) {
                 Log.d("your JSON Array", jsonArray2.getInt(i)+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  
  
 }

 */

public class Webservice_Logout {

	String webServiceLink = CommonResources.prefixLink+"auth";

	Context mContext;
	ProgressDialog mpProgress;

	public Webservice_Logout(Context context) {
		this.mContext = context;
	}

	public void postData() {
		if (isNetworkAvailable()) {
			post_data post = new post_data();
			post.execute();
		} else {
//			Toast.makeText(mContext, "No network availble", 1000).show();
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					Activity_SliderMenu.context);
	 
				// set title
				alertDialogBuilder.setTitle("Server Error");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Unable to connect, please check your internet connection.")
					.setCancelable(false)
					.setPositiveButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
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

	// =========================class post_data ========================
	class post_data extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mpProgress = ProgressDialog.show(mContext,
							"Logging out",
							"Please wait for a moment...");
				}
			});
		}

		@Override
		protected String doInBackground(Void... params) {
			// Variable declaration
			String result = null;

			// Create http client object to send request to server
			HttpClient httpclient = new DefaultHttpClient();
			// Create Request to server and get response
			HttpDelete httpdelete = new HttpDelete(webServiceLink);
//			httpdelete.setHeader("Content-Type", "application/json");
			httpdelete.setHeader(
					"Authorization",
					mContext.getSharedPreferences(Webservice_Login.fileName,
							mContext.MODE_PRIVATE).getString("authToken", null));
			HttpResponse response;
			try {

				response = httpclient.execute(httpdelete);
				HttpEntity entity = response.getEntity();
				if (entity != null) {

					// Read JSON Response
					InputStream instream = entity.getContent();
					StringWriter writer = new StringWriter();
					IOUtils.copy(instream, writer);
					result = writer.toString();
//					Log.d("tag", "theString Logout=" + result);
					instream.close();

				}// if
			} catch (ClientProtocolException e) {
				if (mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			} catch (IOException e) {
				if (mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			}
			return result;
		}



		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (mpProgress.isShowing())
				mpProgress.dismiss();
			super.onPostExecute(result);
			if (result != null) {

//				 Toast.makeText(mContext, "Response-"+result, 1000).show();
//				 System.out.println("Response="+result);
				Intent intent = new Intent(mContext.getApplicationContext(), Activity_Login.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);

			} else {
//				Toast.makeText(mContext, "No response from server", 1000).show();
//				System.out.println("No response from server");
			}
		}

	}

	// =================================================================

	// To check network connectivity
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}