package com.travis.rhinofit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.travis.rhinofit.models.MyBenchmark;
import com.travis.rhinofit.models.Reservation;
import com.travis.rhinofit.rowlayout.BenchmarkRow;
import com.travis.rhinofit.rowlayout.ReservationRow;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class BenchmarkArrayAdapter extends ArrayAdapter<MyBenchmark> {

    Context context;
    int resourceId;
    BenchmarkRow.BenchmarkListener listener;

    public BenchmarkArrayAdapter(Context context, int resource, ArrayList<MyBenchmark> objects, BenchmarkRow.BenchmarkListener callback) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        this.listener = callback;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return new BenchmarkRow(context, getItem(position), listener);
    }
}
