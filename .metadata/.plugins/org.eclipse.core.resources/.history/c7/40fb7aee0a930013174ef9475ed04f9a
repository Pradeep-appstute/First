package com.masergy.iscticket;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.masergy.iscticket.MenuView.ListFragment_ListMenu;

public class Activity_SliderMenu extends SlidingFragmentActivity {

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);        
	    setContentView(R.layout.activity_slidermenu);
	    setBehindContentView(R.layout.menu_container);
	 
	    SlidingMenu slidingmenu = getSlidingMenu();
	    slidingmenu.setMode(SlidingMenu.LEFT);
	    slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	    slidingmenu.setFadeDegree(0.35f);
	    slidingmenu.setBehindWidth(400);
	    slidingmenu.setShadowDrawable(R.drawable.shadow);
	    slidingmenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingmenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    ft.add(R.id.fragment_menu_container, new ListFragment_ListMenu());
	    ft.commit();
	}//onCreate
	
}

