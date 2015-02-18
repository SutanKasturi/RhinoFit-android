package com.travis.rhinofit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.travis.rhinofit.models.RhinofitClass;
import com.travis.rhinofit.rowlayout.ClassRow;
import com.travis.rhinofit.rowlayout.ClassRow.ClassListener;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/10/15.
 */
public class ClassArrayAdapter extends ArrayAdapter<RhinofitClass> {

    Context context;
    int resourceId;
    Date selectedDate;
    ClassListener listener;

    public ClassArrayAdapter(Context context, int resource, ArrayList<RhinofitClass> objects, Date selectedDate, ClassListener callback) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        this.selectedDate = selectedDate;
        this.listener = callback;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return new ClassRow(context, getItem(position), listener, selectedDate);
    }
}
