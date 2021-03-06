package com.masergy.iscControl.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {

	/*
	 To get getActiveNetworkInfo() to work you need to add the following to the manifest.
		1. uses-permission android:name="android.permission.INTERNET"
 		2. uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
	 */
	
	/*
	public static boolean isAvailable(Context context) {
	    boolean status=false;
	    try{
	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo netInfo = cm.getNetworkInfo(0);
	        if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
	            status= true;
	        }else {
	            netInfo = cm.getNetworkInfo(1);
	            if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
	                status= true;
	        }
	    }catch(Exception e){
	        e.printStackTrace();  
	        return false;
	    }
	    return status;

	    } 
	*/
	
	@SuppressWarnings("unused")
	public
	static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
