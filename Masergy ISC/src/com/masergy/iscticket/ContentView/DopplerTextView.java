package com.masergy.iscticket.ContentView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class DopplerTextView extends TextView {
	public DopplerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // TODO: provide an API to set extra bottom pixels (50 in this example)
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + 50);       
    }
}
