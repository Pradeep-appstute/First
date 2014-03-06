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
	
	DateTimeFormatter dateTimeFormatter;
	private Context _context;
	private List<DopplerIM_Parent> _listDataHeader; // header titles

	public DopplerIMListAdapter(Context context, List<DopplerIM_Parent> listDataHeader) 
	{
		this._context = context;
		this._listDataHeader = listDataHeader;	
		dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataHeader.get(groupPosition).child;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final DopplerIM_Child child = (DopplerIM_Child) getChild(groupPosition, childPosition);
if(child==null)
{
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.dopplerim_childrow, null);
		}

		Log.d("tag", "childPosition%2==0="+(childPosition%2==0));
		if (childPosition%2==0)	
			convertView.setBackgroundColor(Color.rgb(230, 240, 246));
		else
			convertView.setBackgroundColor(Color.WHITE);
		
		TextView txtid = (TextView) convertView.findViewById(R.id.text_id);
		TextView txtname = (TextView) convertView.findViewById(R.id.text_name);
		TextView txtalarmstate = (TextView) convertView.findViewById(R.id.text_alarmState);
		TextView txttype = (TextView) convertView.findViewById(R.id.text_type);
		TextView txtcreatedate = (TextView) convertView.findViewById(R.id.text_createDate);
		TextView txtcloudid = (TextView) convertView.findViewById(R.id.text_cloudId);
		TextView txtcloudname = (TextView) convertView.findViewById(R.id.text_cloudName);
		TextView txtipaddress = (TextView) convertView.findViewById(R.id.text_ipAddress);
		TextView txtsite = (TextView) convertView.findViewById(R.id.text_site);
		TextView txtcreditpointsused = (TextView) convertView.findViewById(R.id.text_creditPointsUsed);
		TextView txtassetmanufacturer = (TextView) convertView.findViewById(R.id.text_assetManufacturer);
		TextView txtassetmodel = (TextView) convertView.findViewById(R.id.text_assetModel);
		
		
		 txtid.setText(child.id);
		 txtname.setText(child.name);
		 txtalarmstate.setText(child.alarmState);
		 txttype.setText(child.type);
		 txtcreatedate.setText(child.createDate);
		 txtcloudid.setText(child.cloudId);
		 txtcloudname.setText(child.cloudName);
		 txtipaddress.setText(child.ipAddress);
		 txtsite.setText(child.site);
		 txtcreditpointsused.setText(child.creditPointsUsed);
		 txtassetmanufacturer.setText(child.assetManufacturer);
		 txtassetmodel.setText(child.assetModel);
}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
//		return this._listDataHeader.size();
		return 0;
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
