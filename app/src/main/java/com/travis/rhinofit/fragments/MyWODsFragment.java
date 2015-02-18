package com.travis.rhinofit.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.MyWODsArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.JSONModel;
import com.travis.rhinofit.models.WodInfo;
import com.travis.rhinofit.rowlayout.MyWODRow;
import com.travis.rhinofit.utils.AlertUtil;
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
public class MyWODsFragment extends BaseFragment implements MyWODRow.MyWODListener, DatePickerDialog.OnDateSetListener {

    Date startDate;

    TextView            monthTextView;
    Button              monthPickerButton;
    ListView            myWODsListView;
    WaitingLayout       waitingLayout;

    ArrayList<WodInfo>  myWODs;
    MyWODsArrayAdapter  arrayAdapter;

    DatePickerDialog    monthPickerDialog;

    public static MyWODsFragment newInstance(Date startDate) {
        MyWODsFragment myWODsFragment = new MyWODsFragment();
        myWODsFragment.startDate = startDate;
        return myWODsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_wod_frame, container, false);

        monthTextView = (TextView) view.findViewById(R.id.monthTextView);
        monthPickerButton = (Button) view.findViewById(R.id.monthPickerButton);
        myWODsListView = (ListView) view.findViewById(R.id.myWODsListView);
        waitingLayout = (WaitingLayout) view.findViewById(R.id.waitingLayout);
        waitingLayout.setVisibility(View.GONE);

        monthPickerDialog = MonthPicker.createDialogWithoutDateField(parentActivity, startDate, this);

        monthPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthPickerDialog.show();
            }
        });

        myWODs = new ArrayList<WodInfo>();
        arrayAdapter = new MyWODsArrayAdapter(parentActivity, R.layout.row_my_wod, myWODs, this);
        myWODsListView.setAdapter(arrayAdapter);

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
        getMyWODs();
    }

    @Override
    public void setNavTitle() {
        int count = myWODs == null ? 0 : myWODs.size();
        parentActivity.setNavTitle(String.format("My WODS (%d)", count));
    }

    @Override
    public void onEdit(WodInfo wodInfo) {
        parentActivity.changeFragment(TrackWODFragment.newInstance(wodInfo, arrayAdapter));
    }

    private void getMyWODs() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, y");
        monthTextView.setText(sdf.format(startDate));

        sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(startDate);
        sdf = new SimpleDateFormat("MM");
        String month = sdf.format(startDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int lastInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String sDate = String.format("%s-%s-01", year, month);
        String eDate = String.format("%s-%s-%d", year, month, lastInMonth);

        arrayAdapter.clear();
        waitingLayout.showLoadingProgressBar();
        task = WebService.getMyWOD(parentActivity, sDate, eDate, new InterfaceHttpRequest.HttpRequestArrayListener() {
            @Override
            public void complete(JSONArray result, String errorMsg) {
                task = null;
                if (result == null) {
                    if (errorMsg == null)
                        errorMsg = "Failure track WOD";
                    waitingLayout.showResult(errorMsg);
                } else {
                    if (result.length() == 0) {
                        waitingLayout.showResult(Constants.kMessageNoMyWods);
                    } else {
                        waitingLayout.setVisibility(View.GONE);
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                JSONObject jsonObject = result.getJSONObject(i);
                                WodInfo wodInfo = new WodInfo(jsonObject);
                                arrayAdapter.add(wodInfo);
                            }
                            catch (JSONException e) {
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
        startDate = calendar.getTime();
        getMyWODs();
    }
}
