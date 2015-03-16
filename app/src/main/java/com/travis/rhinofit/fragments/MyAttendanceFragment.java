package com.travis.rhinofit.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.AttendanceArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.Attendance;
import com.travis.rhinofit.rowlayout.AttendanceRow;
import com.travis.rhinofit.utils.MonthPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class MyAttendanceFragment extends BaseFragment implements AttendanceRow.AttendanceListener, DatePickerDialog.OnDateSetListener {

    public static Date selectedDate = new Date();

    TextView monthTextView;
    Button monthPickerButton;

    ListView attendanceListView;
    WaitingLayout waitingLayout;

    ArrayList<Attendance> attendances;
    AttendanceArrayAdapter arrayAdapter;

    DatePickerDialog monthPickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendance_frame, container, false);

        monthTextView = (TextView) view.findViewById(R.id.monthTextView);
        monthPickerButton = (Button) view.findViewById(R.id.monthPickerButton);
        attendanceListView = (ListView) view.findViewById(R.id.attendanceListView);
        waitingLayout = (WaitingLayout) view.findViewById(R.id.waitingLayout);
        waitingLayout.setVisibility(View.GONE);

        monthPickerDialog = MonthPicker.createDialogWithoutDateField(parentActivity, selectedDate, this);

        monthPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthPickerDialog.show();
            }
        });

        attendances = new ArrayList<Attendance>();
        arrayAdapter = new AttendanceArrayAdapter(parentActivity, R.layout.row_attendance, attendances, this);
        attendanceListView.setAdapter(arrayAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setNavTitle();
        if ( arrayAdapter != null )
            arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void init() {
        getAttendances();
    }

    @Override
    public void setNavTitle() {
        int count = attendances == null ? 0 : attendances.size();
        parentActivity.setNavTitle(String.format("My Attendance (%d)", count));
    }

    @Override
    public void onCancelAttendance(Attendance attendance) {
        if ( arrayAdapter != null ) {
            arrayAdapter.remove(attendance);
            if (arrayAdapter.getCount() == 0)
                waitingLayout.showResult(Constants.kMessageNoAttendance);
        }
        setNavTitle();
    }

    private void getAttendances() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, y");
        monthTextView.setText(sdf.format(selectedDate));

        sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(selectedDate);
        sdf = new SimpleDateFormat("MM");
        String month = sdf.format(selectedDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        int lastInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String sDate = String.format("%s-%s-01", year, month);
        String eDate = String.format("%s-%s-%d", year, month, lastInMonth);
        
        arrayAdapter.clear();
        waitingLayout.showLoadingProgressBar();
        task = WebService.getAttendances(parentActivity, sDate, eDate, new InterfaceHttpRequest.HttpRequestArrayListener() {
            @Override
            public void complete(JSONArray result, String errorMsg) {
                task = null;
                if (result == null) {
                    if (errorMsg == null)
                        errorMsg = "Failure get my attendances";
                    waitingLayout.showResult(errorMsg);
                } else {
                    if (result.length() == 0) {
                        waitingLayout.showResult(Constants.kMessageNoMyWods);
                    } else {
                        waitingLayout.setVisibility(View.GONE);
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                JSONObject jsonObject = result.getJSONObject(i);
                                Attendance attendance = new Attendance(jsonObject);
                                arrayAdapter.add(attendance);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setNavTitle();
                    }
                }
            }
        });

        task.onRun();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        selectedDate = calendar.getTime();
        getAttendances();
    }
}
