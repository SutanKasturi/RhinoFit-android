package com.travis.rhinofit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.travis.rhinofit.models.Reservation;
import com.travis.rhinofit.rowlayout.ReservationRow;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class ReservationArrayAdapter extends ArrayAdapter<Reservation> {

    Context context;
    int resourceId;
    ReservationRow.ReservationListener listener;

    public ReservationArrayAdapter(Context context, int resource, ArrayList<Reservation> objects, ReservationRow.ReservationListener callback) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        this.listener = callback;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return new ReservationRow(context, getItem(position), listener);
    }
}
