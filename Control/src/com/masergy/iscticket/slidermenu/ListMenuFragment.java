package com.masergy.iscticket.slidermenu;

import java.util.ArrayList;
import java.util.List;

import com.masergy.iscticket.FragmentChangeActivity;
import com.masergy.iscticket.R;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class ListMenuFragment extends ListFragment {

	public final String[] titles = new String[] { "Tickets", "Modify Service", "Doppler IM", "Contact us", "Logout" };
	public static final Integer[] images = { R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout };
	List<RowItem> rowItems;
	   
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	   TextView tv_listTitle = new TextView(getActivity());
	   tv_listTitle.setText(getString(R.string.textViewListHeaderMSG));
	   tv_listTitle.setTextSize(25);
	   tv_listTitle.setTextColor(Color.WHITE);
	   tv_listTitle.setTypeface(null, Typeface.BOLD);
	   tv_listTitle.setBackgroundColor(Color.BLACK);
	   tv_listTitle.setGravity(Gravity.CENTER_HORIZONTAL);
//	   tv_listTitle.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
	   getListView().addHeaderView(tv_listTitle);
	   
//	   getListView().setBackgroundColor(Color.parseColor("#4E4E50"));
		
		//Create adaptor and set row title and icon
		rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < titles.length; i++) {
            RowItem item = new RowItem(images[i], titles[i]);
            rowItems.add(item);
        }
        
		ListAdapter listAdapter = new ListAdapter(getActivity(),rowItems);
		
		//set adapter to list view
		setListAdapter(listAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			
			newContent = new Activity_Fragment(R.color.red);
			break;
		case 1:
			newContent = new Activity_Fragment(R.color.green);
			break;
		case 2:
			newContent = new Activity_Fragment(R.color.blue);
			break;
		case 3:
			newContent = new Activity_Fragment(android.R.color.white);
			break;
		case 4:
			newContent = new Activity_Fragment(android.R.color.black);
			break;
		}
		if (newContent != null)
			switchFragment(newContent);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof FragmentChangeActivity) {
			FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
			fca.switchContent(fragment);
		} 
//			else if (getActivity() instanceof ResponsiveUIActivity) {
//			ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
//			ra.switchContent(fragment);
//		}
	}


}
