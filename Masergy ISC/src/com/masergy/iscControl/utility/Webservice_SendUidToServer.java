package com.masergy.iscControl.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
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
import com.masergy.iscControl.Activity_SliderMenu;
import com.masergy.iscControl.ContentView.Fragment_Tickets;

public class Webservice_SendUidToServer {
	
	SharedPreferences.Editor sharedPrefEditor;
	
	String webServiceLink =CommonResources.prefixLink+"auth/";
//	public static String fileName = "Login";
	String uid;
	Context mContext;
	ProgressDialog mpProgress;
	boolean calledFromForgotPassword;
	
	public Webservice_SendUidToServer(Context context,String uid, boolean calledFromForgotPassword)
	{
		this.uid=uid;
		webServiceLink = webServiceLink+uid;
		this.mContext=context;
		this.calledFromForgotPassword = calledFromForgotPassword;
//		sharedPrefEditor = ((Activity) context).getSharedPreferences(fileName, context.MODE_PRIVATE).edit();
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
					mpProgress= ProgressDialog.show(mContext, "Sending request to server", "Please wait for a moment...");
				}
			});
			
		}
		@Override
		protected String doInBackground(Void... params) {
			//Variable declaration
			StringBuilder sb = new StringBuilder();
			
			
			if(calledFromForgotPassword)
			{
			
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(webServiceLink);

			         
			try {
				
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				
//				Log.d("tag", "Forgot Password Response="+response.getStatusLine().getStatusCode());
				sb.append(response.getStatusLine().getStatusCode());
			} catch (ClientProtocolException e) {
				if(mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			} catch (IOException e) {
				if(mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			}
			}
			else //Forgot Username
			{
				// Variable declaration
				String result = null;
				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				// Create Request to server and get response
				HttpGet httpget = new HttpGet(webServiceLink);
				try {

					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httpget);
//					Log.d("tag", "Forgot Username Response="+response.getStatusLine().getStatusCode());
					sb.append(response.getStatusLine().getStatusCode());

				} catch (ClientProtocolException e) {
					if(mpProgress.isShowing())
						mpProgress.dismiss();
					// TODO Auto-generated catch block
				} catch (IOException e) {
					if(mpProgress.isShowing())
						mpProgress.dismiss();
					// TODO Auto-generated catch block
				}	
			}
			return sb.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(mpProgress.isShowing())
				mpProgress.dismiss();
			super.onPostExecute(result);
			if (result!=null) {
				
				

//				Toast.makeText(mContext, "Response-"+result, 1000).show();

				if(result.equalsIgnoreCase("200"))
				{
					//display invalid user name  or password
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
					 
					// set title
					alertDialogBuilder.setTitle("Request Successful");
		 
					// set dialog message
					alertDialogBuilder
					    .setMessage("We have sent the requested information to your email address.")
						.setCancelable(false)
						.setPositiveButton("OK",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
								((Activity)mContext).finish();
							}
						  });
						
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
				}
				else if (result.equalsIgnoreCase("500"))
				{
					//display invalid user name  or password
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
					 
					// set title
					alertDialogBuilder.setTitle("Request Failed");
		 
					// set dialog message
					alertDialogBuilder
					    .setMessage("Invalid UserId. Please try again, or contact Masergy Customer Service.")
						.setCancelable(false)
						.setPositiveButton("OK",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
								((Activity)mContext).finish();
							}
						  });
						
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
				}
				else
				{
					//display invalid user name  or password
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
					 
					// set title
					alertDialogBuilder.setTitle("Request Failed");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("Your request was not successful.  Please try again, or contact Masergy Customer Service.")
						.setCancelable(false)
						.setPositiveButton("OK",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
								((Activity)mContext).finish();
							}
						  });
						
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
				}
			}
			else
			{
//				Toast.makeText(mContext, "No response from server", 1000).show();
//				System.out.println("No response from server");
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