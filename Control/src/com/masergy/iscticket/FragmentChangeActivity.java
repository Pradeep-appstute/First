package com.masergy.iscticket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.masergy.iscticket.slidermenu.Activity_Fragment;
import com.masergy.iscticket.slidermenu.ListMenuFragment;

public class FragmentChangeActivity extends com.masergy.iscticket.slidermenu.BaseActivity {
	
	private Fragment mFragment;
	
	public FragmentChangeActivity() {
		super(R.string.emptystring);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		if (savedInstanceState != null)
			mFragment = getSupportFragmentManager().getFragment(savedInstanceState, "mFragment");
		
		if (mFragment == null)
			mFragment = new Activity_Fragment(R.color.red);	
		
		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mFragment)
		.commit();
		
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new ListMenuFragment())
		.commit();
		
		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mFragment", mFragment);
	}
	
	public void switchContent(Fragment fragment) {
		mFragment = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}

}
