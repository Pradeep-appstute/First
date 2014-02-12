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


public class Send_to_Web {
	
	SharedPreferences.Editor sharedPrefEditor;
	
	String webServiceLink ="https://webservice-dev.masergy.com/webservices_mobile/rest/v1/auth";
	
	String name,password;
	Context mContext;
	ProgressDialog mpProgress;
	SharedPreferences sharedPreferences;
	Editor editor;
	

	public Send_to_Web(Context context,String name,String password) {
		this.name=name;
		this.password=password;
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
					mpProgress= ProgressDialog.show(mContext, "Logging in", "Please wait for a moment...");
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
			         
			try {
			
				//Prepare string entity using JSON string to be posted to server	
				      JSONObject jsonObj = new JSONObject();
				           try {
							jsonObj.put("username", name);
							jsonObj.put("password", password);
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
			// TODO Auto-generated method stub
			if(mpProgress.isShowing())
				mpProgress.dismiss();
			super.onPostExecute(result);
			if (result!=null) {

				Toast.makeText(mContext, "Response-"+result, 1000).show();
				System.out.println("Response="+result);
				
				try {
					JSONObject jObj = new JSONObject(result);
					String authToken = jObj.getString("authToken");
					String contactId = jObj.getString("contactId");
					String userId = jObj.getString("userId");
					String firstName = jObj.getString("firstName");
					String lastName = jObj.getString("lastName");
					String email = jObj.getString("email");
					String phone = jObj.getString("phone");
					String permViewTicket = jObj.getString("permViewTicket");
					String permSubmitTicket = jObj.getString("permSubmitTicket");
					String permViewServiceDetails = jObj.getString("permViewServiceDetails");
					String permModifyTierNetworkAccess = jObj.getString("permModifyTierNetworkAccess");
					String permViewVnoc = jObj.getString("permViewVnoc");
					String custId = jObj.getString("custId");
					String custName = jObj.getString("custName");
					/*
					System.out.println(authToken);
					System.out.println(contactId);
					System.out.println(userId);
					System.out.println(firstName);
					*/
					
					editor.putString("authToken", authToken);
					editor.putString("contactId", contactId);
					editor.putString("userId", userId);
					editor.putString("firstName", firstName);
					editor.putString("lastName", lastName);
					editor.putString("email", email);
					editor.putString("phone", phone);
					editor.putString("permViewTicket", permViewTicket);
					editor.putString("permSubmitTicket", permSubmitTicket);
					editor.putString("permViewServiceDetails", permViewServiceDetails);
					editor.putString("permModifyTierNetworkAccess", permModifyTierNetworkAccess);
					editor.putString("permViewVnoc", permViewVnoc);
					editor.putString("custId", custId);
					editor.putString("custName", custName);
					editor.commit();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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


}