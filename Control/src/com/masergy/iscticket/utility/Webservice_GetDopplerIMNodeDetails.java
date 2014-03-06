package com.masergy.iscticket.utility;

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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.masergy.iscticket.ContentView.Fragment_ModifyService;
import com.masergy.iscticket.ContentView.ModifyService;


public class Webservice_GetDopplerIMNodeDetails {
	
	String fileName = "DopplerIM";
	SharedPreferences.Editor sharedPrefEditor;
	//ArrayList<ModifyService> serviceList;
	String webServiceLink = "https://webservice-dev.masergy.com/webservices_mobile/rest/v1/node/";
//	String webServiceLink ="https://webservice.masergy.com/webservices_mobile/rest/v1/node/";
	Context mContext;
	ProgressDialog mpProgress;
	String nodeId;

	public Webservice_GetDopplerIMNodeDetails(Context context, String nodeId) {
		this.mContext = context;
		this.nodeId = nodeId;
		webServiceLink = webServiceLink+nodeId;
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
					mContext.getSharedPreferences(Send_to_Web.fileName,
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
					Log.d("tag", "dopplerimdetails=" + result);
					instream.close();

					
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
						sharedPrefEditor.putString("dopplerimdetails", result.toString());
						sharedPrefEditor.commit();
						
						//Init service details
						//Fragment_ModifyService.initServiceDetailsView();
					}
				}

			} else {
				Toast.makeText(mContext, "No response from server", 1000)
						.show();
				System.out.println("No response from server");
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