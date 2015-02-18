package com.travis.rhinofit.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/13/15.
 */
public class MonthPicker {
    public static DatePickerDialog createDialogWithoutDateField(Context context, Date date, DatePickerDialog.OnDateSetListener listener){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(sdf.format(date));
        sdf = new SimpleDateFormat("MM");
        int month = Integer.parseInt(sdf.format(date));

        DatePickerDialog dpd = new DatePickerDialog(context, listener, year, month - 1, 1);
        try{
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }

            }
        }catch(Exception ex){
        }
        return dpd;
    }
}
