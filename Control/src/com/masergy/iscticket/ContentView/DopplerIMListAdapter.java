package com.masergy.iscticket.ContentView;

import com.masergy.iscticket.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class DopplerIMListAdapter extends BaseExpandableListAdapter {
/*
	//Public
	Context mContext;
	ArrayList<DopplerIM_Parent> dopplerIMList;
	
	DopplerIMListAdapter(Context context, ArrayList<DopplerIM_Parent> list)
	{
		mContext = context;
		dopplerIMList = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dopplerIMList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dopplerIMList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = new TextView(mContext);
			
			((TextView)convertView).setText(dopplerIMList.get(position).name);
			((TextView)convertView).setTextSize(16);
			}
		((TextView)convertView).setText(dopplerIMList.get(position).name);
		((TextView)convertView).setTextSize(16);
//		((TextView)convertView).setBackgroundResource(mContext.getResources().getDrawable(R.drawable.));
//		Log.d("tag", "item="+dopplerIMList.get(position).name);
		return ((TextView)convertView);
	}
*/
	
	DateTimeFormatter dateTimeFormatter;
	private Context _context;
	private List<DopplerIM_Parent> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<DopplerIM_Child>> _listDataChild;

	public DopplerIMListAdapter(Context context, List<DopplerIM_Parent> listDataHeader, HashMap<String, List<DopplerIM_Child>> listChildData) 
	{
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;	
		dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

//		final Ticket ticket = (Ticket) getChild(groupPosition, childPosition);
//
//		if (convertView == null) {
//			LayoutInflater infalInflater = (LayoutInflater) this._context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = infalInflater.inflate(R.layout.list_item, null);
//		}
//
//		Log.d("tag", "childPosition%2==0="+(childPosition%2==0));
//		if (childPosition%2==0)	
//			convertView.setBackgroundColor(Color.rgb(230, 240, 246));
//		else
//			convertView.setBackgroundColor(Color.WHITE);
//		
//		TextView txtTicketNum = (TextView) convertView
//				.findViewById(R.id.lblTicketNum);
//		TextView txtDateCreated = (TextView) convertView
//				.findViewById(R.id.lblDateCreated);
//		TextView txtSubject = (TextView) convertView
//				.findViewById(R.id.lblSubject);
//
//		txtTicketNum.setText(ticket.ticketId);
//		txtDateCreated.setText(dateTimeFormatter.print(new DateTime(Long.parseLong(ticket.createDate))).toString());
//		txtSubject.setText(ticket.subject);
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		DopplerIM_Parent dParent = (DopplerIM_Parent)getGroup(groupPosition);
		String headerTitle = dParent.name;
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.dopplerim_grouprow, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.txtViewParent);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
