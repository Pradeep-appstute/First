package com.masergy.iscControl.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

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
import com.masergy.iscControl.ContentView.Fragment_ModifyService;
import com.masergy.iscControl.ContentView.ModifyService;


public class Webservice_GetModifyServiceDetails {
	
	String fileName = "ModifyService";
	SharedPreferences.Editor sharedPrefEditor;
	//ArrayList<ModifyService> serviceList;
	String webServiceLink = CommonResources.prefixLink+"bundle/";

	Context mContext;
	ProgressDialog mpProgress;
	String bundleId;

	public Webservice_GetModifyServiceDetails(Context context, String bundleId) {
		this.mContext = context;
		this.bundleId = bundleId;
		webServiceLink = webServiceLink+bundleId+"/modify/tier";
		sharedPrefEditor = ((Activity) context).getSharedPreferences(fileName, context.MODE_PRIVATE).edit();
	}

	public void postData() {
		if (isNetworkAvailable()) {
			post_data post = new post_data();
			post.execute();
		} else {
			Toast.makeText(mContext, "No network availble", 1000).show();
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
//					Log.d("tag", "modifyservicedetails=" + result);
					instream.close();

					/*
					 	[
  						{
    						"bundleId": "MB098596",
    						"bundleAlias": "PLANO1",
    						"currentBandwidth": "42 Mbps",
    						"location": "Plano, TX"
  						},
  						{
    						"bundleId": "MB098596",
    						"bundleAlias": "PLANO1",
    						"currentBandwidth": "42 Mbps",
    						"location": "Plano, TX"
  						}
						]
					 */
//					// Convert string to JSONArray
//					JSONArray jsonArray;
//					try {
//						jsonArray = new JSONArray(result);
//						// Getting JSON Array node
//						for (int i = 0; i < jsonArray.length(); i++) {
//
//							JSONObject jsonObj = jsonArray.getJSONObject(i);
//							ModifyService service = new ModifyService();
//							/*
//							 * Log.d("tag", "" + jsonObj.get("bundleId"));
//							 * Log.d("tag", "" + jsonObj.get("bundleAlias"));
//							 * Log.d("tag", "" + jsonObj.get("currentBandwidth"));
//							 * Log.d("tag", "" + jsonObj.get("location"));
//							 */
//							if (!(jsonObj.get("bundleId").equals(JSONObject.NULL)))
//								service.bundleId = jsonObj.getString("bundleId");
//							else
//								service.bundleId = "-1";
//
//							if (!(jsonObj.get("bundleAlias").equals(JSONObject.NULL)))
//								service.bundleAlias = jsonObj.getString("bundleAlias");
//							else
//								service.bundleAlias = "";
//
//							if (!(jsonObj.get("currentBandwidth").equals(JSONObject.NULL)))
//								service.currentBandwidth = jsonObj.getString("currentBandwidth");
//							else
//								service.currentBandwidth = "-1";
//
//							
//							if (!(jsonObj.get("location").equals(JSONObject.NULL)))
//								service.location = jsonObj.getString("location");
//							else
//								service.location = "-1";
//							
//							serviceList.add(service);
//							
//						}// for	
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}

					
//					//Store serviceList array in shared preference file
//					if()
				
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
				if (result!=null)
				{
					if(result.length()>0)
					{
						//Save modify service to shared preference file
						sharedPrefEditor.putString("modifyservicedetails", result.toString());
						sharedPrefEditor.commit();
						
						//Init service details
						Fragment_ModifyService.initServiceDetailsView();

					}
				}

			} else {
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