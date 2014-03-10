package com.masergy.iscticket.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EncodingUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.masergy.iscticket.Activity_SliderMenu;
import com.masergy.iscticket.ContentView.Fragment_Tickets;
import com.masergy.iscticket.ContentView.Ticket;

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

public class Webservice_GetSubmitData {

	SharedPreferences.Editor sharedPrefEditor;
	String webServiceLinkForSubmit = CommonResources.prefixLink+"ticket/submit";
	Context mContext;
	ProgressDialog mpProgress;

	public Webservice_GetSubmitData(Context context) {
		this.mContext = context;
		String fileName = "Login";
		sharedPrefEditor = ((Activity) context).getSharedPreferences(fileName,
				context.MODE_PRIVATE).edit();
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
							"Downloading data",
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
			HttpGet httpget = new HttpGet(webServiceLinkForSubmit);
			httpget.setHeader("Content-Type", "application/json");
			httpget.setHeader(
					"Authorization",
					mContext.getSharedPreferences(Send_to_Web.fileName,
							mContext.MODE_PRIVATE).getString("authToken", null));
			HttpResponse response_submit;
			try {

				response_submit = httpclient.execute(httpget);
				HttpEntity entity = response_submit.getEntity();
				
				
				if (entity != null) {

					// Read JSON Response
					InputStream instream = entity.getContent();
					StringWriter writer = new StringWriter();
					IOUtils.copy(instream, writer);
					result = writer.toString();
					Log.d("tag", "submit---theString=" + result);
					instream.close();

					JSONObject jsonObj = new JSONObject(result);//;/jsonArray.getJSONObject(i)
					JSONArray bundle_JsonArray = jsonObj.getJSONArray("bundles");
					
					
					sharedPrefEditor.putString("bundles", bundle_JsonArray.toString());
					sharedPrefEditor.commit();
//					Log.d("tag", "bundle_JsonArray="+bundle_JsonArray.toString());
//					// Getting JSON Array node
//					for (int i = 0; i < bundle_JsonArray.length(); i++) {
//
//					     String bundle = (String) bundle_JsonArray.get(i);
//					      // loop and add it to array or arraylist
//						  Log.d("tag", "" + bundle);
//					}
						
					bundle_JsonArray = jsonObj.getJSONArray("subjects");
					sharedPrefEditor.putString("subjects", bundle_JsonArray.toString());
					sharedPrefEditor.commit();
//					Log.d("tag", "bundle_JsonArray="+bundle_JsonArray.toString());
//					// Getting JSON Array node
//					for (int i = 0; i < bundle_JsonArray.length(); i++) {
//
//					     String subject = (String) bundle_JsonArray.get(i);
//					      // loop and add it to array or arraylist
//						  Log.d("tag", "" + subject);
//		 
//					}					
				}// if

			} catch (JSONException e) {
				e.printStackTrace();
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

				// Toast.makeText(mContext, "Response-"+result, 1000).show();
				// System.out.println("Response="+result);
				Fragment_Tickets.imgButtonOpen.performClick();

			} else {
				Toast.makeText(mContext, "No response from server", 1000)
						.show();
				System.out.println("No response from server");
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