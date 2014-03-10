package com.masergy.iscticket.utility;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.masergy.iscticket.Activity_Home;

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
			Toast.makeText(mContext, "No network availble", 1000).show();
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
					mpProgress= ProgressDialog.show(mContext, "Creating Ticket", "Please wait for a moment...");
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
								mContext.getSharedPreferences(Send_to_Web.fileName,
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

				Toast.makeText(mContext, "Response-"+result, 1000).show();
				//System.out.println("Response="+result);
			}
			else
			{
				Toast.makeText(mContext, "No response from server", 1000).show();
				//System.out.println("No response from server");
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