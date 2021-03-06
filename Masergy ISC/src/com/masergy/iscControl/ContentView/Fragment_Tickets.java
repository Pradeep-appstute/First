package com.masergy.iscControl.ContentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.masergy.iscControl.Activity_SliderMenu;
import com.masergy.iscControl.R;
import com.masergy.iscControl.utility.Webservice_GetTicketDetails;
import com.masergy.iscControl.utility.Webservice_GetTicketsList;
import com.masergy.iscControl.utility.Webservice_Login;
import com.masergy.iscControl.utility.Webservice_PostSubmitData;

public class Fragment_Tickets extends Fragment {

	// For  Spinner
	int bundle_position, subject_position;
	List<String> list_bundle_spinner,list_subject_spinner;
	ArrayAdapter<String> dataAdapter_bundle_spinner, dataAdapter_subject_spinner;
    boolean tappedBundleSpinner=false, tappedSubjectSpinner=false;
	//To resolve issue of changing tabs
	static boolean isSubmitTapped;//Temporary declared static, later save in shared pref. 
	public static boolean isTicketDetailsTapped;
	public static LinearLayout lin_rootview;
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
	public static Runnable tickets_runnable_forPrevNext;
	
	//To maintain Next and Previous button on TiketDetails View
	int groupPosition, childPosition;
	
	public static void reloadTicketsView()
	{
		imgButtonOpen.performClick();
		Webservice_GetTicketsList instance = new Webservice_GetTicketsList(Activity_SliderMenu.context);
		instance.postData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		isSubmitTapped =false;
		isTicketDetailsTapped=false;
		bundle_position=subject_position=0;
		// construct the RelativeLayout
		lin_rootview = (LinearLayout) inflater.inflate(R.layout.fragment_tickets, container, false);
		lin_rootview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				
				InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
               		    InputMethodManager.HIDE_NOT_ALWAYS);
				return false;
			}
		});
		
		
		// ==========Menu Title============
		TextView menu_title = ((TextView) lin_rootview.findViewById(R.id.activity_main_content_title));
			menu_title.setText("Tickets");
				
		// ===========Menu Button===============
		Button toggleMenuButton = ((Button) lin_rootview.findViewById(R.id.activity_main_content_button_menu));
			       toggleMenuButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
							Activity_SliderMenu.slidingMenu.toggle();
							InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			                 inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
			                		    InputMethodManager.HIDE_NOT_ALWAYS);
					}
			});
			       
			       
		// ================== Hide or Unhide ==========
	    imgButtonOpen = (ImageButton) lin_rootview.findViewById(R.id.imgButtonOpen);
		float btn_x_position = imgButtonOpen.getX();
		float btn_y_position = imgButtonOpen.getY();
	    imgButtonClosed = (ImageButton) lin_rootview.findViewById(R.id.imgButtonClosed);
	    imgButtonMaint = (ImageButton) lin_rootview.findViewById(R.id.imgButtonMaint);
	    imgButtonSubmit = (ImageButton) lin_rootview.findViewById(R.id.imgButtonSubmit);
	    
		SharedPreferences prefs = Activity_SliderMenu.context.getSharedPreferences("Login", Activity_SliderMenu.context.MODE_PRIVATE);				
		if(prefs.getString("permViewTicket", "true").equals("false"))
		{
			imgButtonOpen.setEnabled(false);
			imgButtonClosed.setEnabled(false);
			imgButtonMaint.setEnabled(false);
			imgButtonOpen.setVisibility(View.INVISIBLE);
			imgButtonClosed.setVisibility(View.INVISIBLE);
			imgButtonMaint.setVisibility(View.INVISIBLE);
			
			//Remove views
			lin_rootview.removeView(imgButtonOpen);
			lin_rootview.removeView(imgButtonClosed);
			lin_rootview.removeView(imgButtonMaint);
		}
		
		if(prefs.getString("permSubmitTicket", "true").equals("false"))
		{
			imgButtonSubmit.setEnabled(false);
			imgButtonSubmit.setVisibility(View.INVISIBLE);
			
			//Remove view
			lin_rootview.removeView(imgButtonSubmit);
		}
		
		// ===========Tab Buttons===============
		// If imgButtonOpen is enabled
		if (imgButtonOpen.isEnabled())
		{
			// get the listview
			expListView = (ExpandableListView) lin_rootview.findViewById(R.id.lvExp);
			expListView.setGroupIndicator(null);
			expListView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
	                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
	               		    InputMethodManager.HIDE_NOT_ALWAYS);
					return false;
				}
			});
			
			// preparing list data
			prepareListData(OpenTab);
			// load list view
			initExpandableListView();	
			
			
			imgButtonOpen.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
	                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
	               		    InputMethodManager.HIDE_NOT_ALWAYS);
					return false;
				}
			});
			imgButtonOpen.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					
					imgButtonOpen.setBackgroundResource(R.drawable.img_btnmopenselected);
					imgButtonClosed.setBackgroundResource(R.drawable.img_btnmclosed);
					imgButtonMaint.setBackgroundResource(R.drawable.img_btnmmaint);
					imgButtonSubmit.setBackgroundResource(R.drawable.img_btnmsubmit);
					
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
			
			imgButtonClosed.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
	                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
	               		    InputMethodManager.HIDE_NOT_ALWAYS);
					return false;
				}
			});
			imgButtonClosed.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					imgButtonOpen.setBackgroundResource(R.drawable.img_btnmopen);
					imgButtonClosed.setBackgroundResource(R.drawable.img_btnmclosedselected);
					imgButtonMaint.setBackgroundResource(R.drawable.img_btnmmaint);
					imgButtonSubmit.setBackgroundResource(R.drawable.img_btnmsubmit);
					
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
			
			imgButtonMaint.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
	                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
	               		    InputMethodManager.HIDE_NOT_ALWAYS);
					return false;
				}
			});
			imgButtonMaint.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					imgButtonOpen.setBackgroundResource(R.drawable.img_btnmopen);
					imgButtonClosed.setBackgroundResource(R.drawable.img_btnmclosed);
					imgButtonMaint.setBackgroundResource(R.drawable.img_btnmmaintselected);
					imgButtonSubmit.setBackgroundResource(R.drawable.img_btnmsubmit);

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
					
					String ticketId = new String(":"+ticketdetails_JsonObj.getString("ticketId")); 
					String subject = new String(ticketdetails_JsonObj.getString("subject")); 
					String status = new String(":"+ticketdetails_JsonObj.getString("status"));
			
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
							        comment.detail = (comments_JsonObj.getString("detail").toString()).replace("null", "");
							        commentsList.add(comment);
					}
					
				
					//b. Prepare view
					// Remove expandable list view
					((LinearLayout) lin_rootview).removeView(expListView);
					
					// Add ticketdetails_view
					viewgroup_ticketdetails_view = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.ticketdetails_view, (ViewGroup) lin_rootview,false);
					viewgroup_ticketdetails_view.setOnTouchListener(new OnTouchListener() {
						
						@Override
						
						
						public boolean onTouch(View v, MotionEvent event) {
							
							Toast.makeText(Activity_SliderMenu.context, "OnTouch detected", Toast.LENGTH_SHORT).show();
							InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
			               		    InputMethodManager.HIDE_NOT_ALWAYS);
			                
			                
							return false;
						}
					});
		
					((ViewGroup) lin_rootview).addView(viewgroup_ticketdetails_view);
					
						//subject
			        	TextView tv_subject = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewSubjectValue);
			        	tv_subject.setText(subject);
			        	//Submitted by
			        	SharedPreferences prefs = Activity_SliderMenu.context.getSharedPreferences(Webservice_Login.fileName, Activity_SliderMenu.MODE_PRIVATE);
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
			        	
			        	//List View
			        	ListView listView = (ListView) viewgroup_ticketdetails_view.findViewById(R.id.listViewComment);
			        	listView.setAdapter(new TicketsDetailsListAdapter(Activity_SliderMenu.context, commentsList));
			        	
			        	//Next and Prev button
			        	ImageButton prevBtn=(ImageButton)viewgroup_ticketdetails_view.findViewById(R.id.ticketdetails_prev);
			        	ImageButton nextBtn=(ImageButton)viewgroup_ticketdetails_view.findViewById(R.id.ticketdetails_next);
//			        	       Log.d("tag", "childPosition="+childPosition);
//			        	       Log.d("tag", "listDataChild.get(listDataHeader.get(groupPosition)).size()="+listDataChild.get(listDataHeader.get(groupPosition)).size());
					        	if((listDataChild.get(listDataHeader.get(groupPosition)).size())==1)
					        	{
					        		nextBtn.setEnabled(false);
			        				prevBtn.setEnabled(false);
//			        				Log.d("tag", "false-false");
			        				nextBtn.setBackgroundResource(R.drawable.img_btnnextunselected);
			        				prevBtn.setBackgroundResource(R.drawable.img_btnprevunselected);
					        	}
					        	else if(childPosition==0)
					        	{
					        	    prevBtn.setEnabled(false);
					        	    nextBtn.setEnabled(true);
//					        	    Log.d("tag", "false-true");
					        	    nextBtn.setBackgroundResource(R.drawable.img_btnnextselected);
					        	    prevBtn.setBackgroundResource(R.drawable.img_btnprevunselected);
					        	}
					        	else if(childPosition==(listDataChild.get(listDataHeader.get(groupPosition)).size() -1))
			        			{
					        		prevBtn.setEnabled(true);
					        		nextBtn.setEnabled(false);
					        		nextBtn.setBackgroundResource(R.drawable.img_btnnextunselected);
					        		prevBtn.setBackgroundResource(R.drawable.img_btnprevselected);
//			        				Log.d("tag", "true-false");
			        			}
					        	else
					        	{
					        		nextBtn.setEnabled(true);
			        				prevBtn.setEnabled(true);
			        				prevBtn.setBackgroundResource(R.drawable.img_btnprevselected);
			        				nextBtn.setBackgroundResource(R.drawable.img_btnnextselected);
//			        				Log.d("tag", "true-true");
					        	}
			        	
			        	prevBtn.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View view) {
								if (childPosition>0)
								{
									childPosition=(childPosition-1);
//									tickets_handler.post(tickets_runnable);
									Webservice_GetTicketDetails details = new Webservice_GetTicketDetails(Activity_SliderMenu.context, listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).ticketId,true);
									details.postData();

								}
							}
						});
			        	
			        	
			        			
			        	nextBtn.setOnClickListener(new OnClickListener() {
									
							@Override
							public void onClick(View v) {
								if(childPosition<(listDataChild.get(listDataHeader.get(groupPosition)).size() -1))
								{
									childPosition = (childPosition+1);
//									tickets_handler.post(tickets_runnable);
									Webservice_GetTicketDetails details = new Webservice_GetTicketDetails(Activity_SliderMenu.context, listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).ticketId,true);
									details.postData();

								}		
							}
						});
					}
					catch (JSONException e)
					{
						
					}
					
				}
			};
			
			tickets_runnable_forPrevNext = new Runnable() {
				
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
					
					String ticketId = new String(":"+ticketdetails_JsonObj.getString("ticketId")); 
					String subject = new String(ticketdetails_JsonObj.getString("subject")); 
					String status = new String(":"+ticketdetails_JsonObj.getString("status"));
			
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
							        comment.detail = (comments_JsonObj.getString("detail")).toString().replace("null", "");
							        commentsList.add(comment);
					}
					
				
					// Add ticketdetails_view
//					viewgroup_ticketdetails_view = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.ticketdetails_view, (ViewGroup) lin_rootview,false);
//					((ViewGroup) lin_rootview).addView(viewgroup_ticketdetails_view);
					
						//subject
			        	TextView tv_subject = (TextView)viewgroup_ticketdetails_view.findViewById(R.id.textViewSubjectValue);
			        	tv_subject.setText(subject);
			        	//Submitted by
			        	SharedPreferences prefs = Activity_SliderMenu.context.getSharedPreferences(Webservice_Login.fileName, Activity_SliderMenu.MODE_PRIVATE);
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
			        	
			        	//List View
			        	ListView listView = (ListView) viewgroup_ticketdetails_view.findViewById(R.id.listViewComment);
			        	listView.setAdapter(new TicketsDetailsListAdapter(Activity_SliderMenu.context, commentsList));
			        	
			        	//Next and Prev button
			        	ImageButton prevBtn=(ImageButton)viewgroup_ticketdetails_view.findViewById(R.id.ticketdetails_prev);
			        	ImageButton nextBtn=(ImageButton)viewgroup_ticketdetails_view.findViewById(R.id.ticketdetails_next);
			        	  
//			        	Log.d("tag", "childPosition="+childPosition);
//		        	       Log.d("tag", "listDataChild.get(listDataHeader.get(groupPosition)).size()="+listDataChild.get(listDataHeader.get(groupPosition)).size());
				        	if((listDataChild.get(listDataHeader.get(groupPosition)).size())==1)
				        	{
				        		nextBtn.setEnabled(false);
		        				prevBtn.setEnabled(false);
//		        				Log.d("tag", "false-false");
		        				nextBtn.setBackgroundResource(R.drawable.img_btnnextunselected);
		        				prevBtn.setBackgroundResource(R.drawable.img_btnprevunselected);
				        	}
				        	else if(childPosition==0)
				        	{
				        	    prevBtn.setEnabled(false);
				        	    nextBtn.setEnabled(true);
//				        	    Log.d("tag", "false-true");
				        	    nextBtn.setBackgroundResource(R.drawable.img_btnnextselected);
				        	    prevBtn.setBackgroundResource(R.drawable.img_btnprevunselected);
				        	}
				        	else if(childPosition==(listDataChild.get(listDataHeader.get(groupPosition)).size() -1))
		        			{
				        		prevBtn.setEnabled(true);
				        		nextBtn.setEnabled(false);
				        		nextBtn.setBackgroundResource(R.drawable.img_btnnextunselected);
				        		prevBtn.setBackgroundResource(R.drawable.img_btnprevselected);
//		        				Log.d("tag", "true-false");
		        			}
				        	else
				        	{
				        		nextBtn.setEnabled(true);
		        				prevBtn.setEnabled(true);
		        				prevBtn.setBackgroundResource(R.drawable.img_btnprevselected);
		        				nextBtn.setBackgroundResource(R.drawable.img_btnnextselected);
//		        				Log.d("tag", "true-true");
				        	}
		        	
			        	
			        	prevBtn.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View view) {
								if (childPosition>0)
								{
									childPosition=(childPosition-1);
//									tickets_handler.post(tickets_runnable);
									Webservice_GetTicketDetails details = new Webservice_GetTicketDetails(Activity_SliderMenu.context, listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).ticketId,true);
									details.postData();

								}
							}
						});
			        	
			        	
			        			
			        	nextBtn.setOnClickListener(new OnClickListener() {
									
							@Override
							public void onClick(View v) {
								if(childPosition<(listDataChild.get(listDataHeader.get(groupPosition)).size() -1))
								{
									childPosition = (childPosition+1);
//									tickets_handler.post(tickets_runnable);
									Webservice_GetTicketDetails details = new Webservice_GetTicketDetails(Activity_SliderMenu.context, listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).ticketId,true);
									details.postData();

								}		
							}
						});
					}
					catch (JSONException e)
					{
						
					}
					
				}
			};
		}// if (imgButtonOpen.isEnabled())
			

			

		

		// If imgButtonSubmit is enabled
		if (imgButtonSubmit.isEnabled())
		{
		
		imgButtonSubmit.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
               		    InputMethodManager.HIDE_NOT_ALWAYS);
				return false;
			}
		});
		imgButtonSubmit.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if(isSubmitTapped==false)
				{
				imgButtonOpen.setBackgroundResource(R.drawable.img_btnmopen);
				imgButtonClosed.setBackgroundResource(R.drawable.img_btnmclosed);
				imgButtonMaint.setBackgroundResource(R.drawable.img_btnmmaint);
				imgButtonSubmit.setBackgroundResource(R.drawable.img_btnmsubmitselected);
						
				isSubmitTapped=true;
				tappedBundleSpinner=false;
				tappedSubjectSpinner=false;
				if (isTicketDetailsTapped) //Load Tickets list view
				{
					TextView menu_title = ((TextView) lin_rootview.findViewById(R.id.activity_main_content_title));
					menu_title.setText("Tickets");
					isTicketDetailsTapped=false;
					
					// Remove ticketdetails view
					((LinearLayout) lin_rootview).removeView(viewgroup_ticketdetails_view);
					
					// Add submitview
					viewgroup_submitview = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.submit_view, (ViewGroup) lin_rootview,false);
					viewgroup_submitview.setOnTouchListener(new OnTouchListener() {
						
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							
							InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
			               		    InputMethodManager.HIDE_NOT_ALWAYS);
							return false;
						}
					});
					((ViewGroup) lin_rootview).addView(viewgroup_submitview);
					
				}//if (isTicketDetailsTapped) 
				else 
				{
				// Remove expandable list view
				((LinearLayout) lin_rootview).removeView(lin_rootview.findViewById(R.id.lvExp));
				
				// Add submitview
				viewgroup_submitview = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.submit_view, (ViewGroup) lin_rootview,false);
				viewgroup_submitview.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
		               		    InputMethodManager.HIDE_NOT_ALWAYS);
						return false;
					}
				});
				((ViewGroup) lin_rootview).addView(viewgroup_submitview);
				}
				
				final SharedPreferences prefs = Activity_SliderMenu.context.getSharedPreferences(Webservice_Login.fileName, Activity_SliderMenu.context.MODE_PRIVATE);    
		        
		        String firstName = prefs.getString("firstName", "");
		        if (firstName != null) 
		        {
		        	firstName = firstName+" "+prefs.getString("lastName", "");
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
		        addItemsOnSpinnerBundle(spinner_bundle, prefs, true);
		        spinner_bundle.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
		               		    InputMethodManager.HIDE_NOT_ALWAYS);
						tappedBundleSpinner = true;
						addItemsOnSpinnerBundle(spinner_bundle, prefs, false);
						dataAdapter_bundle_spinner.notifyDataSetChanged();
						spinner_bundle.setSelection(bundle_position);
						return false;
					}
				});

		        spinner_bundle.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int pos, long id) {
						// Remove select text from spinner
//						addItemsOnSpinnerBundle(spinner_bundle, prefs, false);
						bundle_position=pos;
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
		        addItemsOnSpinnerSubject(spinner_subject, prefs, true);	
		        spinner_subject.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
		               		    InputMethodManager.HIDE_NOT_ALWAYS);
		                
						tappedSubjectSpinner = true;
						addItemsOnSpinnerSubject(spinner_subject, prefs, false);
						dataAdapter_subject_spinner.notifyDataSetChanged();
						spinner_subject.setSelection(subject_position);
						return false;
					}
				});

		        spinner_subject.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int pos, long id) {
						// Remove select text from spinner
//						addItemsOnSpinnerBundle(spinner_subject, prefs, false);
						subject_position=pos;
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
							txt_description = ""+editTextDescription.getText().toString();
							
							
							//Check for empty field

							if (txt_description.length()==0 && tappedBundleSpinner==false && tappedSubjectSpinner==false)
							{
								//If all fields are empty and then tap on create ticket then
								//Warning! : Please fill all the respective fields.     OK
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity_SliderMenu.context);
								 
								// set title
								alertDialogBuilder.setTitle("Warning!");
					 
								// set dialog message
								alertDialogBuilder
									.setMessage("Please fill all the respective fields.")
									.setCancelable(false)
									.setPositiveButton("OK",new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											// if this button is clicked, just close
											// the dialog box and do nothing
											dialog.cancel();
										}
									  });
									
					 
									// create alert dialog
									AlertDialog alertDialog = alertDialogBuilder.create();
					 
									// show it
									alertDialog.show();
							}
							else if (tappedSubjectSpinner==false)
							{
							//If Subject is not entered, and then tap on create ticket then
							//Warning! : Please enter the subject. OK
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity_SliderMenu.context);
								 
								// set title
								alertDialogBuilder.setTitle("Warning!");
					 
								// set dialog message
								alertDialogBuilder
									.setMessage("Please enter the subject.")
									.setCancelable(false)
									.setPositiveButton("OK",new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											// if this button is clicked, just close
											// the dialog box and do nothing
											dialog.cancel();
										}
									  });
									
					 
									// create alert dialog
									AlertDialog alertDialog = alertDialogBuilder.create();
					 
									// show it
									alertDialog.show();
							}
							else if (tappedBundleSpinner==false)
							{
							//If Bundle is not entered, and then tap on create ticket then
							//Warning! : Please enter the bundle.                    OK
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity_SliderMenu.context);
								 
								// set title
								alertDialogBuilder.setTitle("Warning!");
					 
								// set dialog message
								alertDialogBuilder
									.setMessage("Please enter the bundle.")
									.setCancelable(false)
									.setPositiveButton("OK",new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											// if this button is clicked, just close
											// the dialog box and do nothing
											dialog.cancel();
										}
									  });
									
					 
									// create alert dialog
									AlertDialog alertDialog = alertDialogBuilder.create();
					 
									// show it
									alertDialog.show();
							}
							else if (txt_description.length()==0)
							{
							//In description is not entered, and then tap on create ticket then
							//Warning! : Please enter the description.             OK
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity_SliderMenu.context);
								 
								// set title
								alertDialogBuilder.setTitle("Warning!");
					 
								// set dialog message
								alertDialogBuilder
									.setMessage("Please enter the description.")
									.setCancelable(false)
									.setPositiveButton("OK",new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											// if this button is clicked, just close
											// the dialog box and do nothing
											dialog.cancel();
										}
									  });
									
					 
									// create alert dialog
									AlertDialog alertDialog = alertDialogBuilder.create();
					 
									// show it
									alertDialog.show();
							}
							else if (txt_description.length()>0 && tappedBundleSpinner==true && tappedSubjectSpinner==true)
							{
								Webservice_PostSubmitData webservicePostSubmitData = new Webservice_PostSubmitData(Activity_SliderMenu.context, txt_subject, txt_bundleid, txt_description);
								webservicePostSubmitData.postData();
							}
						}
					});
		               
			}//if (isSubmitTapped==false)     
			}


		});

		if (imgButtonOpen.isEnabled()==false)

			
		{	
//			lin_rootview = (LinearLayout) inflater.inflate(R.layout.fragment_tickets, container, false);
//			imgButtonSubmit.setX(btn_x_position);
//		    imgButtonSubmit.setY(btn_y_position);
//			imgButtonSubmit.invalidate();
			imgButtonSubmit.performClick();
		}
		}//if (imgButtonSubmit.isEnabled())

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
//				Log.d("tag", "groupPosition="+groupPosition);
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

				
				//Set groupPosition, childPosition
				//Log.d("tag", "listDataHeader GROUP SIZE="+listDataChild.get(listDataHeader.get(groupPosition)).size());
				//Log.d("tag", "childPosition="+childPosition);
				Fragment_Tickets.this.groupPosition=groupPosition; 
				Fragment_Tickets.this.childPosition=childPosition;
				
				// 1. Fetching ticket id
				//Toast.makeText(Activity_SliderMenu.context, "ticketId="+listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).ticketId, Toast.LENGTH_SHORT).show();
				Webservice_GetTicketDetails details = new Webservice_GetTicketDetails(Activity_SliderMenu.context, listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).ticketId,false);
				details.postData();

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
	private void prepareListData(int tabId) {

		if (isTicketDetailsTapped) //Load Tickets list view
		{
			TextView menu_title = ((TextView) lin_rootview.findViewById(R.id.activity_main_content_title));
			menu_title.setText("Tickets");
			isTicketDetailsTapped=false;
			
			// Remove expandable list view
			((LinearLayout) lin_rootview).removeView(viewgroup_ticketdetails_view);
			
			// Add ticketdetails_view
			//expListView = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.ticketdetails_view, (ViewGroup) lin_rootview,false);
			((ViewGroup) lin_rootview).addView(expListView);
		}//if (isTicketDetailsTapped) 

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

		if (tabId == OpenTab) {
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
		} else if (tabId == ClosedTab) {

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

		} else if (tabId == MaintTab) {

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
		} else if (tabId == SubmitTab) {

		}

		listDataChild.clear();
		listDataChild.put(listDataHeader.get(0), today); // Header, Child data
		listDataChild.put(listDataHeader.get(1), thisWeek);
		listDataChild.put(listDataHeader.get(2), lastWeek);
		listDataChild.put(listDataHeader.get(3), last30Days);
		
//		Log.d("tag", "listDataChild size="+listDataChild.size());
//		Log.d("tag", "listDataHeader size="+listDataHeader.size());
	}

    private void addItemsOnSpinnerSubject(Spinner subject_spinner, SharedPreferences prefs, boolean addSelectText) {
		
    	list_subject_spinner = new ArrayList<String>();
	
		

		if(addSelectText==true)
			list_subject_spinner.add("Select subject");
        try {
            JSONArray jsonArraySubject = new JSONArray(prefs.getString("subjects", "[]"));
            for (int i = 0; i < jsonArraySubject.length(); i++) {
            	list_subject_spinner.add((String) jsonArraySubject.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  
		dataAdapter_subject_spinner = new ArrayAdapter<String>(Activity_SliderMenu.context, android.R.layout.simple_spinner_item, list_subject_spinner);
		dataAdapter_subject_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subject_spinner.setAdapter(dataAdapter_subject_spinner);
		
	}

	private void addItemsOnSpinnerBundle(Spinner bundle_spinner, SharedPreferences prefs, boolean addSelectText) {
		list_bundle_spinner = new ArrayList<String>();
		
		if(addSelectText==true)
			list_bundle_spinner.add("Select bundle");
        try {
            JSONArray jsonArraySubject = new JSONArray(prefs.getString("bundles", "[]"));
            for (int i = 0; i < jsonArraySubject.length(); i++) {
            	list_bundle_spinner.add((String) jsonArraySubject.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  
		dataAdapter_bundle_spinner = new ArrayAdapter<String>(Activity_SliderMenu.context, android.R.layout.simple_spinner_item, list_bundle_spinner);
		dataAdapter_bundle_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bundle_spinner.setAdapter(dataAdapter_bundle_spinner);	
		
	}
}//eof class




