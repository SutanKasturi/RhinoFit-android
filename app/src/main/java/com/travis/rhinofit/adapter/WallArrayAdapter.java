package com.travis.rhinofit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.travis.rhinofit.models.Wall;
import com.travis.rhinofit.rowlayout.WallRow;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class WallArrayAdapter extends ArrayAdapter<Wall> {

    Context context;
    int resourceId;

    public WallArrayAdapter(Context context, int resource, ArrayList<Wall> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return new WallRow(context, getItem(position));
    }
}
