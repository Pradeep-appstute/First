package com.masergy.iscticket.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.masergy.iscticket.Activity_SliderMenu;

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
}
  
 */


public class Webservice_GetTicketsList {
	
	SharedPreferences.Editor sharedPrefEditor;
	
	String webServiceLink ="https://webservice-dev.masergy.com/webservices_mobile/rest/v1/ticket/list";
//	String webServiceLink ="https://webservice.masergy.com/webservices_mobile/rest/v1/ticket/list";
	Context mContext;
	ProgressDialog mpProgress;

	public Webservice_GetTicketsList(Context context) {
		this.mContext=context;
		String fileName = "Login";
		sharedPrefEditor = ((Activity) context).getSharedPreferences(fileName, context.MODE_PRIVATE).edit();
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
					mpProgress= ProgressDialog.show(mContext, "Ticket web-service call", "Please wait for a moment...");
				}
			});
			
		}
		@Override
		protected String doInBackground(Void... params) {
			//Variable declaration
			String result=null;
						
			// Create http client object to send request to server
			HttpClient httpclient = new DefaultHttpClient(); 
			// Create Request to server and get response
			HttpGet httpget = new HttpGet(webServiceLink);
			        httpget.setHeader("Content-Type", "application/json");
			        httpget.setHeader("Authorization", mContext.getSharedPreferences(Send_to_Web.fileName, mContext.MODE_PRIVATE).getString("authToken",null));
			 HttpResponse response;        
			try {
				
				response = httpclient.execute(httpget);         
		        HttpEntity entity = response.getEntity();
		        if (entity != null) {

		            // A Simple JSON Response Read
		            InputStream instream = entity.getContent();
		            System.out.println("instream="+instream.toString());
		            result = getStringFromInputStream(instream);
		            // now you have the string representation of the HTML request
		            System.out.println("RESPONSE: " + result);
		            instream.close();
//		            if (response.getStatusLine().getStatusCode() == 200) {
//		                netState.setLogginDone(true);
//		            }
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
			
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(mpProgress.isShowing())
				mpProgress.dismiss();
			super.onPostExecute(result);
			if (result!=null) {

				//Toast.makeText(mContext, "Response-"+result, 1000).show();
				//System.out.println("Response="+result);

			}
			else
			{
				Toast.makeText(mContext, "No response from server", 1000).show();
				System.out.println("No response from server");
			}
		}

	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}

	private static String getStringFromInputStream(InputStream is) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}
}