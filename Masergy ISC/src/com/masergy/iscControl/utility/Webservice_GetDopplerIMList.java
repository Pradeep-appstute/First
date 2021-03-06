package com.masergy.iscControl.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.masergy.iscControl.Activity_SliderMenu;
import com.masergy.iscControl.ContentView.DopplerIM_Parent;
import com.masergy.iscControl.ContentView.Fragment_DopplerIM;


public class Webservice_GetDopplerIMList {
	public static String fileName = "DopplerIM";
	SharedPreferences.Editor sharedPrefEditor;
//	ArrayList<ModifyService> serviceList;
	String webServiceLink = CommonResources.prefixLink+"node/list";
	Context mContext;
	ProgressDialog mpProgress;

	public Webservice_GetDopplerIMList(Context context) {
		this.mContext = context;
		
		sharedPrefEditor = ((Activity) context).getSharedPreferences(fileName,
				context.MODE_PRIVATE).edit();
//		serviceList = new ArrayList<ModifyService>();
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
			super.onPreExecute();

			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
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
			HttpGet httpget = new HttpGet(webServiceLink);
			httpget.setHeader("Content-Type", "application/json");
			httpget.setHeader(
					"Authorization",
					mContext.getSharedPreferences(Webservice_Login.fileName,
							mContext.MODE_PRIVATE).getString("authToken", null));
			HttpResponse response;
			try {

				response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				if (entity != null) {

					// Read JSON Response
					InputStream instream = entity.getContent();
					StringWriter writer = new StringWriter();
					IOUtils.copy(instream, writer);
					result = writer.toString();
//					Log.d("tag", "modifyservice=" + result);
					instream.close();				
				}// if

			} catch (ClientProtocolException e) {
				if (mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			} catch (IOException e) {
				if (mpProgress.isShowing())
					mpProgress.dismiss();
			}
			
			
			return result;
		}//doInBackground()



		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (mpProgress.isShowing())
				mpProgress.dismiss();
			super.onPostExecute(result);
			if (result != null) {

				// Toast.makeText(mContext, "Response-"+result, 1000).show();
//				 System.out.println("DopplerIM="+result);
				if (result!=null)
				{
					if(result.length()>0)
					{
						try {
						
						//Check if code and message has any message
						boolean flag=true;
						JSONArray jsonArray = new JSONArray(result);
						
						{
						//Save modify service to shared preference file
						sharedPrefEditor.putString("dopplerim", result.toString());
						sharedPrefEditor.commit();
						
						
						//JSON parsing
					// Convert string to JSONArray
				
						
							jsonArray = new JSONArray(result);
							JSONObject jsonObj;
							// Getting JSON Array node
							for (int i = 0; i < jsonArray.length(); i++) {
			
								jsonObj = jsonArray.getJSONObject(i);
								DopplerIM_Parent dopplerim = new DopplerIM_Parent();
								/*
								 * Log.d("tag", "" + jsonObj.get("bundleId")); Log.d("tag",
								 * "" + jsonObj.get("bundleAlias")); Log.d("tag", "" +
								 * jsonObj.get("currentBandwidth")); Log.d("tag", "" +
								 * jsonObj.get("location"));
								 */
								if (!(jsonObj.get("id").equals(JSONObject.NULL)))
									dopplerim.id= jsonObj.getString("id");
								else
									dopplerim.id = "-1";
			
								if (!(jsonObj.get("name").equals(JSONObject.NULL)))
									dopplerim.name = jsonObj.getString("name");
								else
									dopplerim.name = "";
			
								if (!(jsonObj.get("alarmState").equals(JSONObject.NULL)))
									dopplerim.alarmState = jsonObj.getString("alarmState");
								else
									dopplerim.alarmState = "-1";
			
								//dopplerim.child = new DopplerIM_Child();
								Fragment_DopplerIM.dopplerIM_Parents.add(dopplerim);
							}// for
						
						
						//Init listview
						Fragment_DopplerIM.initExpandableListView();
						
					}
						
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}//if (length>0)
				
				}
				
			} else {
				Fragment_DopplerIM.noResponseFromServer();
//				Toast.makeText(mContext, "No response from server", 1000).show();
//				System.out.println("No response from server");
			}
		}

	}
	// To check network connectivity
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}