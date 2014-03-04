package com.masergy.iscticket.ContentView;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.masergy.iscticket.Activity_SliderMenu;
import com.masergy.iscticket.R;
import com.masergy.iscticket.utility.Webservice_GetModifyServiceList;

public class Fragment_ModifyService extends Fragment {
 
	LinearLayout lin_rootview;
	ViewGroup viewgroup_servicedetails_view;
	static ListAdapter listAdapter;
	static ListView listView;
	static ArrayList<ModifyService> serviceList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// construct the RelativeLayout
		lin_rootview = (LinearLayout) inflater.inflate(
				R.layout.fragment_modifyservice, container, false);
		
		// get the listview
		listView = (ListView) lin_rootview.findViewById(R.id.lvExp);

		// load list view
		initListView();		

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
	}

	public static void initListView() {
		SharedPreferences prefs = Activity_SliderMenu.context.getSharedPreferences(Webservice_GetModifyServiceList.fileName, Activity_SliderMenu.context.MODE_PRIVATE); 
		 String modifyJSONStr = prefs.getString("modifyservice", null);
		 serviceList = new ArrayList<ModifyService>();
		 serviceList.clear();
		 if (modifyJSONStr != null) 
		 {
			 Log.d("tag", "modifyJSONStr"+modifyJSONStr);
			 
				// Convert string to JSONArray
				JSONArray jsonArray;
				try {
					jsonArray = new JSONArray(modifyJSONStr);
					// Getting JSON Array node
					for (int i = 0; i < jsonArray.length(); i++) {

						JSONObject jsonObj = jsonArray.getJSONObject(i);
						ModifyService service = new ModifyService();
						/*
						 * Log.d("tag", "" + jsonObj.get("bundleId"));
						 * Log.d("tag", "" + jsonObj.get("bundleAlias"));
						 * Log.d("tag", "" + jsonObj.get("currentBandwidth"));
						 * Log.d("tag", "" + jsonObj.get("location"));
						 */
						if (!(jsonObj.get("bundleId").equals(JSONObject.NULL)))
							service.bundleId = jsonObj.getString("bundleId");
						else
							service.bundleId = "-1";

						if (!(jsonObj.get("bundleAlias").equals(JSONObject.NULL)))
							service.bundleAlias = jsonObj.getString("bundleAlias");
						else
							service.bundleAlias = "";

						if (!(jsonObj.get("currentBandwidth").equals(JSONObject.NULL)))
							service.currentBandwidth = jsonObj.getString("currentBandwidth");
						else
							service.currentBandwidth = "-1";

						
						if (!(jsonObj.get("location").equals(JSONObject.NULL)))
							service.location = jsonObj.getString("location");
						else
							service.location = "-1";
						
						serviceList.add(service);
						
					}// for	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 listAdapter = new ModifyServiceListAdapter(Activity_SliderMenu.context, serviceList); 
			 listView.setAdapter(listAdapter);
			 listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					//Toast.makeText(Activity_SliderMenu.context, ""+serviceList.get(position).bundleId, Toast.LENGTH_SHORT).show();
					
				}
			});
		 }
	}//initListView
}




