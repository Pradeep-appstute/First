package com.masergy.iscticket.ContentView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Fragment_Tickets extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// construct the RelativeLayout
		LinearLayout v = new LinearLayout(getActivity());
		v.setBackgroundColor(Color.RED);		
		return v;
	}

}
