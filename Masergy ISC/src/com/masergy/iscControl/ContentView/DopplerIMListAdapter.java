package com.masergy.iscControl.ContentView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.masergy.iscControl.R;

public class DopplerIMListAdapter extends BaseExpandableListAdapter {
	
//	DateTimeFormatter dateTimeFormatter;
	private Context _context;
	ArrayList<DopplerIM_Parent> original_dopplerIM_Parents;
	public static ArrayList<DopplerIM_Parent> filtered_dopplerIM_Parents;
	LayoutInflater mInflater;
	ItemFilter mFilter = new ItemFilter();

	public DopplerIMListAdapter(Context context, ArrayList<DopplerIM_Parent> dopplerIM_Parents){
		this._context = context;
		this.original_dopplerIM_Parents = dopplerIM_Parents ;
		this.filtered_dopplerIM_Parents = dopplerIM_Parents ;
		mInflater = LayoutInflater.from(context);
//		dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return filtered_dopplerIM_Parents.size();
	}
	
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return filtered_dopplerIM_Parents.get(position);
	}

	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		 //Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
        return filtered_dopplerIM_Parents.get(groupPosition).getChildren().get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		 /****** When Child row clicked then this function call *******/
		return childPosition;
	}


    // getFilter Method
	public Filter getFilter() {
		return mFilter;
	}
	
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final DopplerIM_Parent dopplerim_parent = filtered_dopplerIM_Parents.get(groupPosition);
        final DopplerIM_Child dopplerim_child = dopplerim_parent.getChildren().get(childPosition);


        // Inflate childrow.xml file for child rows
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.dopplerim_childrow, null);
		}

//		Log.d("tag", "childPosition%2==0="+(childPosition%2==0));
		if (childPosition%2!=0)	
			convertView.setBackgroundColor(Color.rgb(230, 240, 246));
		else
			convertView.setBackgroundColor(Color.WHITE);
		
		// Get childrow.xml file elements and set values
		String main_str = dopplerim_child.getchildText();

        StringTokenizer stt = new StringTokenizer(main_str,":");
        String[] str_array = new String[2];
        int i=0;
        while (stt.hasMoreTokens()){
            String token = stt.nextToken();
            str_array[i++]=token;
            System.out.println(token);
        }
		
		((TextView) convertView.findViewById(R.id.text_childlabel)).setText(str_array[0]+": ");
		((TextView) convertView.findViewById(R.id.text_child)).setText(str_array[1]);		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size=0;
        if(filtered_dopplerIM_Parents.get(groupPosition).getChildren()!=null)
            size = filtered_dopplerIM_Parents.get(groupPosition).getChildren().size();
        return size;
	}

	@Override
	public Object getGroup(int groupPosition) {
//		Log.i("Parent", groupPosition+"=  getGroup ");
		return this.filtered_dopplerIM_Parents.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.filtered_dopplerIM_Parents.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		//Call when parent row clicked
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		DopplerIM_Parent dParent = (DopplerIM_Parent)getGroup(groupPosition);
		String headerTitle = dParent.getName();
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.dopplerim_grouprow, null);
		}

//		Log.d("tag", "childPosition%2==0="+(position%2==0));
		if (groupPosition%2==0)	
			convertView.setBackgroundColor(Color.rgb(230, 240, 246));
		else
			convertView.setBackgroundColor(Color.WHITE);
		
		TextView lblListHeader = (TextView) convertView.findViewById(R.id.txtViewParent);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	 @Override
     public void notifyDataSetChanged()
     {
         // Refresh List rows
         super.notifyDataSetChanged();
     }
	
	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();
			final ArrayList<DopplerIM_Parent> list = original_dopplerIM_Parents;

			int count = list.size();
			final ArrayList<DopplerIM_Parent> new_list = new ArrayList<DopplerIM_Parent>(count);

			DopplerIM_Parent filterableString;
			for (int i = 0; i < count; i++) {
				filterableString = list.get(i);
				if (filterableString.name.toString().toLowerCase().contains(filterString)) {
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
			filtered_dopplerIM_Parents = (ArrayList<DopplerIM_Parent>) results.values;
			
			notifyDataSetChanged();
		}

	}//eof class ItemFilter
}

