package com.example.testproject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	final int Today=0, CurrentWeek=1, LastWeek=2, CurrentMonth=3; 
	
	//Set Of dates
	Date todaysDate;
	ArrayList<Date> currentWeekDatesList;
	ArrayList<Date> lastWeekDatesList;
	ArrayList<Date> currentMonthList;
	
	//Set of open, closed and maintenance tickets
	ArrayList<Ticket> open_ticketList;
	ArrayList<Ticket> closed_ticketList;
	ArrayList<Ticket> maint_ticketList;

	//Set of open tickets
	ArrayList<Ticket> open_todaysTicketList;
	ArrayList<Ticket> open_currentWeekTicketList;
	ArrayList<Ticket> open_lastWeekTicketList;
	ArrayList<Ticket> open_currentMonthTicketList;
	
	//Set of closed tickets
	ArrayList<Ticket> closed_todaysTicketList;
	ArrayList<Ticket> closed_currentWeekTicketList;
	ArrayList<Ticket> closed_lastWeekTicketList;
	ArrayList<Ticket> closed_currentMonthTicketList;
	
	//Set of maintenance tickets
	ArrayList<Ticket> maint_todaysTicketList;
	ArrayList<Ticket> maint_currentWeekTicketList;
	ArrayList<Ticket> maint_lastWeekTicketList;
	ArrayList<Ticket> maint_currentMonthTicketList;

	@SuppressLint("SimpleDateFormat")
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
//		Log.d("TAG", "bufferString=" + bufferString);

		try {

//			Log.d("TAG", "1>>>>>>>>>>>>>>>");
			// convert string to JSONArray
			JSONArray jsonArray = new JSONArray(bufferString);
			// parse an Object from a random index in the JSONArray

			// you can continue to set the different attributes of the Book
			// object using key/value pairs from the Book class (e.g.
			// setPageNum, setChapter, etc).

//			Log.d("TAG", "2>>>>>>>>>>>>>>>");
			// Getting JSON Array node
			// looping through All Contacts
			Log.d("TAG", "jsonArray.length()=" + jsonArray.length());
			open_ticketList = new ArrayList<Ticket>();
			closed_ticketList = new ArrayList<Ticket>();
			maint_ticketList = new ArrayList<Ticket>();
			
			for (int i = 0; i < jsonArray.length(); i++) {
				Ticket ticket = new Ticket();
//				Log.d("TAG", i + ">>>>>>>>>>>>>>>");
				JSONObject c = jsonArray.getJSONObject(i);
//				Log.d("tag", "" + c.get("ticketId"));
//				Log.d("tag", "" + c.get("subject"));
//				Log.d("tag", "" + c.get("status"));
//				Log.d("tag", "" + c.get("createDate"));
//				Log.d("tag", "" + c.get("closeDate"));

				// Phone node is JSON Object
				if (!(c.get("ticketId").equals(JSONObject.NULL)))
					ticket.ticketId = c.getInt("ticketId");
				else
					ticket.ticketId = -1;

				if (!(c.get("subject").equals(JSONObject.NULL)))
					ticket.subject = c.getString("subject");
				else
					ticket.subject = "";

				if (!(c.get("status").equals(JSONObject.NULL)))
				{
					ticket.status = c.getString("status");
					
					if(ticket.status.equalsIgnoreCase("Open"))
					{
						open_ticketList.add(ticket);
					}
					else if(ticket.status.equalsIgnoreCase("Closed"))
					{
						closed_ticketList.add(ticket);
					}
					else if (ticket.status.equalsIgnoreCase("Maint") || ticket.status.equalsIgnoreCase("Maint.") ||ticket.status.equalsIgnoreCase("Maintenance"))
					{
						maint_ticketList.add(ticket);
					}
				}
				else
					ticket.status = "";

				if (!(c.get("createDate").equals(JSONObject.NULL)))
					ticket.createDate = c.getInt("createDate");
				else
					ticket.createDate = -1;

				if (!(c.get("closeDate").equals(JSONObject.NULL)))
					ticket.closeDate = c.getInt("closeDate");
				else
					ticket.closeDate = -1;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		
//		// Parsed
//		Log.d("TAG", "Size=" + ticketList.size());
//		java.util.Date todaysDate = new java.util.Date(
//				(long) ticketList.get(0).createDate * 1000);
//		Calendar c = Calendar.getInstance();
//
//		System.out.println("todaysDate time => " + todaysDate);
//		System.out.println("Current time => " + c.getTime());
//		c.setTime(todaysDate);
//
//		int weekNo = c.get(Calendar.WEEK_OF_YEAR);
//		Log.d("tag", "weekNo=" + weekNo);
//		c.set(Calendar.WEEK_OF_YEAR, weekNo);
//		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//
//		// Current week days
//		SimpleDateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy");
//		for (int i = 0; i < 7; i++) {
//			Log.d("Current Week", "Date=" + formatter.format(c.getTime()));
//			c.add(Calendar.DATE, 1);
//		}
//
//		// Previous week days
//		for (int i = 0; i < 14; i++) {
//			c.add(Calendar.DATE, -1);
//		}
//		for (int i = 0; i < 7; i++) {
//			Log.d("Last Week", "Date=" + formatter.format(c.getTime()));
//			c.add(Calendar.DATE, 1);
//		}
//
//		
//		// Find the month
//		Log.d("Month", "Finding dates of curent month");
//		c.setTime(todaysDate);
//		c.set(Calendar.DATE, 1);
//
//		for (int i = 0; i < 100; i++) {
//			Log.d("Month", "Date=" + formatter.format(c.getTime()));
//			c.add(Calendar.DATE, 1);
//
//			if (c.get(Calendar.DATE) == 1) // break if we reach next month
//				break;
//		}
		
	    //Create dates list and fill them up
		currentWeekDatesList=new ArrayList<Date>();
		lastWeekDatesList=new ArrayList<Date>();
		currentMonthList=new ArrayList<Date>();
		fillUpDatesArray();
		
		//Create dates list and fill them up
		open_todaysTicketList = new ArrayList<Ticket>();
		open_currentWeekTicketList = new ArrayList<Ticket>();
		open_lastWeekTicketList = new ArrayList<Ticket>();
		open_currentMonthTicketList = new ArrayList<Ticket>();
		Log.d("tag", "Before calling filterOpenTickets=");
		filterOpenTickets(open_ticketList);
		Log.d("tag", "Before calling filterOpenTickets=");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Log.d("After Filter", "OPEN-TODAY="+open_todaysTicketList.size()+"OPEN-CURRENT WEEK="+open_currentWeekTicketList.size()+"OPEN-LAST WEEK="+open_lastWeekTicketList.size()+"OPEN-CURRENT MONTH="+open_currentMonthTicketList.size());
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		
//		ArrayList<Ticket> closed_todaysTicketList = new ArrayList<Ticket>();
//		ArrayList<Ticket> closed_currentWeekTicketList = new ArrayList<Ticket>();
//		ArrayList<Ticket> closed_lastWeekTicketList = new ArrayList<Ticket>();
//		ArrayList<Ticket> closed_currentMonthTicketList = new ArrayList<Ticket>();
//		
//		ArrayList<Ticket> maint_todaysTicketList = new ArrayList<Ticket>();
//		ArrayList<Ticket> maint_currentWeekTicketList = new ArrayList<Ticket>();
//		ArrayList<Ticket> maint_lastWeekTicketList = new ArrayList<Ticket>();
//		ArrayList<Ticket> maint_currentMonthTicketList = new ArrayList<Ticket>();
		
	}// onCreate
	
	private void fillUpDatesArray() {
		Calendar c = Calendar.getInstance();
		
		todaysDate = c.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy");
//		Date date;
//		try {
//			
//			date = formatter.parse(""+todaysDate );
//		    long unixTime = (long) date.getTime()/1000;
//		    Log.d("TAG-TAG-TAG","TODAYS in unix time="+unixTime );
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.d("tag", "ERORORORORORORORORRORORORORORORORORORO");
//		}

		
		int weekNo = c.get(Calendar.WEEK_OF_YEAR);
		c.set(Calendar.WEEK_OF_YEAR, weekNo);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
		// Current week days
		formatter = new SimpleDateFormat("EEE dd/MM/yyyy");
		for (int i = 0; i < 7; i++) {
			Log.d("Current Week", "Date=" + formatter.format(c.getTime()));
			currentWeekDatesList.add(c.getTime());
			c.add(Calendar.DATE, 1);
		}
		Log.d("TAG", "currentWeekDatesList Size="+currentWeekDatesList.size());
		
		// last week days
		for (int i = 0; i < 14; i++) {
			c.add(Calendar.DATE, -1);
		}
		for (int i = 0; i < 7; i++) {
			Log.d("Last Week", "Date=" + formatter.format(c.getTime()));
			lastWeekDatesList.add(c.getTime());
			c.add(Calendar.DATE, 1);
		}
		Log.d("TAG", "lastWeekDatesList Size="+lastWeekDatesList.size());
		
		// current month
		Log.d("Month", "Finding dates of curent month");
		c.setTime(todaysDate);
		c.set(Calendar.DATE, 1);

		for (int i = 0; i < 100; i++) {
			Log.d("Month", "Date=" + formatter.format(c.getTime()));
			currentMonthList.add(c.getTime());
			c.add(Calendar.DATE, 1);

			if (c.get(Calendar.DATE) == 1) // break if we reach next month
				break;
		}	
		Log.d("TAG", "currentMonthList Size="+currentMonthList.size());
		
	}//fillUpDatesArray()

	void filterOpenTickets(ArrayList<Ticket> open_ticketList)
	{
		Log.d("tag", "Iniside filterOpenTickets");
		Log.d("tag", "open_ticketList size="+open_ticketList.size());
		//filtering out all tickets present in open_ticketList by two steps: 1. Picking up top object and comparing 2. Deleting that ticket from open_ticketList
//		int i=0;
		for (;open_ticketList.size()!=0;)
		{
//			if(i == 10)
//				break;
//			Log.d("tag", i+"open_ticketList"+open_ticketList.size());
//			i++;
			Date createdDate = new Date((long)open_ticketList.get(0).createDate*1000);
			switch(filter(createdDate))
			{
			case Today:
				open_todaysTicketList.add(open_ticketList.get(0));
				open_ticketList.remove(0);
				break;
				
			case CurrentWeek:
				open_currentWeekTicketList.add(open_ticketList.get(0));
				open_ticketList.remove(0);
				break;
				
			case LastWeek:
				open_lastWeekTicketList.add(open_ticketList.get(0));
				open_ticketList.remove(0);
				break;
				
			case CurrentMonth:
				open_currentMonthTicketList.add(open_ticketList.get(0));
				open_ticketList.remove(0);
				break;
				
			case -1:
				open_ticketList.remove(0);
				break;
				
			}
		}//for
		
		Log.d("TAG", "open_todaysTicketList Size="+open_todaysTicketList.size());
		Log.d("TAG", "open_currentWeekTicketList Size="+open_currentWeekTicketList.size());
		Log.d("TAG", "open_lastWeekTicketList Size="+open_lastWeekTicketList.size());
		Log.d("TAG", "open_currentMonthTicketList Size="+open_currentMonthTicketList.size());
		
	}//filterOpenTickets

	private int filter(Date createdDate) {
		Log.d("tag", "Inside filter()");
		Log.d("tag", "createdDate="+createdDate);
		
		DateTime datetime_created = new DateTime(createdDate.getTime());
		DateTime datetime_today = DateTime.now();
		
		
		if(DateTimeComparator.getDateOnlyInstance().compare(datetime_created, datetime_today)==0)
		{
			Log.d("tag", "createdDate.compareTo(todaysDate)==0");
			return Today;
		}
		else if(DateTimeComparator.getDateOnlyInstance().compare(datetime_created, datetime_today)<0)
		{
			for(int i=0; i<lastWeekDatesList.size(); i++)
			{
				
				if(DateTimeComparator.getDateOnlyInstance().compare(datetime_created, new DateTime(lastWeekDatesList.get(i).getTime()))==0)
					return LastWeek;
			}
		}
		else if(DateTimeComparator.getDateOnlyInstance().compare(datetime_created, datetime_today)>0)
		{
			for(int i=0; i<currentWeekDatesList.size(); i++)
			{
				
				if(DateTimeComparator.getDateOnlyInstance().compare(datetime_created, new DateTime(currentWeekDatesList.get(i).getTime()))==0)
					return CurrentWeek;
			}
		}
		else
		{
			for(int i=0; i<currentMonthList.size(); i++)
			{				
				if(DateTimeComparator.getDateOnlyInstance().compare(datetime_created, new DateTime(currentMonthList.get(i).getTime()))==0)
					return CurrentMonth;
			}
		}
		return -1;
	}
}
