package com.masergy.iscticket.ContentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.masergy.iscticket.Activity_SliderMenu;
import com.masergy.iscticket.R;
import com.masergy.iscticket.utility.Send_to_Web;
import com.masergy.iscticket.utility.Webservice_GetTicketsList;

public class Fragment_Tickets extends Fragment {

	LinearLayout lin_rootview;
	ViewGroup viewgroup_submitview;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<Ticket>> listDataChild;
	List<Ticket> today;
	List<Ticket> thisWeek;
	List<Ticket> lastWeek;
	List<Ticket> last30Days;

	// Constant variable declaration
	final int OpenTab = 1, ClosedTab = 2, MaintTab = 3, SubmitTab = 4;
	// Tickets Tab
	public static ImageButton imgButtonOpen, imgButtonClosed, imgButtonMaint,
			imgButtonSubmit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// construct the RelativeLayout
		lin_rootview = (LinearLayout) inflater.inflate(
				R.layout.fragment_tickets, container, false);

		// v.setBackgroundColor(Color.RED);

		// ================================================================
		// get the listview
		expListView = (ExpandableListView) lin_rootview.findViewById(R.id.lvExp);

		// preparing list data
		prepareListData(OpenTab);
		
		
		listAdapter = new ExpandableListAdapter(Activity_SliderMenu.context,
				listDataHeader, listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
//				Toast.makeText(
//						Activity_SliderMenu.context.getApplicationContext(),
//						listDataHeader.get(groupPosition) + " Expanded",
//						Toast.LENGTH_SHORT).show();
				Log.d("tag", "groupPosition="+groupPosition);
			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
//				Toast.makeText(
//						Activity_SliderMenu.context.getApplicationContext(),
//						listDataHeader.get(groupPosition) + " Collapsed",
//						Toast.LENGTH_SHORT).show();

			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
//				Toast.makeText(
//						Activity_SliderMenu.context.getApplicationContext(),
//						listDataHeader.get(groupPosition)
//								+ " : "
//								+ listDataChild.get(
//										listDataHeader.get(groupPosition)).get(
//										childPosition), Toast.LENGTH_SHORT)
//						.show();
				return false;
			}
		});
		
		
		// ===========Tab Buttons===============
		imgButtonOpen = (ImageButton) lin_rootview.findViewById(R.id.imgButtonOpen);
		imgButtonOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				prepareListData(OpenTab);
				listAdapter.notifyDataSetInvalidated();
				//Expand the list view by default
				if(!expListView.isGroupExpanded(0))
				expListView.expandGroup(0);
				if(!expListView.isGroupExpanded(1))
				expListView.expandGroup(1);
				if(!expListView.isGroupExpanded(2))
				expListView.expandGroup(2);
				if(!expListView.isGroupExpanded(3))
				expListView.expandGroup(3);
			}
		});
		imgButtonClosed = (ImageButton) lin_rootview.findViewById(R.id.imgButtonClosed);
		imgButtonClosed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				prepareListData(ClosedTab);
				listAdapter.notifyDataSetInvalidated();
				if(!expListView.isGroupExpanded(0))
				expListView.expandGroup(0);
				if(!expListView.isGroupExpanded(1))
				expListView.expandGroup(1);
				if(!expListView.isGroupExpanded(2))
				expListView.expandGroup(2);
				if(!expListView.isGroupExpanded(3))
				expListView.expandGroup(3);
			}
		});
		imgButtonMaint = (ImageButton) lin_rootview.findViewById(R.id.imgButtonMaint);
		imgButtonMaint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				prepareListData(MaintTab);
				listAdapter.notifyDataSetInvalidated();
				if(!expListView.isGroupExpanded(0))
				expListView.expandGroup(0);
				if(!expListView.isGroupExpanded(1))
				expListView.expandGroup(1);
				if(!expListView.isGroupExpanded(2))
				expListView.expandGroup(2);
				if(!expListView.isGroupExpanded(3))
				expListView.expandGroup(3);
			}
		});
		imgButtonSubmit = (ImageButton) lin_rootview.findViewById(R.id.imgButtonSubmit);
		imgButtonSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	
				// Remove expandable list view
				((LinearLayout) lin_rootview).removeView(lin_rootview.findViewById(R.id.lvExp));
				
				// Add submitview
				viewgroup_submitview = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.submit_view, (ViewGroup) lin_rootview,false);
				((ViewGroup) lin_rootview).addView(viewgroup_submitview);
				
				SharedPreferences prefs = Activity_SliderMenu.context.getSharedPreferences(Send_to_Web.fileName, Activity_SliderMenu.context.MODE_PRIVATE);    
		        
		        String firstName = prefs.getString("firstName", "");
		        if (firstName != null) 
		        {
		        	TextView name = (TextView)viewgroup_submitview.findViewById(R.id.textViewNameValue);
		        	         name.setText(firstName);
		        }
		        
		        String email = prefs.getString("email", "");
		        if (email != null) 
		        {
		        	TextView name = (TextView)viewgroup_submitview.findViewById(R.id.textViewEmailValue);
		        	         name.setText(email);
		        }
		       
		        
		        String phone = prefs.getString("phone", "");
		        if (email != null) 
		        {
		        	TextView name = (TextView)viewgroup_submitview.findViewById(R.id.textViewPhoneValue);
		        	         name.setText(phone);
		        }
		        
			}
		});
		// =====================================

		return lin_rootview;
	}

	private void init() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData(int tabName) {

		// start===initialization=================================
		if (listDataHeader == null)
			listDataHeader = new ArrayList<String>();
		if (listDataChild == null)
			listDataChild = new HashMap<String, List<Ticket>>();

		if (listDataHeader != null && listDataChild != null) {
			if (listDataHeader.size() == 0 && listDataChild.size() == 0)
			{
				listDataHeader.clear();
				// Adding child data
		    listDataHeader.add("Today");
			listDataHeader.add("This Week");
			listDataHeader.add("Last Week");
			listDataHeader.add("Last 30 days");
			}
		}
		
		if (today == null)
			today = new ArrayList<Ticket>();
		if (thisWeek == null)
			thisWeek = new ArrayList<Ticket>();
		if (lastWeek == null)
			lastWeek = new ArrayList<Ticket>();
		if (last30Days == null)
			last30Days = new ArrayList<Ticket>();
		// end===initialization=================================

		if (tabName == OpenTab) {
			today.clear();
			thisWeek.clear();
			lastWeek.clear();
			last30Days.clear();

			if (Webservice_GetTicketsList.open_todaysTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.open_todaysTicketList
						.size();
				for (int i = 0; i < len; i++) {
					today.add(Webservice_GetTicketsList.open_todaysTicketList
							.get(i));
				}
			}

			if (Webservice_GetTicketsList.open_currentWeekTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.open_currentWeekTicketList
						.size();
				for (int i = 0; i < len; i++) {
					thisWeek.add(Webservice_GetTicketsList.open_currentWeekTicketList
							.get(i));
				}
			}

			if (Webservice_GetTicketsList.open_lastWeekTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.open_lastWeekTicketList
						.size();
				for (int i = 0; i < len; i++) {
					lastWeek.add(Webservice_GetTicketsList.open_lastWeekTicketList
							.get(i));
				}
			}

			if (Webservice_GetTicketsList.open_currentMonthTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.open_currentMonthTicketList
						.size();
				for (int i = 0; i < len; i++) {
					last30Days
							.add(Webservice_GetTicketsList.open_currentMonthTicketList
									.get(i));
				}
			}
		} else if (tabName == ClosedTab) {

			today.clear();
			thisWeek.clear();
			lastWeek.clear();
			last30Days.clear();

			if (Webservice_GetTicketsList.closed_todaysTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.closed_todaysTicketList
						.size();
				for (int i = 0; i < len; i++) {
					today.add(Webservice_GetTicketsList.closed_todaysTicketList
							.get(i));
				}
			}

			if (Webservice_GetTicketsList.closed_currentWeekTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.closed_currentWeekTicketList
						.size();
				for (int i = 0; i < len; i++) {
					thisWeek.add(Webservice_GetTicketsList.closed_currentWeekTicketList
							.get(i));
				}
			}

			if (Webservice_GetTicketsList.closed_lastWeekTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.closed_lastWeekTicketList
						.size();
				for (int i = 0; i < len; i++) {
					lastWeek.add(Webservice_GetTicketsList.closed_lastWeekTicketList
							.get(i));
				}
			}

			if (Webservice_GetTicketsList.closed_currentMonthTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.closed_currentMonthTicketList
						.size();
				for (int i = 0; i < len; i++) {
					last30Days
							.add(Webservice_GetTicketsList.closed_currentMonthTicketList
									.get(i));
				}
			}

		} else if (tabName == MaintTab) {

			today.clear();
			thisWeek.clear();
			lastWeek.clear();
			last30Days.clear();

			if (Webservice_GetTicketsList.maint_todaysTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.maint_todaysTicketList
						.size();
				for (int i = 0; i < len; i++) {
					today.add(Webservice_GetTicketsList.maint_todaysTicketList
							.get(i));
				}
			}

			if (Webservice_GetTicketsList.maint_currentWeekTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.maint_currentWeekTicketList
						.size();
				for (int i = 0; i < len; i++) {
					thisWeek.add(Webservice_GetTicketsList.maint_currentWeekTicketList
							.get(i));
				}
			}

			if (Webservice_GetTicketsList.maint_lastWeekTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.maint_lastWeekTicketList
						.size();
				for (int i = 0; i < len; i++) {
					lastWeek.add(Webservice_GetTicketsList.maint_lastWeekTicketList
							.get(i));
				}
			}

			if (Webservice_GetTicketsList.maint_currentMonthTicketList.size() > 0) {
				int len = Webservice_GetTicketsList.maint_currentMonthTicketList
						.size();
				for (int i = 0; i < len; i++) {
					last30Days
							.add(Webservice_GetTicketsList.maint_currentMonthTicketList
									.get(i));
				}
			}
		} else if (tabName == SubmitTab) {

		}

		listDataChild.clear();
		listDataChild.put(listDataHeader.get(0), today); // Header, Child data
		listDataChild.put(listDataHeader.get(1), thisWeek);
		listDataChild.put(listDataHeader.get(2), lastWeek);
		listDataChild.put(listDataHeader.get(3), last30Days);
		
//		Log.d("tag", "listDataChild size="+listDataChild.size());
//		Log.d("tag", "listDataHeader size="+listDataHeader.size());
	}
}
