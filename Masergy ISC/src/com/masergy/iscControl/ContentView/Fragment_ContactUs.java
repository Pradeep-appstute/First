package com.masergy.iscControl.ContentView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.masergy.iscControl.Activity_SliderMenu;
import com.masergy.iscControl.R;
import com.masergy.iscControl.utility.CommonResources;


public class Fragment_ContactUs extends Fragment {

	LinearLayout lin_rootview;
    private WebView webview;
    private static final String TAG = "Main";
    private ProgressDialog mpProgress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// construct the RelativeLayout
		lin_rootview = (LinearLayout) inflater.inflate(
				R.layout.fragment_contactus, container, false);
		lin_rootview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
               		    InputMethodManager.HIDE_NOT_ALWAYS);
				return true;
			}
		});
		
		// ==========Menu Title============
		TextView menu_title = ((TextView) lin_rootview
				.findViewById(R.id.activity_main_content_title));
		menu_title.setText("Contact Us");

		// ===========Menu Button===============
		Button toggleMenuButton = ((Button) lin_rootview
				.findViewById(R.id.activity_main_content_button_menu));
		toggleMenuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity_SliderMenu.slidingMenu.toggle();
				InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
               		    InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});

		// ===========Web View===============
		this.webview = (WebView) lin_rootview.findViewById(R.id.webViewContactUs);
		final AlertDialog alertDialog = new AlertDialog.Builder(Activity_SliderMenu.context).create();
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		((Activity) Activity_SliderMenu.context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mpProgress = ProgressDialog.show(Activity_SliderMenu.context,
						"Loading page",
						"Please wait for a moment...");
			}
		});

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                
                if (url.startsWith("tel:")) { 
                    Intent intent = new Intent(Intent.ACTION_DIAL,
                            Uri.parse(url)); 
                    startActivity(intent); 
                    return true; // we handled the url loading
            }
                else if (url.startsWith("mailto:")) { 
                    try {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.parse(url));
                        emailIntent.setType("message/rfc822");//u can also try to setType(message/rfc822); might be it will show only mail client installed on your device
                        String recipient = url.substring( url.indexOf(":")+1 );
//                        if (TextUtils.isEmpty(recipient)) recipient = "csc@masergy.com";
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"csc@masergy.com"});
                        
//                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{recipient});
//                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mContext.getString(R.string.email_subject));
//                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mContext.getString(R.string.email_message, " "));

                        Activity_SliderMenu.context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
                        return true; // we handled the url loading
                    }
                    catch (Exception ex) {}
            }
                else if(url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
                return true; // we handled the url loading
            }
            return false; // let WebView handle this event
                
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " +url);
                if (mpProgress.isShowing()) {
                	mpProgress.dismiss();
                }
            }

            @SuppressWarnings("deprecation")
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                Toast.makeText(Activity_SliderMenu.context, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });
        
        if(isNetworkAvailable())
		{
        	String webServiceLink =CommonResources.prefixLinkContactUs+"contact_us.html";
        	webview.loadUrl(webServiceLink);
		}
		else
		{
			Toast.makeText(Activity_SliderMenu.context, "No network availble", 1000).show();
		}
        
		// =====================================

		return lin_rootview;
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) Activity_SliderMenu.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
