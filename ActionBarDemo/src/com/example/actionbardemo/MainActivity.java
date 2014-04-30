package com.example.actionbardemo;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//------------------------------------------
		// Set title and subtitle of action bar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Title - Adobe");
		actionBar.setSubtitle("SubTitle - Adobe ");
		//------------------------------------------
		
		
		
		//---add the custom view to the action bar---
	    actionBar.setCustomView(R.layout.searchbar);
	    EditText search = (EditText) actionBar.getCustomView().findViewById(R.id.searchfield);
	    search.setOnEditorActionListener(new OnEditorActionListener() {

	      @Override
	      public boolean onEditorAction(TextView v, int actionId,
	          KeyEvent event) {
	        Toast.makeText(MainActivity.this, "Search triggered",
	            Toast.LENGTH_LONG).show();
	        return false;
	      }
	    });
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
	    //------------------------------------------
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		
		case R.id.id_action_email:
			Toast.makeText(this, "Action_Email", Toast.LENGTH_LONG).show();
			break;
		case R.id.id_action_info:
			Toast.makeText(this, "Action_Info", Toast.LENGTH_LONG).show();
			break;
		case R.id.id_action_settings:
			Toast.makeText(this, "Action_Settings", Toast.LENGTH_LONG).show();
			break;
		
		}
		return true;
	}
}
