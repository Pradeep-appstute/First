package com.masergy.iscticket.ContentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.masergy.iscticket.Activity_SliderMenu;
import com.masergy.iscticket.R;
import com.masergy.iscticket.ContentView.Fragment_Tickets.Webservice_GetTicketDetails;
import com.masergy.iscticket.utility.Webservice_GetDopplerIMList;
import com.masergy.iscticket.utility.Webservice_GetDopplerIMNodeDetails;
import com.masergy.iscticket.utility.Webservice_GetModifyServiceDetails;
import com.masergy.iscticket.utility.Webservice_GetModifyServiceList;
import com.masergy.iscticket.utility.Webservice_PostModifyDetails;

public class Fragment_DopplerIM extends Fragment {

	static LinearLayout lin_rootview;
	static ViewGroup viewgroup_dopplerimview;
//	static Spinner spinner_changeto;

//	ViewGroup viewgroup_dopplerimdetails_view;
//	static ListAdapter listAdapter;
//	static ListView listView;
	//--------Expandable list view---------
	public static DopplerIMListAdapter listAdapter;
	public static ExpandableListView expListView;
	public static ArrayList<DopplerIM_Parent> dopplerIM_Parents;
	//-------------------------------------
	static SearchView searchView;
	static LayoutInflater inflater;
	static ViewGroup container;
	
//	static ArrayList<DopplerIM_Parent> dopplerimList;
	
//	static String bundleId = null, prodType = null, location = null,
//			currentBandwidth = null, contractBandwidth = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		this.container = container;

		// construct the RelativeLayout
		lin_rootview = (LinearLayout) inflater.inflate(
				R.layout.fragment_dopplerim, container, false);

		// get the listview
		expListView = (ExpandableListView) lin_rootview.findViewById(R.id.lvExp);
		searchView = (SearchView) lin_rootview.findViewById(R.id.searchView);
		
		//initialize static variable
		dopplerIM_Parents = new ArrayList<DopplerIM_Parent>();
		// load list view
		initExpandableListView();

		// ===========Menu Button===============
		Button toggleMenuButton = ((Button) lin_rootview
				.findViewById(R.id.activity_main_content_button_menu));
		toggleMenuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity_SliderMenu.slidingMenu.toggle();
			}
		});
		return lin_rootview;
	}//onCreateView()

	
	public static void noResponseFromServer() {
		((LinearLayout) lin_rootview).removeView(expListView);
		
		// Add submitview
		viewgroup_dopplerimview = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.dopplerim_no_response_view, (ViewGroup) lin_rootview,false);
		((ViewGroup) lin_rootview).addView(viewgroup_dopplerimview);
	}

	public static void initExpandableListView() {
		
		listAdapter = new DopplerIMListAdapter(Activity_SliderMenu.context, dopplerIM_Parents);

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
				// 1. Fetching id
//				Toast.makeText(Activity_SliderMenu.context, "id="+dopplerIM_Parents.get(groupPosition).id, Toast.LENGTH_SHORT).show();
				new Webservice_GetDopplerIMNodeDetails(Activity_SliderMenu.context, dopplerIM_Parents.get(groupPosition).id, groupPosition).postData();

				
			
				return true;
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
				
//				
				return false;
			}
		});
		
	}//init()

	
//	public static void initListView() {
//		SharedPreferences prefs = Activity_SliderMenu.context
//				.getSharedPreferences(Webservice_GetDopplerIMList.fileName,
//						Activity_SliderMenu.context.MODE_PRIVATE);
//		String modifyJSONStr = prefs.getString("dopplerim", null);
//		dopplerimList = new ArrayList<DopplerIM_Parent>();
//		dopplerimList.clear();
//		
//		 					/*
//					 	[
//  {
//    "id": "953",
//    "name": "Accounting",
//    "alarmState": "3"
//  },
//  {
//    "id": "940",
//    "name": "haven",
//    "alarmState": "3"
//  },
// :
// :
// :
//]
//					 */
//
//		if (modifyJSONStr != null) 
//			Log.d("tag", "modifyJSONStr" + modifyJSONStr);
//
////			// Convert string to JSONArray
//			JSONArray jsonArray;
//			try {
//				jsonArray = new JSONArray(modifyJSONStr);
//				// Getting JSON Array node
//				for (int i = 0; i < jsonArray.length(); i++) {
//
//					JSONObject jsonObj = jsonArray.getJSONObject(i);
//					DopplerIM_Parent dopplerim = new DopplerIM_Parent();
//					/*
//					 * Log.d("tag", "" + jsonObj.get("bundleId")); Log.d("tag",
//					 * "" + jsonObj.get("bundleAlias")); Log.d("tag", "" +
//					 * jsonObj.get("currentBandwidth")); Log.d("tag", "" +
//					 * jsonObj.get("location"));
//					 */
//					if (!(jsonObj.get("id").equals(JSONObject.NULL)))
//						dopplerim.id= jsonObj.getString("id");
//					else
//						dopplerim.id = "-1";
//
//					if (!(jsonObj.get("name").equals(JSONObject.NULL)))
//						dopplerim.name = jsonObj.getString("name");
//					else
//						dopplerim.name = "";
//
//					if (!(jsonObj.get("alarmState").equals(JSONObject.NULL)))
//						dopplerim.alarmState = jsonObj.getString("alarmState");
//					else
//						dopplerim.alarmState = "-1";
//
//					dopplerimList.add(dopplerim);
//
//				}// for
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			listAdapter = new DopplerIMListAdapter(Activity_SliderMenu.context, dopplerimList);
//			expListView.setAdapter(listAdapter);
//			expListView.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//					// Toast.makeText(Activity_SliderMenu.context,
//					// ""+serviceList.get(position).bundleId,
//					// Toast.LENGTH_SHORT).show();
//					Webservice_GetDopplerIMNodeDetails instance = new Webservice_GetDopplerIMNodeDetails(Activity_SliderMenu.context, ""+ dopplerimList.get(position).id);
//					instance.postData();
//					
//					/*
//{
//  "id": "953",
//  "name": "Accounting",
//  "alarmState": "3",
//  "type": "Server",
//  "createDate": 1376480376000,
//  "cloudId": "MS079631",
//  "cloudName": null,
//  "ipAddress": "10.0.20.100",
//  "site": "Plano, TX",
//  "creditPointsUsed": "2",
//  "assetManufacturer": "Unknown",
//  "assetModel": "Unknown"
//}
//					 */
//				}
//			});
//		}
////	}// initListView
//
////
//////	public static void initServiceDetailsView() {
//////		SharedPreferences prefs = Activity_SliderMenu.context.getSharedPreferences(Webservice_GetModifyServiceList.fileName, Activity_SliderMenu.context.MODE_PRIVATE); 
//////		 String modifyJSONStr = prefs.getString("modifyservicedetails", null);
//////		 if (modifyJSONStr != null) 
//////		 {
//////			 //Log.d("tag", "modifyJSONStr="+modifyJSONStr);
//////			 /*
//////			  {
//////  "bundleId": "MB098596",
//////  "prodType": "NxT3",
//////  "location": "Plano, TX",
//////  "currentBandwidth": "42 Mbps",
//////  "contractBandwidth": "7.5 Mbps",
//////  "bandwidthOptions": [
//////    {
//////      "value": "171",
//////      "label": "33 Mbps"
//////    },
//////    {
//////      "value": "1074",
//////      "label": "35 Mbps"
//////    },
//////    {
//////      "value": "172",
//////      "label": "36 Mbps"
//////    },
//////    {
//////      "value": "173",
//////      "label": "39 Mbps"
//////    },
//////    {
//////      "value": "1075",
//////      "label": "40 Mbps"
//////    },
//////    {
//////      "value": "175",
//////      "label": "45 Mbps"
//////    }
//////  ]
//////}
//////			  */
//////			 
//////			 
//////			 final HashMap bandwidthOptions = new HashMap();
//////			 ArrayList<String> list = new ArrayList<String>();         
//////				// Convert string to JSONArray
//////				JSONObject jsonRootObj;
//////				try {
//////					jsonRootObj = new JSONObject(modifyJSONStr);
//////					
//////					bundleId = jsonRootObj.getString("bundleId");
//////					prodType = jsonRootObj.getString("prodType");
//////					location = jsonRootObj.getString("location");
//////				    currentBandwidth = jsonRootObj.getString("currentBandwidth");
//////				    contractBandwidth = jsonRootObj.getString("contractBandwidth");
//////				    JSONArray jsonArray = jsonRootObj.getJSONArray("bandwidthOptions");
//////				    
//////					// Getting JSON Array node
//////					for (int i = 0; i < jsonArray.length(); i++) {
//////
//////						JSONObject jsonObj = jsonArray.getJSONObject(i);
//////						bandwidthOptions.put( ""+jsonObj.getString("label").toString(),""+jsonObj.getString("value").toString());
//////						list.add(jsonObj.getString("label").toString());
//////					}// for	
//////					
//////					  // Get a set of the entries
//////				      Set set = bandwidthOptions.entrySet();
//////				      // Get an iterator
//////				      Iterator i = set.iterator();
//////				      // Display elements
//////				      while(i.hasNext()) {
//////				         Map.Entry me = (Map.Entry)i.next();
//////				         System.out.print(me.getKey() + ": ");
//////				         System.out.println(me.getValue());
//////				      }
//////				      
//////				     
//////				} catch (JSONException e) {
//////					// TODO Auto-generated catch block
//////					e.printStackTrace();
//////				} 
//////			 
//////				//Add modify service detail view 
//////				// Remove expandable searchview and list view
//////				((LinearLayout) lin_rootview).removeView(lin_rootview.findViewById(R.id.searchView));
//////				((LinearLayout) lin_rootview).removeView(lin_rootview.findViewById(R.id.lvExp));
//////				
//////				// Add submitview
//////				viewgroup_modifyserviceview = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.modifyservicedetail, (ViewGroup) lin_rootview,false);
//////				((ViewGroup) lin_rootview).addView(viewgroup_modifyserviceview);
//////				
////////				lin_fragment_modifyservicedetails
//////				
//////				TextView tv_bundleid = (TextView)viewgroup_modifyserviceview.findViewById(R.id.txtview_bundleid);
//////				tv_bundleid.setText(tv_bundleid.getText() + bundleId);
//////				
//////
//////				TextView tv_services = (TextView)viewgroup_modifyserviceview.findViewById(R.id.txtview_services);
//////				tv_services.setText(tv_services.getText() + prodType);
//////				
//////
//////				TextView tv_site = (TextView)viewgroup_modifyserviceview.findViewById(R.id.txtview_site);
//////				tv_site.setText(tv_site.getText() + location);
//////				
//////
//////				TextView tv_contract = (TextView)viewgroup_modifyserviceview.findViewById(R.id.txtView_contract);
//////				tv_contract.setText(contractBandwidth);
//////				
//////				TextView tv_current = (TextView)viewgroup_modifyserviceview.findViewById(R.id.txtView_current);
//////				tv_current.setText(currentBandwidth);
//////				
//////		        spinner_changeto = (Spinner)viewgroup_modifyserviceview.findViewById(R.id.spinner_changeto);
//////			 
//////				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Activity_SliderMenu.context, R.layout.modifyservice_spinner_item, list);
//////				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//////				spinner_changeto.setAdapter(dataAdapter);
//////		        spinner_changeto.setOnItemSelectedListener(new OnItemSelectedListener() {
//////
//////					@Override
//////					public void onItemSelected(AdapterView<?> parent, View view,
//////							int pos, long id) {
//////						// TODO Auto-generated method stub
//////						//Toast.makeText(parent.getContext(), "spinner Bundle", Toast.LENGTH_SHORT).show();
//////						//txt_bundleid = spinner_changeto.getSelectedItem().toString();
//////						//Toast.makeText(parent.getContext(), "txt_bundleid="+txt_bundleid,Toast.LENGTH_SHORT).show();
//////					}
//////
//////					@Override
//////					public void onNothingSelected(AdapterView<?> arg0) {
//////						// TODO Auto-generated method stub
//////						
//////					}
//////				});
//////
//////		        Button btn_back = (Button)viewgroup_modifyserviceview.findViewById(R.id.btn_back);
//////		        btn_back.setOnClickListener(new OnClickListener() {
//////					
//////					@Override
//////					public void onClick(View v) {
//////						// Remove modify service details
//////						((LinearLayout) lin_rootview).removeView(viewgroup_modifyserviceview);
//////						
//////						// load list view
//////						initListView();		
//////						
//////						// Add search view and list view
//////						((ViewGroup) lin_rootview).addView(searchView);
//////						((ViewGroup) lin_rootview).addView(listView);
//////						
//////						Webservice_GetModifyServiceList instance_submit = new Webservice_GetModifyServiceList(Activity_SliderMenu.context);
//////					    instance_submit.postData();
//////					}
//////				});
//////		        
//////		       
//////		        Button btn_submit = (Button)viewgroup_modifyserviceview.findViewById(R.id.btn_submit);
//////		        btn_submit.setOnClickListener(new OnClickListener() {
//////					
//////					@Override
//////					public void onClick(View v) {
//////						
//////						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity_SliderMenu.context);
//////				 
//////							// set title
//////							alertDialogBuilder.setTitle("Alert!");
//////				 
//////							// set dialog message
//////							alertDialogBuilder
//////								.setMessage("Are you sure, you want to submit?")
//////								.setCancelable(false)
//////								.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//////									public void onClick(DialogInterface dialog,int id) {
//////										// if this button is clicked, close current activity
////////										TextView tv = (TextView)spinner_changeto.getSelectedView();
////////										bandwidthOptions.get(tv.getText().toString());
////////										Log.d("tag", ""+bandwidthOptions.get(tv.getText().toString()));
//////										Log.d("tag", ""+bandwidthOptions.get(((TextView)spinner_changeto.getSelectedView()).getText().toString()).toString());
//////										Webservice_PostModifyDetails instance_submit = new Webservice_PostModifyDetails(Activity_SliderMenu.context, bundleId, (bandwidthOptions.get(((TextView)spinner_changeto.getSelectedView()).getText().toString())).toString() );
//////									    instance_submit.postData();
//////									    
////////										// Remove modify service details
////////										((LinearLayout) lin_rootview).removeView(viewgroup_modifyserviceview);
////////										
////////										// load list view
////////										initListView();		
////////										
////////										// Add search view and list view
////////										((ViewGroup) lin_rootview).addView(searchView);
////////										((ViewGroup) lin_rootview).addView(listView);
//////										
//////										
//////									}
//////								  })
//////								.setNegativeButton("No",new DialogInterface.OnClickListener() {
//////									public void onClick(DialogInterface dialog,int id) {
//////										// if this button is clicked, just close
//////										// the dialog box and do nothing
//////										dialog.cancel();
//////									}
//////								});
//////				 
//////								// create alert dialog
//////								AlertDialog alertDialog = alertDialogBuilder.create();
//////				 
//////								// show it
//////								alertDialog.show();
//////						 //Webservice_PostModifyDetails
//////						
//////
//////					}
//////				});
//////		 }
////	}//initServiceDetailsView
}