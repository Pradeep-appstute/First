package com.masergy.iscticket.MenuView;

import java.util.ArrayList;
import java.util.List;

import com.masergy.iscticket.R;
import com.masergy.iscticket.MenuView.RowItem;
import com.masergy.iscticket.R.drawable;
import com.masergy.iscticket.R.layout;
import com.masergy.iscticket.R.string;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ListFragment_ListMenu extends ListFragment {
	
	public final String[] titles = new String[] { "Tickets", "Modify Service", "Doppler IM", "Contact us", "Logout" };
	public static final Integer[] images = { R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout };
	List<RowItem> rowItems;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.listfragment_listmenu, container, false);
    }
 
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
 	   TextView tv_listTitle = new TextView(getActivity());
 	   tv_listTitle.setText(getString(R.string.textViewListHeaderMSG));
 	   tv_listTitle.setTextSize(25);
 	   tv_listTitle.setTextColor(Color.WHITE);
 	   tv_listTitle.setTypeface(null, Typeface.BOLD);
 	   tv_listTitle.setBackgroundColor(Color.BLACK);
 	   tv_listTitle.setGravity(Gravity.CENTER_HORIZONTAL);
 	   getListView().addHeaderView(tv_listTitle);
 	   
 		//Create adaptor and set row title and icon
 		rowItems = new ArrayList<RowItem>();
         for (int i = 0; i < titles.length; i++) {
             RowItem item = new RowItem(images[i], titles[i]);
             rowItems.add(item);
         }
         
 		BaseAdapter_ListMenu listAdapter = new BaseAdapter_ListMenu(getActivity(),rowItems);
 		
 		//set adapter to list view
 		setListAdapter(listAdapter);
    }
 
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		Log.d("TAG", "Position="+position);
		//Hack
		position--;
		switch (position) {
		case 0:
			//newContent = new Activity_Fragment(0);

			break;
		case 1:
			//newContent = new Activity_Fragment(1);
			break;
		case 2:
			//newContent = new Activity_Fragment(2);
			break;
		case 3:
			//newContent = new Activity_Fragment(3);
			break;
		case 4:
			//newContent = new Activity_Fragment(4);
			break;
		}
		//if (newContent != null)
			//switchFragment(newContent);
	}

//	// the meat of switching the above fragment
//	private void switchFragment(Fragment fragment) {
//		if (getActivity() == null)
//			return;
//		
//		if (getActivity() instanceof FragmentChangeActivity) {
//			FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
//			fca.switchContent(fragment);
//		} 
//
//	}
}
