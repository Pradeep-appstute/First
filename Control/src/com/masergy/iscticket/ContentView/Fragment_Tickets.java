package com.masergy.iscticket.ContentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.masergy.iscticket.Activity_Login;
import com.masergy.iscticket.Activity_SliderMenu;
import com.masergy.iscticket.R;
import com.masergy.iscticket.utility.Send_to_Web;
import com.masergy.iscticket.utility.Webservice_GetTicketsList;
import com.masergy.iscticket.utility.Webservice_PostSubmitData;

public class Fragment_Tickets extends Fragment {

	boolean isSubmitTapped;
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

	//Submit
	String txt_subject, txt_bundleid, txt_description;
	Spinner spinner_bundle, spinner_subject;
	EditText editTextDescription;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isSubmitTapped =false;
		// construct the RelativeLayout
		lin_rootview = (LinearLayout) inflater.inflate(
				R.layout.fragment_tickets, container, false);

		// v.setBackgroundColor(Color.RED);
		
		// get the listview
		expListView = (ExpandableListView) lin_rootview.findViewById(R.id.lvExp);

		// preparing list data
		prepareListData(OpenTab);
		// load list view
		initExpandableListView();		
		
		// ==========Menu Title============
		TextView menu_title = ((TextView) lin_rootview.findViewById(R.id.activity_main_content_title));
			menu_title.setText("Tickets");
			
		// ===========Menu Button===============
		Button toggleMenuButton = ((Button) lin_rootview.findViewById(R.id.activity_main_content_button_menu));
		       toggleMenuButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						Activity_SliderMenu.slidingMenu.toggle();
				}
		});
		
		// ===========Tab Buttons===============
		imgButtonOpen = (ImageButton) lin_rootview.findViewById(R.id.imgButtonOpen);
		imgButtonOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				prepareListData(OpenTab);
				initExpandableListView();
				//listAdapter.notifyDataSetInvalidated();
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
				initExpandableListView();
				//listAdapter.notifyDataSetInvalidated();
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
				initExpandableListView();
				//listAdapter.notifyDataSetInvalidated();
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
				isSubmitTapped=true;
				// Remove expandable list view
				((LinearLayout) lin_rootview).removeView(lin_rootview.findViewById(R.id.lvExp));
				
				// Add submitview
				viewgroup_submitview = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.submit_view, (ViewGroup) lin_rootview,false);
				((ViewGroup) lin_rootview).addView(viewgroup_submitview);
				
				SharedPreferences prefs = Activity_SliderMenu.context.getSharedPreferences(Send_to_Web.fileName, Activity_SliderMenu.context.MODE_PRIVATE);    
		        
		        String firstName = prefs.getString("firstName", "");
		        if (firstName != null) 
		        {
		        	TextView tv_name = (TextView)viewgroup_submitview.findViewById(R.id.textViewNameValue);
		        	         tv_name.setText(firstName);
		        }
		        
		        String email = prefs.getString("email", "");
		        if (email != null) 
		        {
		        	TextView tv_email = (TextView)viewgroup_submitview.findViewById(R.id.textViewEmailValue);
		        	         tv_email.setText(email);
		        }
		       
		        
		        String phone = prefs.getString("phone", "");
		        if (email != null) 
		        {
		        	TextView tv_phone = (TextView)viewgroup_submitview.findViewById(R.id.textViewPhoneValue);
		        	         tv_phone.setText(phone);
		        }
		        else{
		        	TextView tv_phone = (TextView)viewgroup_submitview.findViewById(R.id.textViewPhoneValue);
		        	tv_phone.setText("");
		        }
   	         
		        //Read JSON string array and populate spinner
		        spinner_bundle = (Spinner)viewgroup_submitview.findViewById(R.id.spinnerBundle);
		        
		        addItemsOnSpinnerBundle(spinner_bundle, prefs);
		        spinner_bundle.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int pos, long id) {
						// TODO Auto-generated method stub
						//Toast.makeText(parent.getContext(), "spinner Bundle", Toast.LENGTH_SHORT).show();
						txt_bundleid = spinner_bundle.getSelectedItem().toString();
						//Toast.makeText(parent.getContext(), "txt_bundleid="+txt_bundleid,Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});

		        spinner_subject = (Spinner)viewgroup_submitview.findViewById(R.id.spinnerSubject);
		        addItemsOnSpinnerSubject(spinner_subject, prefs);	
		        spinner_subject.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int pos, long id) {
						//Toast.makeText(parent.getContext(), "spinner Subject",Toast.LENGTH_SHORT).show();
						txt_subject = spinner_subject.getSelectedItem().toString();
						//Toast.makeText(parent.getContext(), "txt_subject="+txt_subject,Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
					});
		        
		        editTextDescription = (EditText) viewgroup_submitview.findViewById(R.id.editTextDescription);
		        ImageButton btnCreateTicket = (ImageButton) viewgroup_submitview.findViewById(R.id.btnCreateTicket);
		               btnCreateTicket.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							txt_description = editTextDescription.getText().toString();
							Webservice_PostSubmitData webservicePostSubmitData = new Webservice_PostSubmitData(Activity_SliderMenu.context, txt_subject, txt_bundleid, txt_description);
							webservicePostSubmitData.postData();
						}
					});
			}


		});
		// =====================================

		return lin_rootview;
	}

	 
	private void initExpandableListView() {
		
		if(isSubmitTapped==true)
		{
			isSubmitTapped=false;
		// Remove submit view
		((LinearLayout) lin_rootview).removeView(viewgroup_submitview);
		
		// Add list view
		((ViewGroup) lin_rootview).addView(expListView);
		
		}
		
		
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
		
	}//init()

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

    private void addItemsOnSpinnerSubject(Spinner subject_spinner, SharedPreferences prefs) {
		
    	
		List<String> list = new ArrayList<String>();
        try {
            JSONArray jsonArraySubject = new JSONArray(prefs.getString("subjects", "[]"));
            for (int i = 0; i < jsonArraySubject.length(); i++) {
            	list.add((String) jsonArraySubject.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Activity_SliderMenu.context, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subject_spinner.setAdapter(dataAdapter);
	}

	private void addItemsOnSpinnerBundle(Spinner bundle_spinner, SharedPreferences prefs) {
		List<String> list = new ArrayList<String>();
        try {
            JSONArray jsonArraySubject = new JSONArray(prefs.getString("bundles", "[]"));
            for (int i = 0; i < jsonArraySubject.length(); i++) {
            	list.add((String) jsonArraySubject.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Activity_SliderMenu.context, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bundle_spinner.setAdapter(dataAdapter);
		
	}
}
