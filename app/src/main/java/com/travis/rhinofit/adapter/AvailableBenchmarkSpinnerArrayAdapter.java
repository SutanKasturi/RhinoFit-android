package com.travis.rhinofit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.models.AvailableBenchmark;
import com.travis.rhinofit.models.MenuItem;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/13/15.
 */
public class AvailableBenchmarkSpinnerArrayAdapter extends ArrayAdapter<AvailableBenchmark> {

    Context context;
    int resourceId;

    public AvailableBenchmarkSpinnerArrayAdapter(Context context, int resource,
                            ArrayList<AvailableBenchmark> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        ViewHolder holder = null;
        if(v == null){
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(resourceId, null);

            holder = new ViewHolder();
            holder.availableBenchmarkTextView = (TextView) v.findViewById(R.id.availableBenchmarkTextView);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // Set content
        AvailableBenchmark item = getItem(position);
        holder.availableBenchmarkTextView.setText(item.getbDescription());

        return v;
    }

    class ViewHolder {
        TextView availableBenchmarkTextView;
    }
}
