package com.masergy.iscticket.ContentView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.masergy.iscticket.Activity_SliderMenu;
import com.masergy.iscticket.R;


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
                view.loadUrl(url);
                return true;
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
        	webview.loadUrl("https://webservice.masergy.com/webservices_mobile/contact_us.html");
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
