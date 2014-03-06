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

import com.masergy.iscticket.ContentView.DopplerIM_Child;
import com.masergy.iscticket.ContentView.DopplerIM_Parent;
import com.masergy.iscticket.ContentView.Fragment_DopplerIM;
import com.masergy.iscticket.ContentView.Fragment_ModifyService;
import com.masergy.iscticket.ContentView.ModifyService;


public class Webservice_GetDopplerIMNodeDetails {
	
	String fileName = "DopplerIM";
	SharedPreferences.Editor sharedPrefEditor;
	String webServiceLink = CommonResources.prefixLink+"node/";
	Context mContext;
	ProgressDialog mpProgress;
	String nodeId;
	int groupPosition;
	
	public Webservice_GetDopplerIMNodeDetails(Context context, String nodeId, int groupPosition) {
		this.mContext = context;
		this.nodeId = nodeId;
		webServiceLink = webServiceLink+nodeId;
		this.groupPosition=groupPosition;
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
					//Log.d("tag", "dopplerimdetails=" + result);
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
						
//						Log.d("tag", "groupPosition="+groupPosition+"-"+result);
						/*
						 {"id":"953",
						 "name":"Accounting",
						 "alarmState":"3",
						 "type":"Server",
						 "createDate":1376480376000,
						 "cloudId":"MS079631",
						 "cloudName":null,
						 "ipAddress":"10.0.20.100",
						 "site":"Plano, TX",
						 "creditPointsUsed":"2",
						 "assetManufacturer":"Unknown",
						 "assetModel":"Unknown"
						 }
						 */
						
						try {
								JSONObject jsonObj = new JSONObject(result);
								
								DopplerIM_Child dopplerim_child = new DopplerIM_Child();
								/*
								 * Log.d("tag", "" + jsonObj.get("bundleId")); Log.d("tag",
								 * "" + jsonObj.get("bundleAlias")); Log.d("tag", "" +
								 * jsonObj.get("currentBandwidth")); Log.d("tag", "" +
								 * jsonObj.get("location"));
								 */
								if (!(jsonObj.get("id").equals(JSONObject.NULL)))
									dopplerim_child.id= jsonObj.getString("id");
								else
									dopplerim_child.id = "-1";
			
								if (!(jsonObj.get("name").equals(JSONObject.NULL)))
									dopplerim_child.name = jsonObj.getString("name");
								else
									dopplerim_child.name = "";
			
								if (!(jsonObj.get("alarmState").equals(JSONObject.NULL)))
									dopplerim_child.alarmState = jsonObj.getString("alarmState");
								else
									dopplerim_child.alarmState = "-1";
			
								if (!(jsonObj.get("type").equals(JSONObject.NULL)))
									dopplerim_child.type= jsonObj.getString("type");
								else
									dopplerim_child.type = "";
			
								if (!(jsonObj.get("createDate").equals(JSONObject.NULL)))
									dopplerim_child.createDate = jsonObj.getString("createDate");
								else
									dopplerim_child.createDate = "";
			
								if (!(jsonObj.get("cloudId").equals(JSONObject.NULL)))
									dopplerim_child.cloudId = jsonObj.getString("cloudId");
								else
									dopplerim_child.cloudId = "-1";
								
								if (!(jsonObj.get("cloudName").equals(JSONObject.NULL)))
									dopplerim_child.cloudName= jsonObj.getString("cloudName");
								else
									dopplerim_child.cloudName = "";
			
								if (!(jsonObj.get("ipAddress").equals(JSONObject.NULL)))
									dopplerim_child.ipAddress = jsonObj.getString("ipAddress");
								else
									dopplerim_child.ipAddress = "";
			
								if (!(jsonObj.get("site").equals(JSONObject.NULL)))
									dopplerim_child.site = jsonObj.getString("site");
								else
									dopplerim_child.site = "";
								
								if (!(jsonObj.get("creditPointsUsed").equals(JSONObject.NULL)))
									dopplerim_child.creditPointsUsed= jsonObj.getString("creditPointsUsed");
								else
									dopplerim_child.creditPointsUsed = "-1";
			
								if (!(jsonObj.get("assetManufacturer").equals(JSONObject.NULL)))
									dopplerim_child.assetManufacturer = jsonObj.getString("assetManufacturer");
								else
									dopplerim_child.assetManufacturer = "";
			
								if (!(jsonObj.get("assetModel").equals(JSONObject.NULL)))
									dopplerim_child.assetModel = jsonObj.getString("assetModel");
								else
									dopplerim_child.assetModel = "-1";
								
								Fragment_DopplerIM.listDataHeader.get(groupPosition).child = dopplerim_child;
								
								Fragment_DopplerIM.initExpandableListView(groupPosition);//Recreate listview
							}// for
						 catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}//eof if(result.length()>0)
				}//eof if (result!=null)

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