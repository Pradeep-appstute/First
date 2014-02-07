package com.masergy.iscticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	public void loginButtonTapped(View v)
	{
		Intent intentHome;
		intentHome = new Intent(this, FragmentChangeActivity.class);
		startActivity(intentHome);
	}
}
