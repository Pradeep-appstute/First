package com.masergy.iscControl.ContentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.masergy.iscControl.Activity_SliderMenu;
import com.masergy.iscControl.R;
import com.masergy.iscControl.utility.Webservice_GetModifyServiceDetails;
import com.masergy.iscControl.utility.Webservice_GetModifyServiceList;
import com.masergy.iscControl.utility.Webservice_PostModifyDetails;

public class Fragment_ModifyService extends Fragment {

	static LinearLayout lin_rootview;
	static ViewGroup viewgroup_modifyserviceview;
	static Spinner spinner_changeto;
	static boolean tappedSpinnerChangeTo=false;

	ViewGroup viewgroup_servicedetails_view;
	public static ModifyServiceListAdapter listAdapter;
	public static LinearLayout lin_inputSearch;
	public static ListView listView;
	public static EditText inputSearch;
	public static LayoutInflater inflater;
	public static ViewGroup container;
	public static ArrayList<ModifyService> serviceList;
	public static String bundleId = null, prodType = null, location = null,
			currentBandwidth = null, contractBandwidth = null;
	static ArrayAdapter<String> dataAdapterSpinnerChangeTo;
	static ArrayList<String> list_bandwidth;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		this.container = container;

		// construct the RelativeLayout
		lin_rootview = (LinearLayout) inflater.inflate(
				R.layout.fragment_modifyservice, container, false);
		lin_rootview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
               		    InputMethodManager.HIDE_NOT_ALWAYS);
				return false;
			}
		});
		
		// get the listview
		lin_inputSearch = (LinearLayout) lin_rootview.findViewById(R.id.lin_inputSearch);
		listView = (ListView) lin_rootview.findViewById(R.id.lvExp);
		listView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
               		    InputMethodManager.HIDE_NOT_ALWAYS);
				return false;
			}
		});
		inputSearch = (EditText) lin_rootview.findViewById(R.id.inputSearch);
		inputSearch.addTextChangedListener(new TextWatcher() {
		     
		    @Override
		    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		        // When user changed the Text
		        listAdapter.getFilter().filter(cs);  
		    }
		     
		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		            int arg3) {
		        // TODO Auto-generated method stub
		         
		    }
		     
		    @Override
		    public void afterTextChanged(Editable arg0) {
		        // TODO Auto-generated method stub                         
		    }
		});

		 
		 SharedPreferences.Editor sharedPrefEditor =  Activity_SliderMenu.context.getSharedPreferences(Webservice_GetModifyServiceList.fileName,
				 Activity_SliderMenu.context.MODE_PRIVATE).edit();

		sharedPrefEditor.putString("modifyservice", null);
		sharedPrefEditor.commit();
		// load list view
		initListView();

		// ===========Menu Button===============
		Button toggleMenuButton = ((Button) lin_rootview
				.findViewById(R.id.activity_main_content_button_menu));
		toggleMenuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity_SliderMenu.slidingMenu.toggle();
				InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
               		    InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		return lin_rootview;
	}

	public static void initListView() {
		SharedPreferences prefs = Activity_SliderMenu.context
				.getSharedPreferences(Webservice_GetModifyServiceList.fileName,
						Activity_SliderMenu.context.MODE_PRIVATE);
		String modifyJSONStr = prefs.getString("modifyservice", null);
		if(serviceList==null)
		serviceList = new ArrayList<ModifyService>();
		serviceList.clear();
		if (modifyJSONStr != null) {
//			Log.d("tag", "modifyJSONStr" + modifyJSONStr);

			
//======			
			
			try {
				
				//Check if code and message has any message
				boolean flag=true;

//				Log.d("tag", "Pre----code");
				if(modifyJSONStr.contains("code") && modifyJSONStr.contains("message"))//jsonObj.has("code") && jsonObj.has("message"))
				{
					JSONArray jsonArray = new JSONArray(modifyJSONStr);
					/*
					 [
							{
							"code": 1000,
							"message": "Validation Field Problem: Bundle can not be modified at this time, reason: Unknown"
							}
					 ]
					 */
					JSONObject jsonObj = jsonArray.getJSONObject(0);
//					Log.d("tag", "intermediate----code");

					//if((jsonObj.getInt("code")>0) && (jsonObj.get("message").toString().length()>0))
					{
//					Log.d("tag", "Post----code");
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							Activity_SliderMenu.context);
			 
						// set title
						alertDialogBuilder.setTitle("Alert!");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("This service is not modifiable.")
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
				}
				else
				{
			
			// Convert string to JSONArray
			
				JSONArray jsonArray = new JSONArray(modifyJSONStr);
				JSONObject jsonObj;
				// Getting JSON Array node
				for (int i = 0; i < jsonArray.length(); i++) {

					jsonObj = jsonArray.getJSONObject(i);
					ModifyService service = new ModifyService();
					/*
					 * Log.d("tag", "" + jsonObj.get("bundleId")); Log.d("tag",
					 * "" + jsonObj.get("bundleAlias")); Log.d("tag", "" +
					 * jsonObj.get("currentBandwidth")); Log.d("tag", "" +
					 * jsonObj.get("location"));
					 */
					if (!(jsonObj.get("bundleId").equals(JSONObject.NULL)))
						service.bundleId = jsonObj.getString("bundleId");
					else
						service.bundleId = "-1";

					if (!(jsonObj.get("bundleAlias").equals(JSONObject.NULL)))
						service.bundleAlias = jsonObj.getString("bundleAlias");
					else
						service.bundleAlias = "";

					if (!(jsonObj.get("currentBandwidth")
							.equals(JSONObject.NULL)))
						service.currentBandwidth = jsonObj
								.getString("currentBandwidth");
					else
						service.currentBandwidth = "-1";

					if (!(jsonObj.get("location").equals(JSONObject.NULL)))
						service.location = jsonObj.getString("location");
					else
						service.location = "-1";

					serviceList.add(service);

				}// for

			listAdapter = new ModifyServiceListAdapter(
					Activity_SliderMenu.context, serviceList);
			listView.setAdapter(listAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Toast.makeText(Activity_SliderMenu.context,
					// ""+serviceList.get(position).bundleId,
					// Toast.LENGTH_SHORT).show();
					Webservice_GetModifyServiceDetails instance = new Webservice_GetModifyServiceDetails(
							Activity_SliderMenu.context, ""
									+ ModifyServiceListAdapter.filtered_modifyServiceList.get(position).bundleId);
					instance.postData();
				}
			});
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//=====			
		}//if (modifyJSONStr != null)
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					Activity_SliderMenu.context);
	 
				// set title
				alertDialogBuilder.setTitle("Error!");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Unable to get details, Please contact service desk")
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
//					alertDialog.show();
		}
	}// initListView

	public static void initServiceDetailsView() {
		tappedSpinnerChangeTo=false;
		final SharedPreferences prefs = Activity_SliderMenu.context.getSharedPreferences(Webservice_GetModifyServiceList.fileName, Activity_SliderMenu.context.MODE_PRIVATE); 
		 final String modifyJSONStr = prefs.getString("modifyservicedetails", null);
		 if (modifyJSONStr != null) 
		 {

				if(modifyJSONStr.contains("code") && modifyJSONStr.contains("message"))//jsonObj.has("code") && jsonObj.has("message"))
				{
					JSONArray jsonArray;
					try {
						jsonArray = new JSONArray(modifyJSONStr);
					
					/*
					 [
							{
							"code": 1000,
							"message": "Validation Field Problem: Bundle can not be modified at this time, reason: Unknown"
							}
					 ]
					 */
					JSONObject jsonObj = jsonArray.getJSONObject(0);
//					Log.d("tag", "intermediate----code");

					//if((jsonObj.getInt("code")==1000) && (jsonObj.get("message").toString().length()>0))
					{
//					Log.d("tag", "Post----code");
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							Activity_SliderMenu.context);
			 
						// set title
						alertDialogBuilder.setTitle("Alert!");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("This service is not modifiable.")
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
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
			 
			 
			 //Log.d("tag", "modifyJSONStr="+modifyJSONStr);
			 /*
			  {
  "bundleId": "MB098596",
  "prodType": "NxT3",
  "location": "Plano, TX",
  "currentBandwidth": "42 Mbps",
  "contractBandwidth": "7.5 Mbps",
  "bandwidthOptions": [
    {
      "value": "171",
      "label": "33 Mbps"
    },
    {
      "value": "1074",
      "label": "35 Mbps"
    },
    {
      "value": "172",
      "label": "36 Mbps"
    },
    {
      "value": "173",
      "label": "39 Mbps"
    },
    {
      "value": "1075",
      "label": "40 Mbps"
    },
    {
      "value": "175",
      "label": "45 Mbps"
    }
  ]
}
			  */
			 
			 
			 final HashMap bandwidthOptions = new HashMap();
			 list_bandwidth = new ArrayList<String>();         
				// Convert string to JSONArray
				JSONObject jsonRootObj;
				JSONArray jsonArray;
				JSONObject jsonObj;
				try {
					jsonRootObj = new JSONObject(modifyJSONStr);
					
					bundleId = jsonRootObj.getString("bundleId");
					prodType = jsonRootObj.getString("prodType");
					location = jsonRootObj.getString("location");
				    currentBandwidth = jsonRootObj.getString("currentBandwidth");
				    contractBandwidth = jsonRootObj.getString("contractBandwidth");
				    jsonArray = jsonRootObj.getJSONArray("bandwidthOptions");
				    
				    list_bandwidth.add("");
					// Getting JSON Array node
					for (int i = 0; i < jsonArray.length(); i++) {

						jsonObj = jsonArray.getJSONObject(i);
						bandwidthOptions.put( ""+jsonObj.getString("label").toString(),""+jsonObj.getString("value").toString());
						list_bandwidth.add(jsonObj.getString("label").toString());
					}// for	
					
//					  // Get a set of the entries
//				      Set set = bandwidthOptions.entrySet();
//				      // Get an iterator
//				      Iterator i = set.iterator();
//				      // Display elements
//				      while(i.hasNext()) {
//				         Map.Entry me = (Map.Entry)i.next();
//				         System.out.print(me.getKey() + ": ");
//				         System.out.println(me.getValue());
//				      }
				      
				     
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			 
				//Add modify service detail view 
				// Remove expandable searchview and list view
				((LinearLayout) lin_rootview).removeView(lin_rootview.findViewById(R.id.lin_inputSearch));
				((LinearLayout) lin_rootview).removeView(lin_rootview.findViewById(R.id.lvExp));
				
				// Add submitview
				viewgroup_modifyserviceview = (ViewGroup) ((Activity) Activity_SliderMenu.context).getLayoutInflater().inflate(R.layout.modifyservicedetail, (ViewGroup) lin_rootview,false);
				((ViewGroup) lin_rootview).addView(viewgroup_modifyserviceview);
				
//				lin_fragment_modifyservicedetails
				
				TextView tv_bundleid = (TextView)viewgroup_modifyserviceview.findViewById(R.id.txtview_bundleid);
				tv_bundleid.setText(tv_bundleid.getText() + bundleId);
				

				TextView tv_services = (TextView)viewgroup_modifyserviceview.findViewById(R.id.txtview_services);
				tv_services.setText(tv_services.getText() + prodType);
				

				TextView tv_site = (TextView)viewgroup_modifyserviceview.findViewById(R.id.txtview_site);
				tv_site.setText(tv_site.getText() + location);
				

				TextView tv_contract = (TextView)viewgroup_modifyserviceview.findViewById(R.id.txtView_contract);
				tv_contract.setText(contractBandwidth);
				
				TextView tv_current = (TextView)viewgroup_modifyserviceview.findViewById(R.id.txtView_current);
				tv_current.setText(currentBandwidth);
				
		        spinner_changeto = (Spinner)viewgroup_modifyserviceview.findViewById(R.id.spinner_changeto);
			 
				dataAdapterSpinnerChangeTo = new ArrayAdapter<String>(Activity_SliderMenu.context, R.layout.modifyservice_spinner_item, list_bandwidth);
				dataAdapterSpinnerChangeTo.setDropDownViewResource(R.layout.spinner_layout);
				spinner_changeto.setAdapter(dataAdapterSpinnerChangeTo);
				tappedSpinnerChangeTo=false;
				spinner_changeto.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						tappedSpinnerChangeTo=true;
//						Toast.makeText(Activity_SliderMenu.context, "Touch", Toast.LENGTH_SHORT).show();
						JSONObject jsonRootObj;
						JSONArray jsonArray;
						JSONObject jsonObj;
						
						try {
							jsonRootObj = new JSONObject(modifyJSONStr);
						    jsonArray = jsonRootObj.getJSONArray("bandwidthOptions");
						    
							// Getting JSON Array node
						    
						    if (list_bandwidth!=null)
						    	list_bandwidth.clear();
							for (int i = 0; i < jsonArray.length(); i++) {
								jsonObj = jsonArray.getJSONObject(i);
								bandwidthOptions.put( ""+jsonObj.getString("label").toString(),""+jsonObj.getString("value").toString());
								list_bandwidth.add(jsonObj.getString("label").toString());
//								Log.d("tag", "ChangeTo: "+jsonObj.getString("label").toString());
							}// for	 
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						dataAdapterSpinnerChangeTo = new ArrayAdapter<String>(Activity_SliderMenu.context, R.layout.modifyservice_spinner_item, list_bandwidth);
						dataAdapterSpinnerChangeTo.setDropDownViewResource(R.layout.spinner_layout);
						spinner_changeto.setAdapter(dataAdapterSpinnerChangeTo);
						
						dataAdapterSpinnerChangeTo.notifyDataSetChanged();
						
//						Toast.makeText(Activity_SliderMenu.context, "Touch End", Toast.LENGTH_SHORT).show();
						return false;
					}
				});
		        spinner_changeto.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int pos, long id) {
						// TODO Auto-generated method stub
						//Toast.makeText(parent.getContext(), "spinner Bundle", Toast.LENGTH_SHORT).show();
						//txt_bundleid = spinner_changeto.getSelectedItem().toString();
						//Toast.makeText(parent.getContext(), "txt_bundleid="+txt_bundleid,Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});

		        ImageButton btn_back = (ImageButton)viewgroup_modifyserviceview.findViewById(R.id.btn_back);
		        btn_back.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// Remove modify service details
						((LinearLayout) lin_rootview).removeView(viewgroup_modifyserviceview);
						
						// load list view
						initListView();		
						
						// Add search view and list view
						((ViewGroup) lin_rootview).addView(lin_inputSearch);
						((ViewGroup) lin_rootview).addView(listView);
						listAdapter.getFilter().filter(inputSearch.getText().toString()); //To retain search history  
						//Webservice_GetModifyServiceList instance_submit = new Webservice_GetModifyServiceList(Activity_SliderMenu.context);
					    //instance_submit.postData();
					}
				});
		        
		       
		        ImageButton btn_submit = (ImageButton)viewgroup_modifyserviceview.findViewById(R.id.btn_submit);
		        btn_submit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//Toast.makeText(Activity_SliderMenu.context, "tappedSpinnerChangeTo="+tappedSpinnerChangeTo, Toast.LENGTH_SHORT).show();
						if(tappedSpinnerChangeTo)
						{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity_SliderMenu.context);
				 
							// set title
							alertDialogBuilder.setTitle("Alert!");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("Are you sure you want to submit?")
								.setCancelable(false)
								.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, close current activity
										//Log.d("tag", ""+bandwidthOptions.get(((TextView)spinner_changeto.getSelectedView()).getText().toString()).toString());
										Webservice_PostModifyDetails instance_submit = new Webservice_PostModifyDetails(Activity_SliderMenu.context, bundleId, (bandwidthOptions.get(((TextView)spinner_changeto.getSelectedView()).getText().toString())).toString() );
									    instance_submit.postData();										
									}
								  })
								.setNegativeButton("No",new DialogInterface.OnClickListener() {
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
						 //Webservice_PostModifyDetails
						}
						else
						{
							//If changeto feild is not tapped 
							//Warning! : Please select bandwidth.     OK
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity_SliderMenu.context);
							 
							// set title
							alertDialogBuilder.setTitle("Warning!");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("Please select bandwidth.")
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

					}
				});
		 }
		 }
	}//initServiceDetailsView
}
