package com.masergy.iscticket.ContentView;

import com.masergy.iscticket.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Fragment_Tickets extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// construct the RelativeLayout
		LinearLayout v = (LinearLayout) inflater.inflate(
				R.layout.fragment_tickets, container, false);

		// v.setBackgroundColor(Color.RED);

		return v;
	}

}
