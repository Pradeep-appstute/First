package com.masergy.iscticket.ContentView;

import java.util.ArrayList;

//Parent view for Doppler IM 
public class DopplerIM_Parent {
    public String id;
    public String name;
    public String alarmState;
    
    // ArrayList to store child objects
    ArrayList<DopplerIM_Child> childList;
    
    
    public String getId()
    {
        return id;
    }
     
    public void setId(String id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
     
    public void setName(String name)
    {
        this.name = name;
    }
     
    public String getAlarmState()
    {
        return alarmState;
    }
     
    public void setAlarmState(String alarmState)
    {
        this.alarmState = alarmState;
    }
 
    // ArrayList to store child objects
    public ArrayList<DopplerIM_Child> getChildren()
    {
        return childList;
    }
     
    public void setChildren(ArrayList<DopplerIM_Child> childList)
    {
        this.childList = childList;
    }
}
