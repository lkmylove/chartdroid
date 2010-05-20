package com.googlecode.chartdroid.calendar;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeekdayLabelsAdapter extends BaseAdapter {

	final static public String TAG = "Calendar";
	
    private LayoutInflater mInflater;
	public GregorianCalendar cal;
	public int active_month;
    private List<String> weekay_label_list = new ArrayList<String>();
	
    Context context;
    public WeekdayLabelsAdapter(LayoutInflater inflator, Context context) {
    	this.context = context;
    	mInflater = inflator;
    	
    	
    	String[] wdlist = new DateFormatSymbols().getShortWeekdays();
    	
    	Log.d(TAG, "Number of weekdays: " + wdlist.length);
    	
    	
    	int i=0;
    	for (String wd : wdlist) {
    		
    		if (wd != null && wd.length() > 0) {
	    		Log.i(TAG, wd);
	    		
	    		weekay_label_list.add(wd);
    		} else {
	    		Log.e(TAG, "Index " + i + " is null or blank");
    			
    		}
    		
    		
    		i++;
    	}
    	
    	
    	
    	Log.d(TAG, "Length of weekday list: " + weekay_label_list.size());
    	
    	
    	
    }


    
    
    public View getView(int position, View convertView, ViewGroup parent) {

    	TextView tv = new TextView(context);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
    	tv.setText( weekay_label_list.get(position) );

        return tv;
    }

	@Override
	public boolean areAllItemsEnabled() {

		return false;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}
	
    public final int getCount() {
        return weekay_label_list.size();
    }

    public final Object getItem(int position) {
        return weekay_label_list.get(position);
    }

    public final long getItemId(int position) {
        return position;
    }
}