package com.masergy.iscticket.ContentView;

import in.appstute.androidlibrary.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class DopplerIMListAdapter extends BaseAdapter implements
		ListAdapter {
	//Public
	Context mContext;
	ArrayList<DopplerIM> dopplerIMList;
	
	DopplerIMListAdapter(Context context, ArrayList<DopplerIM> list)
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

}
