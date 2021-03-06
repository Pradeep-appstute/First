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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
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
import com.masergy.iscControl.ContentView.DopplerIMListAdapter;
import com.masergy.iscControl.ContentView.DopplerIM_Child;
import com.masergy.iscControl.ContentView.DopplerIM_Parent;
import com.masergy.iscControl.ContentView.Fragment_DopplerIM;
import com.masergy.iscControl.ContentView.Fragment_ModifyService;
import com.masergy.iscControl.ContentView.ModifyService;


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
						
//						Log.d("tag", "dopplerimdetails***>>>>>>>>>>"+groupPosition+"-"+result);
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
								
								ArrayList<DopplerIM_Child> dopplerim_childList = new ArrayList<DopplerIM_Child>();
								/*
								 * Log.d("tag", "" + jsonObj.get("bundleId")); Log.d("tag",
								 * "" + jsonObj.get("bundleAlias")); Log.d("tag", "" +
								 * jsonObj.get("currentBandwidth")); Log.d("tag", "" +
								 * jsonObj.get("location"));
								 */
								
			
								if (!(jsonObj.get("cloudId").equals(JSONObject.NULL)))
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Cloud:"+jsonObj.getString("cloudId"));
									dopplerim_childList.add(child);
								}
									
								else
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Cloud:");
									dopplerim_childList.add(child);
								}
								
								if (!(jsonObj.get("ipAddress").equals(JSONObject.NULL)))
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("IP:"+jsonObj.getString("ipAddress"));
									dopplerim_childList.add(child);
								}
								else
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("IP:");
									dopplerim_childList.add(child);
								}
				
								
									
			
								if (!(jsonObj.get("type").equals(JSONObject.NULL)))
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Type:"+jsonObj.getString("type"));
									dopplerim_childList.add(child);
								}
								else
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Type:");
									dopplerim_childList.add(child);
								}
			
								if (!(jsonObj.get("alarmState").equals(JSONObject.NULL)))
								{
									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Status:"+jsonObj.getString("alarmState"));
									dopplerim_childList.add(child);
								}
									
								else
								{
									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Status:");
									dopplerim_childList.add(child);
								}
									
								if (!(jsonObj.get("site").equals(JSONObject.NULL)))
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Site:"+jsonObj.getString("site"));
									dopplerim_childList.add(child);
								}
								else
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Site:");
									dopplerim_childList.add(child);
								}
								
								if (!(jsonObj.get("assetManufacturer").equals(JSONObject.NULL)))
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Manufacturer:"+jsonObj.getString("assetManufacturer"));
									dopplerim_childList.add(child);
								}
									
								else
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Manufacturer:");
									dopplerim_childList.add(child);
								}
								
								if (!(jsonObj.get("assetModel").equals(JSONObject.NULL)))
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Model:"+jsonObj.getString("assetModel"));
									dopplerim_childList.add(child);
								}
									
								else
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Model:");
									dopplerim_childList.add(child);
								}
								
								
								if (!(jsonObj.get("createDate").equals(JSONObject.NULL)))
								{

									DopplerIM_Child child = new DopplerIM_Child();
									
									child.setchildText("Created:"+DateTimeFormat.forPattern("MM/dd/yyyy").print(new DateTime(Long.parseLong(jsonObj.getString("createDate")))).toString());
									dopplerim_childList.add(child);
								}
									
								else
								{

									DopplerIM_Child child = new DopplerIM_Child();
									child.setchildText("Created:");
									dopplerim_childList.add(child);
								}
							
								DopplerIMListAdapter.filtered_dopplerIM_Parents.get(groupPosition).setChildren(dopplerim_childList);								
								Fragment_DopplerIM.listAdapter.notifyDataSetChanged();//Recreate listview
								Fragment_DopplerIM.expListView.expandGroup(groupPosition);
							}// for
						 catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}//eof if(result.length()>0)
				}//eof if (result!=null)

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