package com.travis.rhinofit.models;

import android.util.Log;

import com.travis.rhinofit.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Sutan Kasturi on 2/6/2015.
 */
public class JSONModel {
    private static final String LOG_TAG = "JSONModel";

    public static boolean isNull(JSONObject obj, String keyString) {

        if ( obj == null || keyString == null )
            return false;

        try {
            if ( obj.isNull(keyString) ||
                    obj.getString(keyString).equals("null") ||
                    obj.getString(keyString).equals("<null>") ||
                    obj.getString(keyString).equals("(null)") )
                return true;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "JSONObject : " + obj.toString());
            Log.e(LOG_TAG, "KeyString : " + keyString);
        }
        return false;
    }

    public static String getStringFromJson(JSONObject obj, String keyString) {
        if ( !isNull(obj, keyString) ) {
            try {
                return obj.getString(keyString);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "JSONObject : " + obj.toString());
                Log.e(LOG_TAG, "KeyString : " + keyString);
            }
        }

        return "";
    }

    public static int getIntFromJson(JSONObject obj, String keyString) {
        if ( !isNull(obj, keyString) ) {
            try {
                return obj.getInt(keyString);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "JSONObject : " + obj.toString());
                Log.e(LOG_TAG, "KeyString : " + keyString);
            }
        }

        return -1;
    }

    public static double getDoubleFromJson(JSONObject obj, String keyString) {
        if ( !isNull(obj, keyString) ) {
            try {
                return obj.getDouble(keyString);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "JSONObject : " + obj.toString());
                Log.e(LOG_TAG, "KeyString : " + keyString);
            }
        }

        return Double.MIN_VALUE;
    }

    public static boolean getBooleanFromJson(JSONObject obj, String keyString) {
        if ( !isNull(obj, keyString) ) {
            try {
                return obj.getBoolean(keyString);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "JSONObject : " + obj.toString());
                Log.e(LOG_TAG, "KeyString : " + keyString);
            }
        }

        return false;
    }

    public static Date getDateFromJson(JSONObject obj, String keyString) {
        String date = JSONModel.getStringFromJson(obj, keyString);
        if ( date != null && !date.isEmpty() ) {
            return DateUtils.parseDate(date);
        }

        return null;
    }

    public static JSONObject getJSONObjectFromJson(JSONObject obj, String keyString) {
        if ( !isNull(obj, keyString) ) {
            try {
                return obj.getJSONObject(keyString);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "JSONObject : " + obj.toString());
                Log.e(LOG_TAG, "KeyString : " + keyString);
            }
        }

        return null;
    }

    public static JSONArray getJSONArrayFromJson(JSONObject obj, String keyString) {
        if ( !isNull(obj, keyString) ) {
            try {
                return obj.getJSONArray(keyString);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "JSONObject : " + obj.toString());
                Log.e(LOG_TAG, "KeyString : " + keyString);
            }
        }

        return null;
    }

    public static ArrayList<String> getKeysFromJson(JSONObject jsonObject) {
        if ( jsonObject == null )
            return null;
        Iterator iterator = jsonObject.keys();
        ArrayList<String> keys = new ArrayList<String>();
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            keys.add(key);
        }

        return keys;
    }

    public static ArrayList<String> convertArrayListFromJSONArray(JSONArray jsonArray) {
        if ( jsonArray == null )
            return null;

        ArrayList<String> listdata = new ArrayList<String>();
        if (jsonArray != null) {
            for (int i=0;i<jsonArray.length();i++){
                try {
                    listdata.add(jsonArray.get(i).toString());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return listdata;
    }

    public static JSONArray convertJSONArrayFromArrayList(ArrayList<String> arrayList) {
        if ( arrayList == null )
            return null;
        return new JSONArray(arrayList);
    }
}
