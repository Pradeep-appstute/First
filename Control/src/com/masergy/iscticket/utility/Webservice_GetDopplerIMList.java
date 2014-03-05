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

import com.masergy.iscticket.ContentView.Fragment_DopplerIM;
import com.masergy.iscticket.ContentView.Fragment_ModifyService;
import com.masergy.iscticket.ContentView.ModifyService;


public class Webservice_GetDopplerIMList {
	public static String fileName = "DopplerIM";
	SharedPreferences.Editor sharedPrefEditor;
//	ArrayList<ModifyService> serviceList;
	String webServiceLink = "https://webservice-dev.masergy.com/webservices_mobile/rest/v1/node/list";
//	String webServiceLink ="https://webservice.masergy.com/webservices_mobile/rest/v1/node/list";
	Context mContext;
	ProgressDialog mpProgress;

	public Webservice_GetDopplerIMList(Context context) {
		this.mContext = context;
		
		sharedPrefEditor = ((Activity) context).getSharedPreferences(fileName,
				context.MODE_PRIVATE).edit();
//		serviceList = new ArrayList<ModifyService>();
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
					Log.d("tag", "modifyservice=" + result);
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
		}



		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (mpProgress.isShowing())
				mpProgress.dismiss();
			super.onPostExecute(result);
			if (result != null) {

				// Toast.makeText(mContext, "Response-"+result, 1000).show();
				 System.out.println("DopplerIM="+result);
				if (result!=null)
				{
					if(result.length()>0)
					{
						//Save modify service to shared preference file
						sharedPrefEditor.putString("dopplerim", result.toString());
						sharedPrefEditor.commit();
						
						//Init listview
						Fragment_DopplerIM.initListView();
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