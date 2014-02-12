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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

public class Send_to_Web {
	
	String webServiceLink ="https://webservice-dev.masergy.com/webservices_mobile/rest/v1/auth";
	
	String name,password;
	Context mContext;
	ProgressDialog mpProgress;
	SharedPreferences sharedPreferences;
	Editor editor;
	

	public Send_to_Web(Context context,String name,String password) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.password=password;
		this.mContext=context;
//		sharedPreferences = mContext.getSharedPreferences("com.example.secure_life", Context.MODE_PRIVATE);
//		editor=sharedPreferences.edit();
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
					mpProgress= ProgressDialog.show(mContext, "Logging in", "Please wait for a moment...");
				}
			});
			
		}
		@Override
		protected String doInBackground(Void... params) {


			String responce = null;
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(webServiceLink);
			         httppost.setHeader("Content-Type", "application/json");
			         
			try {
				
				JSONObject jsonObj = new JSONObject();
				           try {
							jsonObj.put("username", name);
							jsonObj.put("password", password);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				          
				System.out.println("jsonObj.toString()="+jsonObj.toString());           
				StringEntity strEntity = new StringEntity(jsonObj.toString());
				strEntity.setContentType("application/json");
				
//				// Add your data
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//				nameValuePairs.add(new BasicNameValuePair("Name", name));
//				nameValuePairs.add(new BasicNameValuePair("Password", password));
				
				httppost.setEntity(strEntity);

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				System.out.println(response);
				responce=response.toString();
				
				
                 System.out.println("httpresponse" + response.getStatusLine().toString());
				StringBuilder sb = new StringBuilder();
				InputStream in = response.getEntity().getContent();
					    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					    String line = null;
					    while((line = reader.readLine()) != null){
					        sb.append(line);

					    }
					    System.out.println("Response from server-"+sb.toString());
					    
					  //  return sb.toString();
				return sb.toString();


			} catch (ClientProtocolException e) {
				if(mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			} catch (IOException e) {
				if(mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			}

			// TODO Auto-generated method stub
			return responce;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(mpProgress.isShowing())
				mpProgress.dismiss();
			super.onPostExecute(result);
			if (result!=null) {

//				editor.putString("Posedtoweb", "yes");
//				editor.commit();
				Toast.makeText(mContext, "Response-"+result, 1000).show();
				System.out.println("Response="+result);
				
				
			}
			else
			{
				Toast.makeText(mContext, "No response from server", 1000).show();
				System.out.println("No response from server");
			}
			//((Activity)mContext).finish();
		}

	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}


}