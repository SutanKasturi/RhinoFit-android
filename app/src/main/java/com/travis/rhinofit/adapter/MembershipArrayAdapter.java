package com.travis.rhinofit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.models.Membership;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class MembershipArrayAdapter extends ArrayAdapter<Membership> {

    Context context;
    int resourceId;

    public MembershipArrayAdapter(Context context, int resource, ArrayList<Membership> objects) {
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
            holder.nameTextView = (TextView) v.findViewById(R.id.nameTextView);
            holder.startAndEndTextView = (TextView) v.findViewById(R.id.startAndEndTextView);
            holder.renewsTextView = (TextView) v.findViewById(R.id.renewsTextView);
            holder.attendedTextView = (TextView) v.findViewById(R.id.attendedTextView);
            holder.limitTextView = (TextView) v.findViewById(R.id.limitTextview);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // Set content
        Membership item = getItem(position);
        holder.nameTextView.setText(item.getMembershipName());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        holder.startAndEndTextView.setText("Starts " + sdf.format(item.getStartDate()) + "\nand ends " + sdf.format(item.getEndDate()));
        holder.renewsTextView.setText(item.getRenewal());
        if ( item.getAttendedLimit() > 0 )
            holder.attendedTextView.setText("You have attended " + item.getAttended() + " out of " + item.getAttendedLimit() + " classes");
        else
            holder.attendedTextView.setText(item.getLimit() + " classes");

        if (item.getLimit().length() == 0)
        {
            holder.limitTextView.setVisibility(View.GONE);
        } else {
            holder.limitTextView.setVisibility(View.VISIBLE);
            holder.limitTextView.setText(item.getLimit());
        }

        return v;
    }

    class ViewHolder {
        TextView nameTextView;
        TextView startAndEndTextView;
        TextView renewsTextView;
        TextView attendedTextView;
        TextView limitTextView;
    }
}
