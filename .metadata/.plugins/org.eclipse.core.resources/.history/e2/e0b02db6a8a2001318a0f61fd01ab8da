package com.masergy.iscticket;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.masergy.iscticket.ContentView.Fragment_Tickets;
import com.masergy.iscticket.MenuView.ListFragment_ListMenu;
import com.masergy.iscticket.utility.Webservice_GetSubmitData;
import com.masergy.iscticket.utility.Webservice_GetTicketsList;
import com.masergy.iscticket.utility.Webservice_Logout;

public class Activity_SliderMenu extends SlidingFragmentActivity {

	//Variable declaration
	public static SlidingMenu slidingMenu;
	public static Context context;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState); 
	    //Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//	    requestWindowFeature(Window.FEATURE_LEFT_ICON); 
	    setContentView(R.layout.contentview);
	    //Set the layout resource to use for the custom title
//	     
	    
	    
	    setBehindContentView(R.layout.menuview);
	 
//	    setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.img_menu);
//	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
	    
	    context = Activity_SliderMenu.this;
	    
	    slidingMenu = getSlidingMenu();
	    slidingMenu.setMode(SlidingMenu.LEFT);
	    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	    slidingMenu.setFadeDegree(0.35f);
	    slidingMenu.setBehindWidth(400);
	    slidingMenu.setShadowDrawable(R.drawable.shadow);
	    slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
	    slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	    slidingMenu.setBehindOffset(100);
//	    slidingMenu.setBehindWidth(800);
//	    slidingMenu.setBehindWidth(480); //Give it equal to screen width
	    
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    ft.add(R.id.linlayout_menuview, new ListFragment_ListMenu());
	    ft.commit();
	    
	    Bundle bundle=this.getIntent().getExtras();
	    String selectedlistitem=bundle.getString("selectedlistitem");
	    if(selectedlistitem.equalsIgnoreCase("Tickets"))
	    {
	    	Webservice_GetSubmitData instance_submit = new Webservice_GetSubmitData(Activity_SliderMenu.context);
		    instance_submit.postData();
		    Webservice_GetTicketsList instance = new Webservice_GetTicketsList(Activity_SliderMenu.context);
		    instance.postData();
		    ft = getSupportFragmentManager().beginTransaction();
		    Fragment_Tickets newContent = new Fragment_Tickets();
		    ft.replace(R.id.activity_main_content_fragment, newContent);
		    ft.commit();

		    //Toggle sliding menu
		    Activity_SliderMenu.slidingMenu.showContent();
	    }
	    else if(selectedlistitem.equalsIgnoreCase("Modify Service"))
	    {

	    }
	    else if(selectedlistitem.equalsIgnoreCase("Doppler IM"))
	    {

	    }
	    else if(selectedlistitem.equalsIgnoreCase("Contact us"))
	    {

	    }
	    else if(selectedlistitem.equalsIgnoreCase("Logout"))
	    {
	    	//This will never happen because on tap of logout on Activity_Home, application will logout
	    }
	}//onCreate
}

