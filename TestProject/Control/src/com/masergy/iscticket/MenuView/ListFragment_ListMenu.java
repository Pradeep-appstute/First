package com.masergy.iscticket.MenuView;

import java.util.ArrayList;
import java.util.List;

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

import com.masergy.iscticket.Activity_SliderMenu;
import com.masergy.iscticket.R;
import com.masergy.iscticket.ContentView.Fragment_ContactUs;
import com.masergy.iscticket.ContentView.Fragment_DopplerIM;
import com.masergy.iscticket.ContentView.Fragment_ModifyService;
import com.masergy.iscticket.ContentView.Fragment_Tickets;
import com.masergy.iscticket.utility.Webservice_GetDopplerIMList;
import com.masergy.iscticket.utility.Webservice_GetModifyServiceList;
import com.masergy.iscticket.utility.Webservice_GetSubmitData;
import com.masergy.iscticket.utility.Webservice_GetTicketsList;
import com.masergy.iscticket.utility.Webservice_Logout;

public class ListFragment_ListMenu extends ListFragment {
	
	public final String[] titles = new String[] { "Tickets", "Modify Service", "Doppler IM", "Contact us", "Logout" };
	public static Integer[] images = { R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout };
	public static final Integer[] images_ticketsselected = { R.drawable.ic_tickets_w, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout };
	public static final Integer[] images_modifyserviceselected = { R.drawable.ic_tickets, R.drawable.ic_modifyservice_w, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout };
	public static final Integer[] images_dopplerimselected = { R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim_w, R.drawable.ic_contactus, R.drawable.ic_logout };
	public static final Integer[] images_contactusselected = { R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus_w, R.drawable.ic_logout };
	public static final Integer[] images_logoutselected = { R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout_w };
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
 	  
 		//Create adaptor and set row title and icon
 		rowItems = new ArrayList<RowItem>();
         for (int i = 0; i < titles.length; i++) {
             RowItem item = new RowItem(images[i], titles[i]);
             rowItems.add(item);
         }
         
 		listAdapter = new BaseAdapter_ListMenu(getActivity(),rowItems);
 		
 		//set adapter to list view
 		setListAdapter(listAdapter);
    }
 
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		Log.d("TAG", "Position="+position);
		//Hack
		position--;
		switch (position) {
		case 0:{
			images = images_ticketsselected;
	 		rowItems = new ArrayList<RowItem>();
	         for (int i = 0; i < titles.length; i++) {
	             RowItem item = new RowItem(images[i], titles[i]);
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
			images = images_modifyserviceselected;
	 		rowItems = new ArrayList<RowItem>();
	         for (int i = 0; i < titles.length; i++) {
	             RowItem item = new RowItem(images[i], titles[i]);
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
			images = images_dopplerimselected;
	 		rowItems = new ArrayList<RowItem>();
	         for (int i = 0; i < titles.length; i++) {
	             RowItem item = new RowItem(images[i], titles[i]);
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
			images = images_contactusselected;
	 		rowItems = new ArrayList<RowItem>();
	         for (int i = 0; i < titles.length; i++) {
	             RowItem item = new RowItem(images[i], titles[i]);
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
			images = images_logoutselected;
	 		rowItems = new ArrayList<RowItem>();
	         for (int i = 0; i < titles.length; i++) {
	             RowItem item = new RowItem(images[i], titles[i]);
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
