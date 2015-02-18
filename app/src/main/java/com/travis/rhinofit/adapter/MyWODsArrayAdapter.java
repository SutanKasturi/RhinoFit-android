package com.travis.rhinofit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.travis.rhinofit.models.WodInfo;
import com.travis.rhinofit.rowlayout.MyWODRow;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class MyWODsArrayAdapter extends ArrayAdapter<WodInfo> {

    Context context;
    int resourceId;
    MyWODRow.MyWODListener listener;

    public MyWODsArrayAdapter(Context context, int resource, ArrayList<WodInfo> objects, MyWODRow.MyWODListener callback) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        this.listener = callback;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return new MyWODRow(context, getItem(position), listener);
    }
}
