package com.travis.rhinofit.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.MyWODsArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.CustomButton;
import com.travis.rhinofit.customs.CustomEditText;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.JSONModel;
import com.travis.rhinofit.models.RhinofitClass;
import com.travis.rhinofit.models.WodInfo;
import com.travis.rhinofit.utils.AlertUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class TrackWODFragment extends BaseFragment {

    TextView        nameTextView;
    TextView        timeTextView;
    TextView        dateTextView;
    CustomEditText  titleEditText;
    CustomEditText  descriptionEditText;
    CustomEditText  resultEditText;
    Button          trackWODButton;

    RelativeLayout  waitingContainerLayout;
    WaitingLayout   waitingLayout;

    RhinofitClass   rfClass;
    WodInfo         wodInfo;
    MyWODsArrayAdapter arrayAdapter;

    String          classId;
    Date            startDate;

    public static TrackWODFragment newInstance(RhinofitClass rfClass) {
        TrackWODFragment trackWODFragment = new TrackWODFragment();
        trackWODFragment.rfClass = rfClass;
        return trackWODFragment;
    }

    public static TrackWODFragment newInstance(WodInfo wodInfo, MyWODsArrayAdapter arrayAdapter) {
        TrackWODFragment trackWODFragment = new TrackWODFragment();
        trackWODFragment.wodInfo = wodInfo;
        trackWODFragment.arrayAdapter = arrayAdapter;
        return trackWODFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.track_wod_frame, container, false);

        waitingContainerLayout = (RelativeLayout) view;
        waitingLayout = (WaitingLayout) view.findViewById(R.id.waitingLayout);
        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        titleEditText = (CustomEditText) view.findViewById(R.id.titleEditText);
        descriptionEditText = (CustomEditText) view.findViewById(R.id.descriptionEditText);
        resultEditText = (CustomEditText) view.findViewById(R.id.resultEditText);
        trackWODButton = (Button) view.findViewById(R.id.trackWODButton);

        trackWODButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrackWOD();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setNavTitle();
    }

    @Override
    public void setNavTitle() {
        parentActivity.setNavTitle("Track WOD");
    }

    @Override
    public void init() {
        if ( rfClass != null ) {
            classId = rfClass.getClassId();
            startDate = rfClass.getStartDate();
        }
        else if ( wodInfo != null ) {
            classId = wodInfo.getClassId();
            startDate = wodInfo.getStartDate();
        }
        setupWODInfo();
        getWODInfo();
    }

    private void setupWODInfo() {
        if ( wodInfo != null ) {
            nameTextView.setText(wodInfo.getName());
            titleEditText.setText(wodInfo.getTitle());
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            timeTextView.setText(sdf.format(startDate));
            sdf = new SimpleDateFormat("MMM dd, yyyy");
            dateTextView.setText(sdf.format(startDate));
            descriptionEditText.setText(wodInfo.getWod());
            resultEditText.setText(wodInfo.getResults());

            if ( wodInfo.isCanEdit() ) {
                titleEditText.setEnabled(true);
                descriptionEditText.setEnabled(true);
                titleEditText.setBackgroundResource(R.drawable.rounded_edittext);
                descriptionEditText.setBackgroundResource(R.drawable.rounded_edittext);
            }
            else {
                titleEditText.setEnabled(false);
                descriptionEditText.setEnabled(false);
                titleEditText.setBackgroundResource(R.drawable.disable_bg);
                descriptionEditText.setBackgroundResource(R.drawable.disable_bg);
            }
            resultEditText.setEnabled(true);
            resultEditText.setBackgroundResource(R.drawable.rounded_edittext);
            trackWODButton.setEnabled(true);
        }
        else {
            nameTextView.setText("");
            timeTextView.setText("");
            dateTextView.setText("");
            titleEditText.setText("");
            titleEditText.setEnabled(false);
            titleEditText.setBackgroundColor(Color.parseColor("#00000000"));
            descriptionEditText.setText("");
            descriptionEditText.setEnabled(false);
            descriptionEditText.setBackgroundColor(Color.parseColor("#00000000"));
            resultEditText.setText("");
            resultEditText.setEnabled(false);
            resultEditText.setBackgroundColor(Color.parseColor("#00000000"));
            trackWODButton.setEnabled(false);
        }
    }
    private void getWODInfo() {

        waitingLayout.showLoadingProgressBar();

        task = WebService.getWodInfo(parentActivity, classId, startDate, new InterfaceHttpRequest.HttpRequestJsonListener() {
            @Override
            public void complete(JSONObject result, String errorMsg) {
                task = null;
                if (result == null) {
                    if (errorMsg != null)
                        waitingLayout.showResult(errorMsg);
                    else
                        waitingLayout.showResult(Constants.kMessageUnkownError);
                } else {
                    waitingLayout.setVisibility(View.GONE);
                    WodInfo temp = new WodInfo(result);
                    if ( wodInfo != null ) {
                        wodInfo.setWodId(temp.getWodId());
                        wodInfo.setWod(temp.getWod());
                        wodInfo.setResults(temp.getResults());
                        wodInfo.setTitle(temp.getTitle());
                        wodInfo.setCanEdit(temp.isCanEdit());
                        wodInfo.setClassId(temp.getClassId());
                        wodInfo.setName(temp.getName());
                        wodInfo.setStartDate(temp.getStartDate());
                    }
                    else {
                        wodInfo = temp;
                    }
                    setupWODInfo();
                }
            }
        });
        task.onRun();
    }

    private void onTrackWOD() {
        boolean isValid = true;
        if ( wodInfo.isCanEdit() ) {
            if ( !titleEditText.isValidInput() ) {
                isValid = false;
            }

            if ( !descriptionEditText.isValidInput() ) {
                isValid = false;
            }
        }

        if ( !resultEditText.isValidInput() ) {
            isValid = false;
        }

        if ( isValid == false ) {
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(parentActivity);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        WebService.trackWod(parentActivity,
                classId,
                startDate,
                descriptionEditText.getText().toString(),
                titleEditText.getText().toString(),
                resultEditText.getText().toString(),
                new InterfaceHttpRequest.HttpRequestJsonListener() {
                    @Override
                    public void complete(JSONObject result, String errorMsg) {
                        progressDialog.dismiss();
                        if ( result == null ) {
                            if ( errorMsg == null )
                                errorMsg = "Failure track WOD";
                            AlertUtil.messageAlert(parentActivity, "Track WOD", errorMsg);
                        }
                        else {
                            if ( rfClass != null ) {
                                MyWODsFragment myWODsFragment = MyWODsFragment.newInstance(rfClass.getStartDate());
                                MenuFragment.setCurrentMenu(MenuFragment.MenuIds.MY_WODS);
                                parentActivity.switchContentWithAnimate(myWODsFragment);
                            }
                            else {
                                wodInfo.setTitle(titleEditText.getText().toString());
                                wodInfo.setWod(descriptionEditText.getText().toString());
                                wodInfo.setResults(resultEditText.getText().toString());
                                wodInfo.setWodId(JSONModel.getStringFromJson(result, Constants.kResponseKeyWodWodId));
                                if ( arrayAdapter != null )
                                    arrayAdapter.notifyDataSetChanged();
                                parentActivity.onBackPressed();
                            }
                        }
                    }
                }).onRun();
    }

    public RhinofitClass getRfClass() {
        return rfClass;
    }

    public void setRfClass(RhinofitClass rfClass) {
        this.rfClass = rfClass;
    }

    public WodInfo getWodInfo() {
        return wodInfo;
    }

    public void setWodInfo(WodInfo wodInfo) {
        this.wodInfo = wodInfo;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
