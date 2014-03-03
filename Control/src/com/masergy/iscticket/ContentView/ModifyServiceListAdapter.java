package com.masergy.iscticket.ContentView;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ModifyServiceListAdapter extends BaseAdapter implements
		ListAdapter {
	//Public
	Context mContext;
	ArrayList<ModifyService> modifyServiceList;
	
	ModifyServiceListAdapter(Context context, ArrayList<ModifyService> list)
	{
		mContext = context;
		modifyServiceList = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return modifyServiceList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return modifyServiceList.get(position);
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
			((TextView)convertView).setText(modifyServiceList.get(position).bundleId+" "+modifyServiceList.get(position).location);
			((TextView)convertView).setTextSize(16);
//			((TextView)convertView).setRelative(5, 5, 5, 5);
			}
		((TextView)convertView).setText(modifyServiceList.get(position).bundleId+" "+modifyServiceList.get(position).location);
		((TextView)convertView).setTextSize(16);
		
		return ((TextView)convertView);
	}

}
