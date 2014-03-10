package com.masergy.iscticket.ContentView;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.masergy.iscticket.Activity_SliderMenu;
import com.masergy.iscticket.R;
import com.masergy.iscticket.utility.Send_to_Web;
import com.masergy.iscticket.utility.Webservice_GetTicketDetails;
//import com.masergy.iscticket.utility.Webservice_GetTicketDetails;
import com.masergy.iscticket.utility.Webservice_GetTicketsList;
import com.masergy.iscticket.utility.Webservice_PostSubmitData;
//import com.masergy.iscticket.utility.Webservice_GetTicketDetails.post_data;

public class Fragment_Tickets extends Fragment {

	//To resolve issue of changing tabs
	static boolean isSubmitTapped;//Temporary declared static, later save in shared pref. 
	LinearLayout lin_rootview;
	ViewGroup viewgroup_submitview,viewgroup_ticketdetails_view;
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
	
	public static Handler tickets_handler;
	public static Runnable tickets_runnable;
	
	
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
		// =================HANDLER====================
		tickets_handler = new Handler();
		tickets_runnable = new Runnable() {
			
			@Override
			public void run() {
				
				   /*
			    {
						"ticketId": 207935,
						"subject": "MB095663 / Acme Labs / Irving, TX / Outage",
						"status": "Open",
						"createDate": 1393823842000,
						"closeDate": null,
						"lastUpdateDate": 1393823844000,
						"comments": [
									{
  										"timestamp": 1393823844000,
  										"userName": "Portal User",
  										"detail": "Name: Road Runner\nPhone: null\nEmail: acmelabs.roadrunner@gmail.com\n\nTest"
									}
									]
				}
				*/
				 //a. Read received JSON response 
				try {
					
				// Convert string to JSONArray
				JSONObject ticketdetails_JsonObj = new JSONObject(Webservice_GetTicketDetails.str_response);
				
				String ticketId = new String(": "+ticketdetails_JsonObj.getString("ticketId")); 
				String subject = new String(ticketdetails_JsonObj.getString("subject")); 
				String status = new String(": "+ticketdetails_JsonObj.getString("status"));
		
				String createDate;
				if (!(ticketdetails_JsonObj.get("createDate").equals(JSONObject.NULL)))
					createDate = ticketdetails_JsonObj.getString("createDate");
				else
					createDate = "-1";
			
				String closeDate;
				if (!(ticketdetails_JsonObj.get("closeDate").equals(JSONObject.NULL)))
					closeDate = ticketdetails_JsonObj.getString("closeDate");
				else
					closeDate = "-1";
	
				String lastUpdateDate;
				if (!(ticketdetails_JsonObj.get("lastUpdateDate").equals(JSONObject.NULL)))
					lastUpdateDate = ticketdetails_JsonObj.getString("lastUpdateDate");
				else
					lastUpdateDate = "-1";
				
				JSONArray comments_JsonArray = ticketdetails_JsonObj.getJSONArray("comments");
				ArrayList<Comment> commentsList = new ArrayList<Fragment_Tickets.Comment>();
				// Getting JSON Array node
				for (int i = 0; i < comments_JsonArray.length(); i++) {
					
						JSONObject comments_JsonObj = comments_JsonArray.getJSONObject(i);
						Comment comment = new Comment();
						        comment.timestamp = comments_JsonObj.getString("timestamp");
						        comment.userName = comments_JsonObj.getString("userName");
						        comment.detail = comments_JsonObj.getString("detail");
						        commentsList.add(comment);
				}
				
			
				//b. Prepare view
				// Remove expandable list view
				((LinearLayout) lin_rootview).removeView(expListView);
				
				// Add ticketdetails_view
				viewgroup_ticketdetails_view = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.ticketdetails_view, (ViewGroup) lin_rootview,false);
				((ViewGroup) lin_rootview).addView(viewgroup_ticketdetails_view);
				
					//subject
		        	TextView tv_subject = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewSubjectValue);
		        	tv_subject.setText(subject);
		        	//Submitted by
		        	SharedPreferences prefs = Activity_SliderMenu.context.getSharedPreferences(Send_to_Web.fileName, Activity_SliderMenu.MODE_PRIVATE);
		        	TextView tv_submittedby = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewSubmittedByValue);
		        	tv_submittedby.setText(prefs.getString("firstName", "")+" "+prefs.getString("lastName", ""));
		        	//Last updated
		        	TextView tv_lastupdated = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewLastUpdatedValue);
		        	DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		        	tv_lastupdated.setText(dateTimeFormatter.print(new DateTime(Long.parseLong(lastUpdateDate))).toString());
		        	//ticket id
		        	TextView tv_ticketid = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewticketsIdValue);
		        	tv_ticketid.setText(ticketId);
		        	//status
		        	TextView tv_status = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewStatusValue);
		        	tv_status.setText(status);
				}
				catch (JSONException e)
				{
					
				}
				
			}
		};
		//==================
		return lin_rootview;
	}//OnCreateView

	 
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
//				Toast.makeText(
//						Activity_SliderMenu.context.getApplicationContext(),
//						listDataHeader.get(groupPosition)
//								+ " : "
//								+ listDataChild.get(
//										listDataHeader.get(groupPosition)).get(
//										childPosition), Toast.LENGTH_SHORT)
//						.show();
				
				// 1. Fetching ticket id
				Toast.makeText(Activity_SliderMenu.context, "ticketId="+listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).ticketId, Toast.LENGTH_SHORT).show();
				Webservice_GetTicketDetails details = new Webservice_GetTicketDetails(Activity_SliderMenu.context, listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).ticketId);
				details.postData();
				
//					Log.d("tag", ""+Webservice_GetTicketDetails.response);
				
				// 2. Prepare ticket view
				   /*
				    {
  						"ticketId": 207935,
  						"subject": "MB095663 / Acme Labs / Irving, TX / Outage",
  						"status": "Open",
  						"createDate": 1393823842000,
  						"closeDate": null,
  						"lastUpdateDate": 1393823844000,
  						"comments": [
    									{
      										"timestamp": 1393823844000,
      										"userName": "Portal User",
      										"detail": "Name: Road Runner\nPhone: null\nEmail: acmelabs.roadrunner@gmail.com\n\nTest"
    									}
  									]
					}
				    * */
//				 //a. Read received JSON response 
//				try {
//				// Convert string to JSONArray
//				JSONObject ticketdetails_JsonObj = new JSONObject(Webservice_GetTicketDetails.response);
//				
//				String ticketId = new String(ticketdetails_JsonObj.getString("ticketId")); 
//				String subject = new String(ticketdetails_JsonObj.getString("subject")); 
//				String status = new String(ticketdetails_JsonObj.getString("status"));
//				String createDate;
//				if (!(ticketdetails_JsonObj.get("createDate").equals(JSONObject.NULL)))
//					createDate = ticketdetails_JsonObj.getString("createDate");
//				else
//					createDate = "-1";
//				
//				String closeDate;
//				if (!(ticketdetails_JsonObj.get("closeDate").equals(JSONObject.NULL)))
//					closeDate = ticketdetails_JsonObj.getString("closeDate");
//				else
//					closeDate = "-1";
//				
//				String lastUpdateDate;
//				if (!(ticketdetails_JsonObj.get("lastUpdateDate").equals(JSONObject.NULL)))
//					lastUpdateDate = ticketdetails_JsonObj.getString("lastUpdateDate");
//				else
//					lastUpdateDate = "-1";
//				
//				JSONArray comments_JsonArray = ticketdetails_JsonObj.getJSONArray("comments");
//				ArrayList<Comment> commentsList = new ArrayList<Fragment_Tickets.Comment>();
//				// Getting JSON Array node
//				for (int i = 0; i < comments_JsonArray.length(); i++) {
//						JSONObject comments_JsonObj = comments_JsonArray.getJSONObject(i);
//						Comment comment = new Comment();
//						        comment.timestamp = comments_JsonObj.getString("comment");
//						        comment.userName = comments_JsonObj.getString("userName");
//						        comment.detail = comments_JsonObj.getString("detail");
//						        commentsList.add(comment);
//				}
//				
//				//b. Prepare view
//				// Remove expandable list view
//				((LinearLayout) lin_rootview).removeView(lin_rootview.findViewById(R.id.lvExp));
//				
//				// Add ticketdetails_view
//				viewgroup_ticketdetails_view = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.ticketdetails_view, (ViewGroup) lin_rootview,false);
//				((ViewGroup) lin_rootview).addView(viewgroup_ticketdetails_view);
//				
//					//subject
//		        	TextView tv_subject = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewSubjectValue);
//		        	tv_subject.setText(subject);
//		        	//Submitted by
////		        	TextView tv_submittedby = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewSubmittedByValue);
////		        	tv_submittedby.setText();
//		        	//Last updated
//		        	TextView tv_lastupdated = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewLastUpdatedValue);
//		        	DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
//		        	tv_lastupdated.setText(dateTimeFormatter.print(new DateTime(Long.parseLong(lastUpdateDate))).toString());
//		        	//ticket id
//		        	TextView tv_ticketid = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewticketsIdValue);
//		        	tv_ticketid.setText(ticketId);
//		        	//status
//		        	TextView tv_status = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewStatusValue);
//		        	tv_status.setText(status);
//				}
//				catch (JSONException e)
//				{
//					
//				}
//				

				
				return true;
			}
		});
		
	}//init()

	
	class Comment
	{
		String timestamp, userName, detail; 
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
	
	
//	//-----------------Get Ticket Details-------------------
//	class Webservice_GetTicketDetails {
//
//		SharedPreferences.Editor sharedPrefEditor;
////		String webServiceLinkForSubmit = "https://webservice-dev.masergy.com/webservices_mobile/rest/v1/ticket/";
//		String webServiceLinkForTicketDetails = "https://webservice.masergy.com/webservices_mobile/rest/v1/ticket/";
//		Context mContext;
//		String ticketId;
//		ProgressDialog mpProgress;
//		public String response;
//
//		public Webservice_GetTicketDetails(Context context, String ticketId) {
//			this.mContext = context;
//			this.ticketId = ticketId;
//			webServiceLinkForTicketDetails = webServiceLinkForTicketDetails+ticketId;
//			String fileName = "Login";
//			sharedPrefEditor = ((Activity) context).getSharedPreferences(fileName,
//					context.MODE_PRIVATE).edit();
//		}
//
//		public void postData() {
//			if (isNetworkAvailable()) {
//				post_data post = new post_data();
//				post.execute();
//			} else {
//				Toast.makeText(mContext, "No network availble", 1000).show();
//			}
//		}
//
//		// =========================class post_data ========================
//		class post_data extends AsyncTask<Void, Void, String> {
//			@Override
//			protected void onPreExecute() {
//				// TODO Auto-generated method stub
//				super.onPreExecute();
//
//				((Activity) mContext).runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						mpProgress = ProgressDialog.show(mContext,
//								"Downloading data",
//								"Please wait for a moment...");
//					}
//				});
//			}//onPreExecute()
//
//			@Override
//			protected String doInBackground(Void... params) {
//				//Local variable declaration
//				String result=null;
//				// Create http client object to send request to server
//				HttpClient httpclient = new DefaultHttpClient();
//				// Create Request to server and get response
//				HttpGet httpget = new HttpGet(webServiceLinkForTicketDetails);
//				httpget.setHeader("Content-Type", "application/json");
//				httpget.setHeader(
//						"Authorization",
//						mContext.getSharedPreferences(Send_to_Web.fileName,
//								mContext.MODE_PRIVATE).getString("authToken", null));
//				HttpResponse response;
//				try {
//					
//					response = httpclient.execute(httpget);
//					HttpEntity entity = response.getEntity();
//					
//					
//					if (entity != null) {
//
//						// Read JSON Response
//						InputStream instream = entity.getContent();
//						StringWriter writer = new StringWriter();
//						IOUtils.copy(instream, writer);
//						result = writer.toString();
//						Log.d("tag", "ticketDetails=" + result);
//						instream.close();					
//					}// if
//
//				} catch (ClientProtocolException e) {
//					if (mpProgress.isShowing())
//						mpProgress.dismiss();
//					// TODO Auto-generated catch block
//				} catch (IOException e) {
//					if (mpProgress.isShowing())
//						mpProgress.dismiss();
//					// TODO Auto-generated catch block
//				}
//				return result;
//			}
//
//
//
//			@Override
//			protected void onPostExecute(String result) {
//				// TODO Auto-generated method stub
//				if (mpProgress.isShowing())
//					mpProgress.dismiss();
//				super.onPostExecute(result);
//				if (result != null) {
//
//					// Toast.makeText(mContext, "Response-"+result, 1000).show();
//					// System.out.println("Response="+result);
//					response=result;
//					
//					//Fragment_Tickets.tickets_handler.post(Fragment_Tickets.tickets_runnable);
//					
//					//a. Read received JSON response 
//					try {
//					// Convert string to JSONArray
//					JSONObject ticketdetails_JsonObj = new JSONObject(response);
//					
//					String ticketId = new String(ticketdetails_JsonObj.getString("ticketId")); 
//					String subject = new String(ticketdetails_JsonObj.getString("subject")); 
//					String status = new String(ticketdetails_JsonObj.getString("status"));
//					String createDate;
//					if (!(ticketdetails_JsonObj.get("createDate").equals(JSONObject.NULL)))
//						createDate = ticketdetails_JsonObj.getString("createDate");
//					else
//						createDate = "-1";
//					
//					String closeDate;
//					if (!(ticketdetails_JsonObj.get("closeDate").equals(JSONObject.NULL)))
//						closeDate = ticketdetails_JsonObj.getString("closeDate");
//					else
//						closeDate = "-1";
//					
//					String lastUpdateDate;
//					if (!(ticketdetails_JsonObj.get("lastUpdateDate").equals(JSONObject.NULL)))
//						lastUpdateDate = ticketdetails_JsonObj.getString("lastUpdateDate");
//					else
//						lastUpdateDate = "-1";
//					
//					JSONArray comments_JsonArray = ticketdetails_JsonObj.getJSONArray("comments");
//					ArrayList<Comment> commentsList = new ArrayList<Fragment_Tickets.Comment>();
//					// Getting JSON Array node
//					for (int i = 0; i < comments_JsonArray.length(); i++) {
//							JSONObject comments_JsonObj = comments_JsonArray.getJSONObject(i);
//							Comment comment = new Comment();
//							        comment.timestamp = comments_JsonObj.getString("comment");
//							        comment.userName = comments_JsonObj.getString("userName");
//							        comment.detail = comments_JsonObj.getString("detail");
//							        commentsList.add(comment);
//					}
//					Log.d("tag-tag-tag", "I am here");
//					//b. Prepare view
//					// Remove expandable list view
//					((LinearLayout) lin_rootview).removeView(lin_rootview.findViewById(R.id.lvExp));
//					
//					// Add ticketdetails_view
//					viewgroup_ticketdetails_view = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.ticketdetails_view, (ViewGroup) lin_rootview,false);
//					((ViewGroup) lin_rootview).addView(viewgroup_ticketdetails_view);
//					
//						//subject
//			        	TextView tv_subject = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewSubjectValue);
//			        	tv_subject.setText(subject);
//			        	//Submitted by
////			        	TextView tv_submittedby = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewSubmittedByValue);
////			        	tv_submittedby.setText();
//			        	//Last updated
//			        	TextView tv_lastupdated = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewLastUpdatedValue);
//			        	DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
//			        	tv_lastupdated.setText(dateTimeFormatter.print(new DateTime(Long.parseLong(lastUpdateDate))).toString());
//			        	//ticket id
//			        	TextView tv_ticketid = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewticketsIdValue);
//			        	tv_ticketid.setText(ticketId);
//			        	//status
//			        	TextView tv_status = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewStatusValue);
//			        	tv_status.setText(status);
//					}
//					catch (JSONException e)
//					{
//						
//					}
//					
//
//				} else {
//					Toast.makeText(mContext, "No response from server", 1000)
//							.show();
//					System.out.println("No response from server");
//				}
//			}
//
//		}
//
//		// =================================================================
//
//		// To check network connectivity
//		private boolean isNetworkAvailable() {
//			ConnectivityManager connectivityManager = (ConnectivityManager) mContext
//					.getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo activeNetworkInfo = connectivityManager
//					.getActiveNetworkInfo();
//			return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//		}
//	}
//	//------------------------------------------------------	
}




