package com.travis.rhinofit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.travis.rhinofit.models.BenchmarkHistory;
import com.travis.rhinofit.rowlayout.BenchmarkHistoryRow;
import com.travis.rhinofit.rowlayout.BenchmarkRow;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class BenchmarkHistoryArrayAdapter extends ArrayAdapter<BenchmarkHistory> {

    Context context;
    int resourceId;
    BenchmarkHistoryRow.BenchmarkHistoryListener listener;

    public BenchmarkHistoryArrayAdapter(Context context, int resource, ArrayList<BenchmarkHistory> objects, BenchmarkHistoryRow.BenchmarkHistoryListener callback) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        this.listener = callback;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return new BenchmarkHistoryRow(context, getItem(position), listener);
    }
}
