package com.masergy.isc.ContentView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.masergy.isc.ContentView.ModifyService;

public class ModifyServiceListAdapter extends BaseAdapter implements
		ListAdapter {
	// Public
	Context mContext;
	ArrayList<ModifyService> original_modifyServiceList;
	public static ArrayList<ModifyService> filtered_modifyServiceList;
	//---------For Filter----------
	LayoutInflater mInflater;
	ItemFilter mFilter = new ItemFilter();
//	List<String>originalData = null;
//	List<String>filteredData = null;
	//-----------------------------
	ModifyServiceListAdapter(Context context, ArrayList<ModifyService> list) {
		mContext = context;
		this.original_modifyServiceList = list ;
		this.filtered_modifyServiceList = list ;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filtered_modifyServiceList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return filtered_modifyServiceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new TextView(mContext);
			convertView.setPadding(10, 15, 10, 15);
			((TextView) convertView)
					.setText(filtered_modifyServiceList.get(position).bundleId + " "
							+ filtered_modifyServiceList.get(position).location);
			((TextView) convertView).setTextSize(16);
			// ((TextView)convertView).setRelative(5, 5, 5, 5);
		}
		
//		Log.d("tag", "childPosition%2==0="+(position%2==0));
		if (position%2==0)	
			convertView.setBackgroundColor(Color.rgb(230, 240, 246));
		else
			convertView.setBackgroundColor(Color.WHITE);
		
		((TextView) convertView)
				.setText(filtered_modifyServiceList.get(position).bundleId + " "
						+ filtered_modifyServiceList.get(position).location);
		((TextView) convertView).setTextSize(16);

		return ((TextView) convertView);
	}

	public Filter getFilter() {
		return mFilter;
	}

	public class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();
			final ArrayList<ModifyService> list = original_modifyServiceList;

			int count = list.size();
			final ArrayList<ModifyService> new_list = new ArrayList<ModifyService>(count);

			ModifyService filterableString;
			for (int i = 0; i < count; i++) {
				filterableString = list.get(i);
				if (filterableString.bundleId.toString().toLowerCase().contains(filterString) || filterableString.location.toString().toLowerCase().contains(filterString)) {
					new_list.add(filterableString);
				}
			}
			results.values = new_list;
			results.count = new_list.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			filtered_modifyServiceList = (ArrayList<ModifyService>) results.values;
			
			notifyDataSetChanged();
		}

	}//eof class ItemFilter
}
