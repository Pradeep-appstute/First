package com.masergy.iscticket.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EncodingUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.json.JSONArray;
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
import android.util.Log;
import android.widget.Toast;
import com.masergy.iscticket.Activity_SliderMenu;
import com.masergy.iscticket.ContentView.Fragment_Tickets;
import com.masergy.iscticket.ContentView.Ticket;


public class Webservice_GetTicketDetails {

	SharedPreferences.Editor sharedPrefEditor;
	String webServiceLinkForTicketDetails = CommonResources.prefixLink+"ticket/";

	Context mContext;
	String ticketId;
	ProgressDialog mpProgress;
	public static String response;

	public Webservice_GetTicketDetails(Context context, String ticketId) {
		this.mContext = context;
		this.ticketId = ticketId;
		webServiceLinkForTicketDetails = webServiceLinkForTicketDetails+ticketId;
		String fileName = "Login";
		sharedPrefEditor = ((Activity) context).getSharedPreferences(fileName,
				context.MODE_PRIVATE).edit();
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
			// TODO Auto-generated method stub
			super.onPreExecute();

			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mpProgress = ProgressDialog.show(mContext,
							"Downloading data",
							"Please wait for a moment...");
				}
			});
		}//onPreExecute()

		@Override
		protected String doInBackground(Void... params) {
			//Local variable declaration
			String result=null;
			// Create http client object to send request to server
			HttpClient httpclient = new DefaultHttpClient();
			// Create Request to server and get response
			HttpGet httpget = new HttpGet(webServiceLinkForTicketDetails);
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
					Log.d("tag", "ticketDetails=" + result);
					instream.close();					
				}// if

			} catch (ClientProtocolException e) {
				if (mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
			} catch (IOException e) {
				if (mpProgress.isShowing())
					mpProgress.dismiss();
				// TODO Auto-generated catch block
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
				response=result;
				
				Fragment_Tickets.tickets_handler.post(Fragment_Tickets.tickets_runnable);

			} else {
				Toast.makeText(mContext, "No response from server", 1000)
						.show();
				System.out.println("No response from server");
			}
		}

	}

	// =================================================================

	// To check network connectivity
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}