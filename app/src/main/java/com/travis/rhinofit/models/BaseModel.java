package com.travis.rhinofit.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Sutan Kasturi on 1/26/2015.
 */
public abstract class BaseModel implements Parcelable {

    public BaseModel() {}

    public BaseModel(Parcel in) {

    }

    public abstract void writeToParcel(Parcel out, int flags);

    public int describeContents() {
        return 0;
    }
}
