package com.travis.rhinofit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by fedoro on 7/13/16.
 */
public class UtilsMethod {

    public static boolean getBooleanFromSharedPreferences(Context context, String key, boolean defaultValue) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        boolean value = sharedPreferences.getBoolean(key, defaultValue);

        return value;
    }

    public static void saveBooleanInSharedPreferences(Context context, String key, boolean value) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value)
                .commit();
    }
}
