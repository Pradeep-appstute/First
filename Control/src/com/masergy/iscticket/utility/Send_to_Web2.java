package com.masergy.iscticket.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.masergy.iscticket.Activity_SliderMenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Send_to_Web2 {
	String name,email;
	String Number;
	Context mContext;
	ProgressDialog mpProgress;
	SharedPreferences sharedPreferences;
	Editor editor;
	

	public Send_to_Web2(Context context,String name,String email,String number) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.email=email;
		this.Number=number;
		this.mContext=context;
		sharedPreferences = mContext.getSharedPreferences("com.example.secure_life", Context.MODE_PRIVATE);
		editor=sharedPreferences.edit();
	
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
					mpProgress= ProgressDialog.show(mContext, "Saving details", "Please wait for a moment...");
				}
			});
			
		}
		@Override
		protected String doInBackground(Void... params) {


			String responce = null;
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://lifeideas-resqume.appspot.com/new");

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
				nameValuePairs.add(new BasicNameValuePair("Name", name));
				nameValuePairs.add(new BasicNameValuePair("Number", Number));
				nameValuePairs.add(new BasicNameValuePair("Email", email));
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				System.out.println(response);
				responce=response.toString();


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

				editor.putString("Posedtoweb", "yes");
				editor.commit();
				Toast.makeText(mContext, "Response-"+result, 1000).show();
			}
			((Activity)mContext).finish();
		}

	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}


}