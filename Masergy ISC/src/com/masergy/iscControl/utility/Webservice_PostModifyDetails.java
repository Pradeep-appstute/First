package com.masergy.iscControl.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.masergy.iscControl.Activity_Home;
import com.masergy.iscControl.Activity_Login;
import com.masergy.iscControl.Activity_SliderMenu;

public class Webservice_PostModifyDetails {	
	SharedPreferences.Editor sharedPrefEditor;
	
	String webServiceLink = CommonResources.prefixLink+"bundle/";

	Context mContext;
	ProgressDialog mpProgress;
	String bundleId;
	String bandwidth;
	public Webservice_PostModifyDetails(Context context,String bundleId,String bandwidth) {
		this.mContext = context;
		this.bundleId = bundleId;
		this.bandwidth = bandwidth;
		webServiceLink = webServiceLink+bundleId+"/modify/tier";
	}

	public void postData() {	
		if(isNetworkAvailable())
		{
		post_data post=new post_data();
		post.execute();
		}
		else
		{
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

	class post_data extends AsyncTask<Void, Void, String>    
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			((Activity)mContext).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mpProgress= ProgressDialog.show(mContext, "Modifying Service", "Please wait for a moment...");
				}
			});
			
		}
		@Override
		protected String doInBackground(Void... params) {
			//Variable declaration
			StringBuilder sb = new StringBuilder();
			
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(webServiceLink);
			         httppost.setHeader("Content-Type", "application/json");
			         httppost.setHeader(
								"Authorization",
								mContext.getSharedPreferences(Webservice_Login.fileName,
										mContext.MODE_PRIVATE).getString("authToken", null));
			         
			try {
			
				//Prepare string entity using JSON string to be posted to server	
				      JSONObject jsonObj = new JSONObject();
				           try {
							jsonObj.put("bandwidth", bandwidth);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				StringEntity strEntity = new StringEntity(jsonObj.toString());
				             strEntity.setContentType("application/json");
				//Set string entity			
				httppost.setEntity(strEntity);
				
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
                //Read server's response
				InputStream in = response.getEntity().getContent();
					    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					    String line = null;
					    while((line = reader.readLine()) != null){
					        sb.append(line);

					    }

			} catch (ClientProtocolException e) {
				if(mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			} catch (IOException e) {
				if(mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			}
			
			return sb.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			if(mpProgress.isShowing())
				mpProgress.dismiss();
			super.onPostExecute(result);
			if (result!=null) {

//				Toast.makeText(mContext, "Response-"+result, 1000).show();
//				System.out.println("Result Create Ticket="+result.toString());
				/*
				{
					  "bundleId": "MB061705",
					  "prodType": "NxT3 Network Access",
					  "currentBandwidth": "18 Mbps",
					  "confirmationId": "CN009501"
				}
					*/
				
				if(result.contains("bundleId") && result.contains("prodType") && result.contains("currentBandwidth") && result.contains("confirmationId"))
				{
					
					try {
						JSONObject jsonObj = new JSONObject(result);
						//String str_bundleID = jsonObj.getString("bundleId");
						String str_prodType = jsonObj.getString("prodType");
						String str_currentBandwidth = jsonObj.getString("currentBandwidth");
						String str_confirmationId = jsonObj.getString("confirmationId");
						
						String dialog_title = "Modify Service Confirmation "+str_confirmationId;
						String dialog_message = str_prodType+" successfully modified. New Bandwidth:"+str_currentBandwidth ;
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								Activity_SliderMenu.context);
				 
							// set title
							alertDialogBuilder.setTitle(dialog_title);
				 
							// set dialog message
							alertDialogBuilder
								.setMessage(dialog_message)
								.setCancelable(false)
								.setPositiveButton("OK",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
										Intent intent = new Intent(mContext.getApplicationContext(), Activity_Home.class);
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										mContext.startActivity(intent);
									}
								  });
								
				 
								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();
				 
								// show it
								alertDialog.show();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					

							
				}//inner-if
			}
			else
			{
//				Toast.makeText(mContext, "No response from server", 1000).show();
				//System.out.println("No response from server");
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Activity_SliderMenu.context);
		 
					// set title
					alertDialogBuilder.setTitle("Error");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("Unable to create ticket.")
						.setCancelable(false)
						.setPositiveButton("OK",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
								Intent intent = new Intent(mContext.getApplicationContext(), Activity_Home.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								mContext.startActivity(intent);
							}
						  });
						
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
			}
		}

	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}


}