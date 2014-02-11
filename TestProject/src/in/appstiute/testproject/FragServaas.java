package in.appstiute.testproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragServaas extends Fragment {
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	Bundle savedInstanceState) {
	 
	View myFragmentView = inflater.inflate(R.layout.frag_servaas, container, false);
	 
	ImageView maps = (ImageView) myFragmentView.findViewById(R.id.ivcityhallmaps);
	maps.setOnClickListener(new View.OnClickListener() {
	 
	@Override
	public void onClick(View v) {
	// TODO Auto-generated method stub
	Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
	Uri.parse("http://maps.google.com/?q=<50.851461>,<5.691043>"));
	startActivity(intent);
	}
	}) ;
	 
	return myFragmentView;
	 
	}
	 
	}
