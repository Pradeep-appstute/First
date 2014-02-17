package com.example.testproject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

ArrayList<Ticket> ticketList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		InputStream is;
		byte[] buffer = null;
		try {
			is = this.getApplicationContext().getAssets().open("tickets.json");
			int size = is.available();
			buffer = new byte[size];
			is.read(buffer);
			is.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String bufferString = new String(buffer);
		Log.d("TAG", "bufferString="+bufferString);
		
        try {

		     
		    Log.d("TAG", "1>>>>>>>>>>>>>>>");
		  //convert string to JSONArray
		    JSONArray jsonArray = new JSONArray(bufferString);
		    //parse an Object from a random index in the JSONArray
		     
		    //you can continue to set the different attributes of the Book object using key/value pairs from the Book class (e.g. setPageNum, setChapter, etc).
		    
		    Log.d("TAG", "2>>>>>>>>>>>>>>>");
		    // Getting JSON Array node
		    // looping through All Contacts
		    Log.d("TAG", "jsonArray.length()="+jsonArray.length());
		    ticketList = new ArrayList<Ticket>();
		    for (int i = 0; i < jsonArray.length(); i++) {
		    	Ticket ticket = new Ticket();
		    	Log.d("TAG", i+">>>>>>>>>>>>>>>");
		    	JSONObject c = jsonArray.getJSONObject(i);
		    	Log.d("tag", ""+c.get("ticketId"));
		    	Log.d("tag", ""+c.get("subject"));
		    	Log.d("tag", ""+c.get("status"));
		    	Log.d("tag", ""+c.get("createDate"));
		    	Log.d("tag", ""+c.get("closeDate"));
		    	
		        // Phone node is JSON Object
		    	if(!(c.get("ticketId").equals(JSONObject.NULL)))
		    		ticket.ticketId=c.getInt("ticketId");
		    	else
		    		ticket.ticketId=-1;
		    	
		    	if(!(c.get("subject").equals(JSONObject.NULL)))
		    		ticket.subject=c.getString("subject");
		    	else
		    		ticket.subject="";
		    	
		    	if( !(c.get("status").equals(JSONObject.NULL)))
		    		ticket.status=c.getString("status");
		    	else
		    		ticket.status="";
		    	
		    	if(!(c.get("createDate").equals(JSONObject.NULL)))
		    		ticket.createDate=c.getInt("createDate");
		    	else
		    		ticket.createDate=-1;
		    	
		    	if( !(c.get("closeDate").equals(JSONObject.NULL)))
		    		ticket.closeDate=c.getInt("closeDate");
		    	else
		    		ticket.closeDate=-1;
		        // adding each child node to HashMap key => value


		        // adding contact to contact list
		        if (ticket!=null)
		        ticketList.add(ticket);
		    }
		} catch (JSONException e) {
		    e.printStackTrace();
		}
        
        Log.d("TAG", "Size="+ticketList.size());
        //Parsed
        
        
        //Preparing filter
        /*
         * http://stackoverflow.com/questions/16388268/getting-current-week-days-with-dates
         * 
         * 
         Calendar c = Calendar.getInstance();
c.set(Calendar.DAY_OF_MONTH, 5);
c.set(Calendar.MONTH, 7);
c.set(Calendar.YEAR, 2013);

int weekNo = c.get(Calendar.WEEK_OF_YEAR);
c.set(Calendar.WEEK_OF_YEAR, weekNo);

c.clear();

c.set(Calendar.WEEK_OF_YEAR, weekNo);
c.set(Calendar.YEAR, 2013);


SimpleDateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy"); 
Date startDate = c.getTime();
c.add(Calendar.DATE, 1);
for (int i = 0; i < 5; i++) {
    Log.d(formatter.format(c.getTime()));
    c.add(Calendar.DATE, 1);
}
         */
        for(int index=0; index<ticketList.size(); index++)
        {
        	java.util.Date time=new java.util.Date((long)ticketList.get(index).createDate*1000);
        	Log.d("tag", "time="+time);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
