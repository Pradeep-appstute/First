package com.masergy.iscticket.ContentView;

import java.util.ArrayList;

import com.masergy.iscticket.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Fragment_Tickets extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		
		
		
		// construct the RelativeLayout
		LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_tickets, container, false);
		
		//v.setBackgroundColor(Color.RED);

		
		return v;
	}

	
}
