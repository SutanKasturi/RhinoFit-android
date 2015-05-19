package com.travis.rhinofit.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/11/15.
 */
//public class DateUtils {
//    public static java.util.Date parseRFC3339Date(String datestring)
//            throws java.text.ParseException, IndexOutOfBoundsException {
//        if ( datestring == null )
//            return null;
//
//        Date d = new Date();
//
//        // if there is no time zone, we don't need to do any special parsing.
//        if (datestring.endsWith("Z")) {
//            try {
//                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // spec for RFC3339
//                d = s.parse(datestring);
//            } catch (java.text.ParseException pe) {// try again with optional decimals
//                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");// spec for RFC3339 (with fractional seconds)
//                s.setLenient(true);
//                d = s.parse(datestring);
//            }
//            return d;
//        }
//
//        // step one, split off the timezone.
//        String firstpart = datestring.substring(0, datestring.lastIndexOf('-'));
//        String secondpart = datestring.substring(datestring.lastIndexOf('-'));
//
//        if ( secondpart.indexOf(":") > 0 ) {
//            // step two, remove the colon from the timezone offset
//            secondpart = secondpart.substring(0, secondpart.indexOf(':'))
//                    + secondpart.substring(secondpart.indexOf(':') + 1);
//            datestring = firstpart + secondpart;
//        }
//        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");// spec for RFC3339
//
//        try {
//            d = s.parse(datestring);
//        } catch (java.text.ParseException pe) {// try again with optional decimals
//            try {
//                s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");// spec for RFC3339 (with fractional seconds)
//                s.setLenient(true);
//                d = s.parse(datestring);
//            }
//            catch (ParseException e) {
//                e.printStackTrace();
//                d = null;
//            }
//        }
//
//        return d;
//    }
//
//    public static Date parseDate(String datestring) {
//        Date d = null;
//        if ( datestring.length() == 10 ) {
//            try {
//                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
//                s.setLenient(true);
//                d = s.parse(datestring);
//                return d;
//            }
//            catch (ParseException e) {
//                return null;
//            }
//        }
//        else if ( datestring.length() == 16 ) {
//            try {
//                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                s.setLenient(true);
//                d = s.parse(datestring);
//                return d;
//            } catch (ParseException e) {
//                return null;
//            }
//        }
//        else if ( datestring.length() == 19 ) {
//            try {
//                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                d = s.parse(datestring);
//                return d;
//            } catch (java.text.ParseException pe) {
//                pe.printStackTrace();
//                return null;
//            }
//        }
//        else {
//            try {
//                SimpleDateFormat s = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
//                s.setLenient(true);
//                d = s.parse(datestring);
//            } catch (ParseException e) {
//                try {
//                    d = parseRFC3339Date(datestring);
//                } catch (ParseException e1) {
//                    e1.printStackTrace();
//                    d = null;
//                }
//            }
//        }
//
//        return d;
//    }
//}

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    private static final String TAG = "DateUtils";

    private static final SimpleDateFormat[] dateFormats;
    private static final int defaultFormat = 2;

    private DateUtils() { }

    static {
        final String[] possibleFormats = {
                "EEE, dd MMM yyyy HH:mm:ss z", // RFC_822
                "EEE, dd MMM yyyy HH:mm zzzz",
                "yyyy-MM-dd'T'HH:mm:ssZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSSzzzz", // Blogger Atom feed has millisecs also
                "yyyy-MM-dd'T'HH:mm:sszzzz",
                "yyyy-MM-dd'T'HH:mm:ss z",
                "yyyy-MM-dd'T'HH:mm:ssz", // ISO_8601
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd'T'HHmmss.SSSz",
                "yyyy-MM-dd"
        };

        dateFormats = new SimpleDateFormat[possibleFormats.length];
        TimeZone gmtTZ = TimeZone.getDefault();

        for (int i = 0; i < possibleFormats.length; i++)
        {
        /* TODO: Support other locales? */
            dateFormats[i] = new SimpleDateFormat(possibleFormats[i],
                    Locale.getDefault());

            dateFormats[i].setTimeZone(gmtTZ);
        }
    }

    public static Date parseDate(String str) {
        Date result = null;
        str = str.trim();

        if (str.length() > 10) {
            // TODO deal with +4:00 (no zero before hour)
            if ((str.substring(str.length() - 5).indexOf("+") == 0 || str
                    .substring(str.length() - 5).indexOf("-") == 0)
                    && str.substring(str.length() - 5).indexOf(":") == 2) {

                String sign = str.substring(str.length() - 5,
                        str.length() - 4);

                str = str.substring(0, str.length() - 5) + sign + "0"
                        + str.substring(str.length() - 4);
                // logger.debug("CASE1 : new date " + strdate + " ? "
                //    + strdate.substring(0, strdate.length() - 5));

            }

            String dateEnd = str.substring(str.length() - 6);

            // try to deal with -05:00 or +02:00 at end of date
            // replace with -0500 or +0200
            if ((dateEnd.indexOf("-") == 0 || dateEnd.indexOf("+") == 0)
                    && dateEnd.indexOf(":") == 3) {
                // TODO deal with GMT-00:03
                if ("GMT".equals(str.substring(str.length() - 9, str
                        .length() - 6))) {
                    Log.d(TAG, "General time zone with offset, no change");
                } else {
                    // continue treatment
                    String oldDate = str;
                    String newEnd = dateEnd.substring(0, 3) + dateEnd.substring(4);
                    str = oldDate.substring(0, oldDate.length() - 6) + newEnd;
                    // logger.debug("!!modifying string ->"+strdate);
                }
            }
        }

        int i = 0;

        while (i < dateFormats.length) {
            try {
                result = dateFormats[i].parse(str);
                // logger.debug("******Parsing Success "+strdate+"->"+result+" with
                // "+dateFormats[i].toPattern());
                break;
            } catch (java.text.ParseException eA) {
                i++;
            }
        }

        return result;
    }

    /**
     * Format a date in a manner that would be most suitable for serialized
     * storage.
     *
     * @param date
     *   {@link Date} object to format.
     *
     * @return
     *   Robust, formatted date string.
     */
    public static String formatDate(Date date) {
        return dateFormats[defaultFormat].format(date);
    }
}
