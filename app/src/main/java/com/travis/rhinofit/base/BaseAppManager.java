package com.travis.rhinofit.base;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Sutan Kasturi on 2/5/2015.
 */
public class BaseAppManager {
    private static final String MyPREFERENCES 		= "MyPrefs" ;

    public Context context;
    private static BaseAppManager baseAppManager;

    private static SharedPreferences _sharedInstance = null;

    public SharedPreferences get_sharedInstance() {
        if ( _sharedInstance == null ) {
            if ( context == null )
                return null;

            _sharedInstance = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        }
        return _sharedInstance;
    }

    public void removeValue(String key) {
        if ( get_sharedInstance() == null )
            return;

        SharedPreferences.Editor editor = get_sharedInstance().edit();
        editor.remove(key);
        editor.commit();
    }

    public String getStringValue(String key) {
        if ( get_sharedInstance() == null )
            return null;
        return get_sharedInstance().getString(key, "");
    }

    public void setStringValue(String key, String value) {
        if ( get_sharedInstance() == null )
            return;

        if ( value == null || value.isEmpty() ) {
            removeValue(key);
        }
        else {
            SharedPreferences.Editor editor = get_sharedInstance().edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public int getIntValue(String key) {
        if ( get_sharedInstance() == null )
            return Integer.MIN_VALUE;
        return get_sharedInstance().getInt(key, Integer.MIN_VALUE);
    }

    public void setIntValue(String key, int value) {
        if ( get_sharedInstance() == null )
            return;

        SharedPreferences.Editor editor = get_sharedInstance().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public boolean getBooleanValue(String key) {
        if ( get_sharedInstance() == null )
            return false;
        return get_sharedInstance().getBoolean(key, false);
    }

    public void setBooleanValue(String key, boolean value) {
        if ( get_sharedInstance() == null )
            return;

        SharedPreferences.Editor editor = get_sharedInstance().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
