package com.masergy.iscControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.masergy.iscControl.R;
import com.masergy.iscControl.MenuView.ListFragment_ListMenu;
import com.masergy.iscControl.MenuView.RowItem;
import com.masergy.iscControl.utility.Webservice_Logout;

public class Activity_Home extends Activity {
	//public final String[] titles = new String[] { "Tickets", "Modify Service", "Doppler IM", "Contact us", "Logout" };
	//public static final Integer[] images = { R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout };
	List<String> menuListLabel;
	List<Integer> menuListImages;
	List<RowItem> rowItems;
	
	ListView listView;
	
	public static final List<Integer> images_ticketsselected = new ArrayList<Integer>(Arrays.asList(new Integer[]{ R.drawable.ic_tickets_w, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout }));
	public static final List<Integer> images_modifyserviceselected = new ArrayList<Integer>(Arrays.asList(new Integer[]{ R.drawable.ic_tickets, R.drawable.ic_modifyservice_w, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout }));
	public static final List<Integer> images_dopplerimselected = new ArrayList<Integer>(Arrays.asList(new Integer[]{ R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim_w, R.drawable.ic_contactus, R.drawable.ic_logout }));
	public static final List<Integer> images_contactusselected = new ArrayList<Integer>(Arrays.asList(new Integer[]{ R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus_w, R.drawable.ic_logout }));
	public static final List<Integer> images_logoutselected = new ArrayList<Integer>(Arrays.asList(new Integer[]{ R.drawable.ic_tickets, R.drawable.ic_modifyservice, R.drawable.ic_dopplerim, R.drawable.ic_contactus, R.drawable.ic_logout_w }));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		menuListLabel = new ArrayList<String>();
		menuListImages = new ArrayList<Integer>();
		SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
////=================================For testing purpose only==================================================================		
//		SharedPreferences.Editor editor = prefs.edit();	
//		editor.putString("permViewTicket", "true");
//		editor.putString("permSubmitTicket", "true");
//		editor.putString("permModifyTierNetworkAccess", "true");
//		editor.putString("permViewServiceDetails", "true");
//		editor.commit();
////===========================================================================================================================
//		//Reset
//		ListFragment_ListMenu.menuListLabel.clear();
//		ListFragment_ListMenu.menuListImages.clear();
		
		if(prefs.getString("permViewTicket", "true").equals("true") || prefs.getString("permSubmitTicket", "true").equals("true"))
		{
			if(menuListImages.size()<5)
			{
			menuListLabel.add("Tickets");
			menuListImages.add(R.drawable.ic_tickets);
			}
			
			if(ListFragment_ListMenu.menuListImages.size()<5)
			{
			ListFragment_ListMenu.menuListLabel.add("Tickets");
			ListFragment_ListMenu.menuListImages.add(R.drawable.ic_tickets);
			}
		}
		     
		if(prefs.getString("permModifyTierNetworkAccess", "true").equals("true"))
		{
			if(menuListImages.size()<5)
			{
			menuListLabel.add("Modify Service");
			menuListImages.add(R.drawable.ic_modifyservice);
			}
			if(ListFragment_ListMenu.menuListImages.size()<5)
			{
			ListFragment_ListMenu.menuListLabel.add("Modify Service");
			ListFragment_ListMenu.menuListImages.add(R.drawable.ic_modifyservice);
			}
		}
		if(prefs.getString("permViewServiceDetails", "true").equals("true"))
		{
			if(menuListImages.size()<5)
			{
			menuListLabel.add("Doppler IM");
			menuListImages.add(R.drawable.ic_dopplerim);
			}
			if(ListFragment_ListMenu.menuListImages.size()<5)
			{
			ListFragment_ListMenu.menuListLabel.add("Doppler IM");
			ListFragment_ListMenu.menuListImages.add(R.drawable.ic_dopplerim);
			}
		}
		
		if(menuListImages.size()<5)
		{
		menuListLabel.add("Contact us");
		menuListImages.add(R.drawable.ic_contactus);
		}
		if(ListFragment_ListMenu.menuListImages.size()<5)
		{
		ListFragment_ListMenu.menuListLabel.add("Contact us");
		ListFragment_ListMenu.menuListImages.add(R.drawable.ic_contactus);
		}
		
		if(menuListImages.size()<5)
		{
		menuListLabel.add("Logout");
		menuListImages.add(R.drawable.ic_logout);
		}
		if(ListFragment_ListMenu.menuListImages.size()<5)
		{
		ListFragment_ListMenu.menuListLabel.add("Logout");
		ListFragment_ListMenu.menuListImages.add(R.drawable.ic_logout);
		}
		//Create adaptor and set row title and icon
 		rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < menuListLabel.size(); i++) {
             RowItem item = new RowItem(menuListImages.get(i), menuListLabel.get(i));
             rowItems.add(item);
        }
         
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new BaseAdapter_ListMenu(Activity_Home.this,rowItems));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			      @Override
			      public void onItemClick(AdapterView<?> parent, final View view,
			          int position, long id) {
			    	  if(menuListLabel.get(position).equalsIgnoreCase("Tickets"))
			    	  {
			    		  ListFragment_ListMenu.menuListImages = images_ticketsselected;
			    		  Intent intent = new Intent(Activity_Home.this, Activity_SliderMenu.class);
			    		  intent.putExtra("selectedlistitem", "Tickets");
			    		  startActivity(intent);
			    		  Activity_Home.this.finish();
			    	  }
			    	  else if(menuListLabel.get(position).equalsIgnoreCase("Modify Service"))
			    	  {
			    		  ListFragment_ListMenu.menuListImages = images_modifyserviceselected;
			    		  Intent intent = new Intent(Activity_Home.this, Activity_SliderMenu.class);
			    		  intent.putExtra("selectedlistitem", "Modify Service");
			    		  startActivity(intent);
			    		  Activity_Home.this.finish();
			    	  }
			    	  else if(menuListLabel.get(position).equalsIgnoreCase("Doppler IM"))
			    	  {
			    		  ListFragment_ListMenu.menuListImages = images_dopplerimselected;
			    		  Intent intent = new Intent(Activity_Home.this, Activity_SliderMenu.class);
			    		  intent.putExtra("selectedlistitem", "Doppler IM");
			    		  startActivity(intent);
			    		  Activity_Home.this.finish();
			    	  }
			    	  else if(menuListLabel.get(position).equalsIgnoreCase("Contact us"))
			    	  {
			    		  ListFragment_ListMenu.menuListImages = images_contactusselected;
			    		  Intent intent = new Intent(Activity_Home.this, Activity_SliderMenu.class);
			    		  intent.putExtra("selectedlistitem", "Contact us");
			    		  startActivity(intent);
			    		  Activity_Home.this.finish();
			    	  }
			    	  else if(menuListLabel.get(position).equalsIgnoreCase("Logout"))
			    	  {
			    		  ListFragment_ListMenu.menuListImages = images_logoutselected;
			  		    Webservice_Logout instance = new Webservice_Logout(Activity_Home.this);
					    instance.postData();
			    	  }			    	  
			      }

			    });
	}//onCreate()
	
	class BaseAdapter_ListMenu extends BaseAdapter {
		Context context;
	    List<RowItem> rowItems;
	 
	    public BaseAdapter_ListMenu(Context context, List<RowItem> items) {
	        this.context = context;
	        this.rowItems = items;
	    }
	 
	    /*private view holder class*/
	    private class ViewHolder {
	        ImageView imageView;
	        TextView txtTitle;
	        TextView txtDesc;
	    }
	 
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	 
	        LayoutInflater mInflater = (LayoutInflater)
	            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.row, null);
	            holder = new ViewHolder();
	            holder.txtTitle = (TextView) convertView.findViewById(R.id.row_title);
	            holder.imageView = (ImageView) convertView.findViewById(R.id.row_icon);
	            convertView.setTag(holder);
	        }
	        else {
	            holder = (ViewHolder) convertView.getTag();
	        }
	 
	        RowItem rowItem = (RowItem) getItem(position);

	        holder.txtTitle.setText(rowItem.getTitle());
	        holder.imageView.setImageResource(rowItem.getImageId());
	        
	        //set background
	        convertView.setBackgroundResource(R.drawable.menulistbackground);
	        return convertView;
	    }
	 
	    @Override
	    public int getCount() {
	        return rowItems.size();
	    }
	 
	    @Override
	    public Object getItem(int position) {
	        return rowItems.get(position);
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return rowItems.indexOf(getItem(position));
	    }
		
	}
	
	@Override
	public void onBackPressed() {
	
	}
}
