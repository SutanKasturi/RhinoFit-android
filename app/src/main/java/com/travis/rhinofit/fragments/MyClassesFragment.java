package com.travis.rhinofit.fragments;

import android.app.DatePickerDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.ClassArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.CustomButton;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.RhinofitClass;
import com.travis.rhinofit.rowlayout.ClassRow;
import com.travis.rhinofit.utils.DeviceSize;
import com.travis.rhinofit.utils.ResourceSize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class MyClassesFragment extends BaseFragment implements ClassRow.ClassListener {

    TextView            dateTextView;
    ImageButton         calendarButton;
    CustomButton        prevButton;
    CustomButton        todayButton;
    CustomButton        nextButton;

    WaitingLayout       waitingLayout;
    ListView            listView;

    ArrayList<RhinofitClass> classes;
    ClassArrayAdapter   arrayAdapter;
    DatePickerDialog    datePickerDialog;

    static Date             currentDate = null;

    static SimpleDateFormat dateFormat = null;//new SimpleDateFormat("LLL d, yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myclasses_frame, container, false);
        dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        calendarButton = (ImageButton) view.findViewById(R.id.calendarImageButton);
        prevButton = (CustomButton) view.findViewById(R.id.prevButton);
        todayButton = (CustomButton) view.findViewById(R.id.todayButton);
        nextButton = (CustomButton) view.findViewById(R.id.nextButton);
        waitingLayout = (WaitingLayout) view.findViewById(R.id.waitingLayout);

        listView = (ListView) view.findViewById(R.id.listView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setNavTitle();
    }

    @Override
    public void setNavTitle() {
        parentActivity.setNavTitle("Today's Classes");
    }

    @Override
    public void init() {
        if ( dateFormat == null )
            setDateFormat();

        if ( currentDate == null )
            currentDate = new Date();

        setDatePicker();

        classes = new ArrayList<RhinofitClass>();
        arrayAdapter = new ClassArrayAdapter(parentActivity, R.layout.row_class, classes, currentDate, this);
        listView.setAdapter(arrayAdapter);

        setDateTextView();
        setActions();
    }

    private void setActions() {
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPrev();
            }
        });

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToday();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext();
            }
        });
    }

    private void setDateTextView() {
        dateTextView.setText(getStringFromDate(currentDate));
        getClasses(currentDate);
    }

    private void setDatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(currentDate);
        datePickerDialog = new DatePickerDialog(parentActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                currentDate = newDate.getTime();
                setDateTextView();
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void onPrev() {
        currentDate = new Date(currentDate.getTime() - 24 * 60 * 60 * 1000);
        setDateTextView();
    }

    private void onToday() {
        currentDate = new Date();
        setDateTextView();
    }

    private void onNext() {
        currentDate = new Date(currentDate.getTime() + 24 * 60 * 60 * 1000);
        setDateTextView();
    }

    private void setDateFormat() {
        float otherWidth = ResourceSize.convertDpToPixel(parentActivity, 180);
        float textViewWidth = DeviceSize.getDeviceSize(parentActivity).x - otherWidth;

        Date date = new Date();
        if ( textViewWidth > getDateStringWidth(date, "EEEE, LLLL d, yyyy") ) {
            dateFormat = new SimpleDateFormat("EEEE, LLLL d, yyyy");
        }
        else if ( textViewWidth > getDateStringWidth(date, "LLLL d, yyyy") ) {
            dateFormat = new SimpleDateFormat("LLLL d, yyyy");
        }
        else {
            dateFormat = new SimpleDateFormat("LLL d, yyyy");
        }
    }

    private float getDateStringWidth(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String dateString = sdf.format(date);
        Paint mPaint = new Paint();
        mPaint.setTextSize(ResourceSize.convertDpToPixel(parentActivity, 17));
        float width = mPaint.measureText(dateString, 0, dateString.length());
        return width;
    }

    private String getStringFromDate(Date date) {
        String dateString = dateFormat.format(date);
        return dateString;
    }

    private void getClasses(Date date) {
        if ( task != null ) {
            task.onCancel();
            task = null;
        }

        arrayAdapter.clear();

        waitingLayout.showLoadingProgressBar();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        task = WebService.getClasses(parentActivity, dateString, dateString, new InterfaceHttpRequest.HttpRequestArrayListener() {
            @Override
            public void complete(JSONArray result, String errorMsg) {
                task = null;
                classes.clear();

                if ( result == null ) {
                    if ( errorMsg != null )
                        waitingLayout.showResult(errorMsg);
                    else
                        waitingLayout.showResult("Error!");
                }
                else {
                    if ( result.length() == 0 ) {
                        waitingLayout.showResult(Constants.kMessageNoClasses);
                    }
                    else {
                        waitingLayout.setVisibility(View.GONE);
                        for ( int i = 0; i < result.length(); i++ ) {
                            try {
                                JSONObject jsonObject = result.getJSONObject(i);
                                RhinofitClass rfclass = new RhinofitClass(jsonObject);
                                if ( rfclass != null )
                                    classes.add(rfclass);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if ( classes.size() > 1 )
                            Collections.sort(classes);
                    }
                }

                arrayAdapter.notifyDataSetChanged();
            }
        });
        task.onRun();
    }

    @Override
    public void refreshRow() {

    }

    @Override
    public void onTrackWod(RhinofitClass rfClass) {
        parentActivity.changeFragment(TrackWODFragment.newInstance(rfClass));
    }
}
