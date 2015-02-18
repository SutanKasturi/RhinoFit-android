package com.travis.rhinofit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.travis.rhinofit.models.Attendance;
import com.travis.rhinofit.models.Reservation;
import com.travis.rhinofit.rowlayout.AttendanceRow;
import com.travis.rhinofit.rowlayout.ReservationRow;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class AttendanceArrayAdapter extends ArrayAdapter<Attendance> {

    Context context;
    int resourceId;
    AttendanceRow.AttendanceListener listener;

    public AttendanceArrayAdapter(Context context, int resource, ArrayList<Attendance> objects, AttendanceRow.AttendanceListener callback) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        this.listener = callback;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return new AttendanceRow(context, getItem(position), listener);
    }
}
