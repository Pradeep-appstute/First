package com.masergy.iscControl.MenuView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.masergy.iscControl.Activity_Home;
import com.masergy.iscControl.Activity_SliderMenu;
import com.masergy.iscControl.R;
import com.masergy.iscControl.ContentView.Fragment_ContactUs;
import com.masergy.iscControl.ContentView.Fragment_DopplerIM;
import com.masergy.iscControl.ContentView.Fragment_ModifyService;
import com.masergy.iscControl.ContentView.Fragment_Tickets;
import com.masergy.iscControl.utility.Webservice_GetDopplerIMList;
import com.masergy.iscControl.utility.Webservice_GetModifyServiceList;
import com.masergy.iscControl.utility.Webservice_GetSubmitData;
import com.masergy.iscControl.utility.Webservice_GetTicketsList;
import com.masergy.iscControl.utility.Webservice_Logout;

public class ListFragment_ListMenu extends ListFragment {
	
//	public final String[] titles = new String[] { "Tickets", "Modify Service", "Doppler IM", "Contact us", "Logout" };
//	public static Integer[] images = { R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout };
	public static List<String> menuListLabel=new ArrayList<String>();
	public static List<Integer> menuListImages =new ArrayList<Integer>();

	
//	public static final List<Integer> images_ticketsselected = new ArrayList<Integer>(Arrays.asList(new Integer[]{ R.drawable.ic_tickets_w, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout }));
//	public static final List<Integer> images_modifyserviceselected = new ArrayList<Integer>(Arrays.asList(new Integer[]{ R.drawable.ic_tickets, R.drawable.ic_modifyservice_w, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout }));
//	public static final List<Integer> images_dopplerimselected = new ArrayList<Integer>(Arrays.asList(new Integer[]{ R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim_w, R.drawable.ic_contactus, R.drawable.ic_logout }));
//	public static final List<Integer> images_contactusselected = new ArrayList<Integer>(Arrays.asList(new Integer[]{ R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus_w, R.drawable.ic_logout }));
//	public static final List<Integer> images_logoutselected = new ArrayList<Integer>(Arrays.asList(new Integer[]{ R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout_w }));
	List<RowItem> rowItems;
	BaseAdapter_ListMenu listAdapter;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.listfragment_listmenu, container, false);
    }
 
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
 	   TextView tv_listTitle = new TextView(getActivity());
 	   tv_listTitle.setText(getString(R.string.textViewListHeaderMSG));
 	   tv_listTitle.setTextSize(30);
 	   tv_listTitle.setTextColor(Color.WHITE);
 	   tv_listTitle.setTypeface(null, Typeface.BOLD);
 	   tv_listTitle.setBackgroundColor(Color.BLACK);
 	   tv_listTitle.setGravity(Gravity.CENTER_HORIZONTAL);
 	   getListView().addHeaderView(tv_listTitle);
 	  
//		menuListLabel = new ArrayList<String>();
//		menuListImages = new ArrayList<Integer>();
		
//		SharedPreferences prefs = Activity_SliderMenu.context.getSharedPreferences("Login", Activity_SliderMenu.context.MODE_PRIVATE);
//		
//		if(prefs.getString("permViewTicket", "true").equals("true") || prefs.getString("permSubmitTicket", "true").equals("true"))
//		{
//			menuListLabel.add("Tickets");
//			menuListImages.add(R.drawable.ic_tickets);
//		}
//		     
//		if(prefs.getString("permModifyTierNetworkAccess", "true").equals("true"))
//		{
//			menuListLabel.add("Modify Service");
//			menuListImages.add(R.drawable.ic_modifyservice);
//		}
//		if(prefs.getString("permViewServiceDetails", "true").equals("true"))
//		{
//			menuListLabel.add("Doppler IM");
//			menuListImages.add(R.drawable.ic_dopplerim);
//		}
//		
//		menuListLabel.add("Contact us");
//		menuListImages.add(R.drawable.ic_contactus);
//		
//		menuListLabel.add("Logout");
//		menuListImages.add(R.drawable.ic_logout);
		
 		//Create adaptor and set row title and icon         
  		rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < menuListLabel.size(); i++) {
//        	Log.d("tag", "imagesID="+menuListImages.get(i));
             RowItem item = new RowItem(menuListImages.get(i), menuListLabel.get(i));
             rowItems.add(item);
        }
         
 		listAdapter = new BaseAdapter_ListMenu(getActivity(),rowItems);
 		
 		//set adapter to list view
 		setListAdapter(listAdapter);
    }
 
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
//		Log.d("TAG", "Position="+position);
		//Hack
		position--;
		switch (position) {
		case 0:{
			menuListImages = Activity_Home.images_ticketsselected;
	  		rowItems = new ArrayList<RowItem>();
	        for (int i = 0; i < menuListLabel.size(); i++) {
	             RowItem item = new RowItem(menuListImages.get(i), menuListLabel.get(i));
	             rowItems.add(item);
	        }
	  		listAdapter = new BaseAdapter_ListMenu(getActivity(),rowItems);
	 		
	 		//set adapter to list view
	 		setListAdapter(listAdapter);
			
		    Webservice_GetSubmitData instance_submit = new Webservice_GetSubmitData(Activity_SliderMenu.context);
		    instance_submit.postData();
		    Webservice_GetTicketsList instance = new Webservice_GetTicketsList(Activity_SliderMenu.context);
		    instance.postData();
			newContent = new Fragment_Tickets();
			}
			break;
		case 1:
		{
			menuListImages = Activity_Home.images_modifyserviceselected;
	  		rowItems = new ArrayList<RowItem>();
	        for (int i = 0; i < menuListLabel.size(); i++) {
	             RowItem item = new RowItem(menuListImages.get(i), menuListLabel.get(i));
	             rowItems.add(item);
	        }
	  		listAdapter = new BaseAdapter_ListMenu(getActivity(),rowItems);
	 		
	 		//set adapter to list view
	 		setListAdapter(listAdapter);
			
			Webservice_GetModifyServiceList instance_submit = new Webservice_GetModifyServiceList(Activity_SliderMenu.context);
		    instance_submit.postData();
		   
		    newContent = new Fragment_ModifyService();

			   }
			break;
		case 2:{
			menuListImages = Activity_Home.images_dopplerimselected;
	  		rowItems = new ArrayList<RowItem>();
	        for (int i = 0; i < menuListLabel.size(); i++) {
	             RowItem item = new RowItem(menuListImages.get(i), menuListLabel.get(i));
	             rowItems.add(item);
	        }
		  		listAdapter = new BaseAdapter_ListMenu(getActivity(),rowItems);
		 		
		 		//set adapter to list view
		 		setListAdapter(listAdapter);
			
			Webservice_GetDopplerIMList instance_submit = new Webservice_GetDopplerIMList(Activity_SliderMenu.context);
		    instance_submit.postData();
		   
		    newContent = new Fragment_DopplerIM();
			   }
			break;
		case 3:{
			menuListImages = Activity_Home.images_contactusselected;
	  		rowItems = new ArrayList<RowItem>();
	        for (int i = 0; i < menuListLabel.size(); i++) {
	             RowItem item = new RowItem(menuListImages.get(i), menuListLabel.get(i));
	             rowItems.add(item);
	        }
		  		listAdapter = new BaseAdapter_ListMenu(getActivity(),rowItems);
		 		
		 		//set adapter to list view
		 		setListAdapter(listAdapter);
			newContent = new Fragment_ContactUs();
			}
			break;
		case 4:{
			//newContent = new Activity_Fragment(4);
			menuListImages = Activity_Home.images_logoutselected;
	  		rowItems = new ArrayList<RowItem>();
	        for (int i = 0; i < menuListLabel.size(); i++) {
	             RowItem item = new RowItem(menuListImages.get(i), menuListLabel.get(i));
	             rowItems.add(item);
	        }
		  		listAdapter = new BaseAdapter_ListMenu(getActivity(),rowItems);
		 		
		 		//set adapter to list view
		 		setListAdapter(listAdapter);
		 		
		    Webservice_Logout instance = new Webservice_Logout(Activity_SliderMenu.context);
		    instance.postData();
		    }
			break;
		}
		
		if (newContent != null)
		{
		    FragmentTransaction ft = getFragmentManager().beginTransaction();
		    ft.replace(R.id.activity_main_content_fragment, newContent);
		    ft.commit();
		    
		    //Toggle sliding menu
		    Activity_SliderMenu.slidingMenu.showContent();
		}
			
	}//onListItemClick
}
