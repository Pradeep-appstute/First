package com.masergy.iscticket.ContentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.masergy.iscticket.Activity_SliderMenu;
import com.masergy.iscticket.R;
import com.masergy.iscticket.utility.Webservice_GetTicketsList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class Fragment_Tickets extends Fragment {
	
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<Ticket>> listDataChild;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// construct the RelativeLayout
		LinearLayout v = (LinearLayout) inflater.inflate(
				R.layout.fragment_tickets, container, false);

		// v.setBackgroundColor(Color.RED);
		
		
		
		//================================================================
		// get the listview
		expListView = (ExpandableListView) v.findViewById(R.id.lvExp);

		// preparing list data
		prepareListData();

		listAdapter = new ExpandableListAdapter(Activity_SliderMenu.context, listDataHeader, listDataChild);

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
				Toast.makeText(Activity_SliderMenu.context.getApplicationContext(),
						listDataHeader.get(groupPosition) + " Expanded",
						Toast.LENGTH_SHORT).show();
			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				Toast.makeText(Activity_SliderMenu.context.getApplicationContext(),
						listDataHeader.get(groupPosition) + " Collapsed",
						Toast.LENGTH_SHORT).show();

			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(
						Activity_SliderMenu.context.getApplicationContext(),
						listDataHeader.get(groupPosition)
								+ " : "
								+ listDataChild.get(
										listDataHeader.get(groupPosition)).get(
										childPosition), Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});
		//================================================================
		
		
		

		return v;
	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<Ticket>>();

		// Adding child data
		listDataHeader.add("Today");
		listDataHeader.add("This Week");
		listDataHeader.add("Last Week");
		listDataHeader.add("Last 30 days");

		// Adding child data
		List<Ticket> today = new ArrayList<Ticket>();
		
		if (Webservice_GetTicketsList.open_todaysTicketList.size()>0)
		{
			int len = Webservice_GetTicketsList.open_todaysTicketList.size(); 
			for(int i=0; i < len; i++)
			{
				today.add(Webservice_GetTicketsList.open_todaysTicketList.get(i));
			}	
		}
		

		List<Ticket> thisWeek = new ArrayList<Ticket>();
		if (Webservice_GetTicketsList.open_currentWeekTicketList.size()>0)
		{
			int len = Webservice_GetTicketsList.open_currentWeekTicketList.size(); 
			for(int i=0; i < len; i++)
			{
				today.add(Webservice_GetTicketsList.open_currentWeekTicketList.get(i));
			}	
		}
		
		
		List<Ticket> lastWeek = new ArrayList<Ticket>();
		if (Webservice_GetTicketsList.open_lastWeekTicketList.size()>0)
		{
			int len = Webservice_GetTicketsList.open_lastWeekTicketList.size(); 
			for(int i=0; i < len; i++)
			{
				today.add(Webservice_GetTicketsList.open_lastWeekTicketList.get(i));
			}	
		}
		
		List<Ticket> last30Days = new ArrayList<Ticket>();
		if (Webservice_GetTicketsList.open_currentMonthTicketList.size()>0)
		{
			int len = Webservice_GetTicketsList.open_currentMonthTicketList.size(); 
			for(int i=0; i < len; i++)
			{
				today.add(Webservice_GetTicketsList.open_currentMonthTicketList.get(i));
			}	
		}


		listDataChild.put(listDataHeader.get(0), today); // Header, Child data
		listDataChild.put(listDataHeader.get(1), thisWeek);
		listDataChild.put(listDataHeader.get(2), lastWeek);
		listDataChild.put(listDataHeader.get(3), last30Days);
	}
}
