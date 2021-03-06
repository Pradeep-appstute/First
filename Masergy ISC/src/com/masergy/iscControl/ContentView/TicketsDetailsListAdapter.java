package com.masergy.iscControl.ContentView;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.masergy.iscControl.R;
import com.masergy.iscControl.ContentView.Fragment_Tickets.Comment;

public class TicketsDetailsListAdapter extends BaseAdapter implements
		ListAdapter {
	// Public
	Context mContext;
	ArrayList<Comment> comment_List;

	TicketsDetailsListAdapter(Context context, ArrayList<Comment> list) {
		mContext = context;
		this.comment_List = list ;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comment_List.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return comment_List.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.ticketdetails_view_list_item, parent, false);
			((TextView) convertView.findViewById(R.id.txtViewUserName)).setText(comment_List.get(position).userName);
			((TextView) convertView.findViewById(R.id.txtVieTimeStamp)).setText(DateTimeFormat.forPattern("MM/dd/yyyy").print(new DateTime(Long.parseLong(comment_List.get(position).timestamp))).toString());		
			((TextView) convertView.findViewById(R.id.txtViewDetails)).setText(comment_List.get(position).detail);
		}
		
		if (position%2==0)	
			convertView.setBackgroundColor(Color.rgb(230, 240, 246));
		else
			convertView.setBackgroundColor(Color.WHITE);
		
		((TextView) convertView.findViewById(R.id.txtViewUserName)).setText(comment_List.get(position).userName);
		((TextView) convertView.findViewById(R.id.txtVieTimeStamp)).setText(DateTimeFormat.forPattern("MM/dd/yyyy").print(new DateTime(Long.parseLong(comment_List.get(position).timestamp))).toString());		
		((TextView) convertView.findViewById(R.id.txtViewDetails)).setText(comment_List.get(position).detail);
		
		return convertView;
	}
}
