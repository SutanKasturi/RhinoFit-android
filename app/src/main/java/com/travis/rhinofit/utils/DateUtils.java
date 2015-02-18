package com.travis.rhinofit.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/11/15.
 */
public class DateUtils {
    public static java.util.Date parseRFC3339Date(String datestring)
            throws java.text.ParseException, IndexOutOfBoundsException {
        if ( datestring == null )
            return null;

        Date d = new Date();

        // if there is no time zone, we don't need to do any special parsing.
        if (datestring.endsWith("Z")) {
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // spec for RFC3339
                d = s.parse(datestring);
            } catch (java.text.ParseException pe) {// try again with optional decimals
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");// spec for RFC3339 (with fractional seconds)
                s.setLenient(true);
                d = s.parse(datestring);
            }
            return d;
        }

        // step one, split off the timezone.
        String firstpart = datestring.substring(0, datestring.lastIndexOf('-'));
        String secondpart = datestring.substring(datestring.lastIndexOf('-'));

        // step two, remove the colon from the timezone offset
        secondpart = secondpart.substring(0, secondpart.indexOf(':'))
                + secondpart.substring(secondpart.indexOf(':') + 1);
        datestring = firstpart + secondpart;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");// spec for RFC3339

        try {
            d = s.parse(datestring);
        } catch (java.text.ParseException pe) {// try again with optional decimals
            try {
                s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");// spec for RFC3339 (with fractional seconds)
                s.setLenient(true);
                d = s.parse(datestring);
            }
            catch (ParseException e) {
                e.printStackTrace();
                d = null;
            }
        }

        return d;
    }

    public static Date parseDate(String datestring) {
        Date d = new Date();
        if ( datestring.length() == 10 ) {
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                s.setLenient(true);
                d = s.parse(datestring);
                return d;
            }
            catch (ParseException e) {

            }
        }
        else if ( datestring.length() == 16 ) {
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                s.setLenient(true);
                d = s.parse(datestring);
                return d;
            } catch (ParseException e) {
            }
        }
        else if ( datestring.length() == 19 ) {
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                d = s.parse(datestring);
                return d;
            } catch (java.text.ParseException pe) {
                pe.printStackTrace();
            }
        }
        else {
            try {
                SimpleDateFormat s = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                s.setLenient(true);
                d = s.parse(datestring);
            } catch (ParseException e) {
                try {
                    d = parseRFC3339Date(datestring);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                    d = null;
                }
            }
        }

        return d;
    }
}
