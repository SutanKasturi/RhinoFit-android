package com.travis.rhinofit.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.models.MenuItem;


public class MenuArrayAdapter extends ArrayAdapter<MenuItem> {
	
	Context context;
	int resourceId;
	
	public MenuArrayAdapter(Context context, int resource,
			ArrayList<MenuItem> objects) {
		super(context, resource, objects);
		this.context = context;
		this.resourceId = resource;
	}

	@SuppressLint("ViewHolder")
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		ViewHolder holder = null;
	    if(v == null){
	    	LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(resourceId, null);
			
	        holder = new ViewHolder();
	        holder.menuTitle = (TextView) v.findViewById(R.id.menuItemTextView);
	        v.setTag(holder);
	    } else {
	        holder = (ViewHolder) v.getTag();
	    }
		
		// Set content
		MenuItem item = getItem(position);
		holder.menuTitle.setText(item.getTitle());
		
		return v;
	}
	
	class ViewHolder {
	   TextView menuTitle;
	}
}
