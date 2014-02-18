package com.example.testproject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
         * */
java.util.Date todaysDate=new java.util.Date((long)ticketList.get(0).createDate*1000);
Calendar c = Calendar.getInstance();

System.out.println("todaysDate time => " + todaysDate);
System.out.println("Current time => " + c.getTime());
c.setTime(todaysDate);

int weekNo = c.get(Calendar.WEEK_OF_YEAR);
Log.d("tag", "weekNo="+weekNo);
c.set(Calendar.WEEK_OF_YEAR, weekNo);
c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

SimpleDateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy"); 
for (int i = 0; i < 7; i++) {
    Log.d("Current Week","Date="+formatter.format(c.getTime()));
    c.add(Calendar.DATE, 1);
}


//Previous week days
for (int i = 0; i < 14; i++) {
    c.add(Calendar.DATE, -1);
}
for (int i = 0; i < 7; i++) {
    Log.d("Last Week","Date="+formatter.format(c.getTime()));
    c.add(Calendar.DATE, 1);
}

Log.d("Month","MonthMonthMonthMonthMonthMonthMonthMonthMonth");
//Find the month
c.setTime(todaysDate);
c.set(Calendar.DATE, 1);

for (int i = 0; i < 100; i++) {
	Log.d("Month","Date="+formatter.format(c.getTime()));
	c.add(Calendar.DATE, 1);
	
	if(c.get(Calendar.DATE)==1) //break if we reach next month
	break;
}


}//onCreate
}
