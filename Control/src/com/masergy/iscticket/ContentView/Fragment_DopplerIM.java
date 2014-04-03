package com.masergy.iscticket.ContentView;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.masergy.iscticket.Activity_SliderMenu;
import com.masergy.iscticket.R;
import com.masergy.iscticket.utility.Webservice_GetDopplerIMNodeDetails;

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
	public static EditText inputSearch;
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
				InputMethodManager inputManager = (InputMethodManager) Activity_SliderMenu.context.getSystemService(Context.INPUT_METHOD_SERVICE); 
                inputManager.hideSoftInputFromWindow(lin_rootview.getWindowToken(),      
               		    InputMethodManager.HIDE_NOT_ALWAYS);
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
				//=====Collapse all childs expect clicked one======
				int c=listAdapter.getGroupCount();
				for(int i=0;i<c;i++)
				{
					if(groupPosition!=i)
					{
						parent.collapseGroup(i);
					}
					
					else {
						if (parent.isGroupExpanded(groupPosition)) {
							parent.collapseGroup(groupPosition);
						}
						else
						{
							parent.expandGroup(i);
						}
						
					}
				}
				expListView.setSelectionFromTop(groupPosition, 0);
				//=======================END========================
				
				
				//expandbleLis.expandGroup(arg2);
				
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
				// 1. Fetching id
				Log.d("tag", "dopplerIM_Parents.get(groupPosition).getChildren()="+dopplerIM_Parents.get(groupPosition).getChildren());
				if(DopplerIMListAdapter.filtered_dopplerIM_Parents.get(groupPosition).getChildren()==null)
				{
//					Toast.makeText(Activity_SliderMenu.context, "id="+dopplerIM_Parents.get(groupPosition).id, Toast.LENGTH_SHORT).show();
					new Webservice_GetDopplerIMNodeDetails(Activity_SliderMenu.context, DopplerIMListAdapter.filtered_dopplerIM_Parents.get(groupPosition).id, groupPosition).postData();
					expListView.setSelection(groupPosition);
				}
				else if (DopplerIMListAdapter.filtered_dopplerIM_Parents.get(groupPosition).getChildren().size()==0)
				{
					Toast.makeText(Activity_SliderMenu.context, "id="+dopplerIM_Parents.get(groupPosition).id, Toast.LENGTH_SHORT).show();
					new Webservice_GetDopplerIMNodeDetails(Activity_SliderMenu.context, DopplerIMListAdapter.filtered_dopplerIM_Parents.get(groupPosition).id, groupPosition).postData();
//					
					//For scroll tapped list view item at top (or as a first list view item)
					expListView.setSelection(groupPosition);
					int offset = groupPosition - expListView.getFirstVisiblePosition();
		            if(expListView.getFirstVisiblePosition() > 0)
		                offset -= 1;

		            expListView.smoothScrollByOffset(offset);
				}
				else
				{
					if(expListView.isGroupExpanded(groupPosition))
						//Collapse
						expListView.collapseGroup(groupPosition);
					else
					{
						//Expand
						expListView.expandGroup(groupPosition);
//						expListView.setSelection(groupPosition);
						int offset = groupPosition - expListView.getFirstVisiblePosition();
			            if(expListView.getFirstVisiblePosition() > 0)
			                offset -= 1;

			            expListView.smoothScrollByOffset(offset);
					}
				}
				
			
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
//				Log.d("tag", "groupPosition="+groupPosition);
//				expListView.setSelection(groupPosition);
				int offset = groupPosition - expListView.getFirstVisiblePosition();
	            if(expListView.getFirstVisiblePosition() > 0)
	                offset -= 1;

	            expListView.smoothScrollByOffset(offset);
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
}
