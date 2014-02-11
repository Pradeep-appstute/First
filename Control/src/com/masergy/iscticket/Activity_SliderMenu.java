package com.masergy.iscticket;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.masergy.iscticket.MenuView.ListFragment_ListMenu;

public class Activity_SliderMenu extends SlidingFragmentActivity {

	//Variable declaration
	public static SlidingMenu slidingMenu;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);        
	    setContentView(R.layout.contentview);
	    setBehindContentView(R.layout.menuview);
	 
	    slidingMenu = getSlidingMenu();
	    slidingMenu.setMode(SlidingMenu.LEFT);
	    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	    slidingMenu.setFadeDegree(0.35f);
	    slidingMenu.setBehindWidth(400);
	    slidingMenu.setShadowDrawable(R.drawable.shadow);
	    slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
	    slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	    
	    
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    ft.add(R.id.linlayout_menuview, new ListFragment_ListMenu());
	    ft.commit();
	}//onCreate
}

