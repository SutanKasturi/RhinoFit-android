package com.travis.rhinofit.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.AvailableBenchmarkSpinnerArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.CustomEditText;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.AvailableBenchmarks;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.AvailableBenchmark;
import com.travis.rhinofit.models.BenchmarkHistory;
import com.travis.rhinofit.models.JSONModel;
import com.travis.rhinofit.models.MyBenchmark;
import com.travis.rhinofit.utils.AlertUtil;
import com.travis.rhinofit.utils.DurationPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/13/15.
 */
public class AddBenchmarkFragment extends BaseFragment {

    private static ArrayList<AvailableBenchmark> availableBenchmarkArrayList;

    MyBenchmark benchmark;
    BenchmarkHistory benchmarkHistory;

    Spinner benchmarkSpinner;
    CustomEditText dateEditText;
    CustomEditText measurementEditText;
    ImageButton dateButton;
    TextView typeTextView;
    Button deleteButton;
    Button saveButton;

    WaitingLayout   waitingLayout;

    AddBenchmarkListener listener;

    AvailableBenchmark selectedBenchmark;
    AvailableBenchmarkSpinnerArrayAdapter arrayAdapter;

    DatePickerDialog datePickerDialog;
    Date currentDate;

    public static AddBenchmarkFragment newInstance(MyBenchmark benchmark, AddBenchmarkListener callback) {
        AddBenchmarkFragment addBenchmarkFragment = new AddBenchmarkFragment();
        addBenchmarkFragment.benchmark = benchmark;
        addBenchmarkFragment.benchmarkHistory = null;
        addBenchmarkFragment.listener = callback;
        return addBenchmarkFragment;
    }

    public static AddBenchmarkFragment newInstance(MyBenchmark benchmark, BenchmarkHistory benchmarkHistory, AddBenchmarkListener callback) {
        AddBenchmarkFragment addBenchmarkFragment = new AddBenchmarkFragment();
        addBenchmarkFragment.benchmark = benchmark;
        addBenchmarkFragment.benchmarkHistory = benchmarkHistory;
        addBenchmarkFragment.listener = callback;
        return addBenchmarkFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_benchmark_frame, container, false);

        waitingLayout = (WaitingLayout) view.findViewById(R.id.waitingLayout);
        benchmarkSpinner = (Spinner) view.findViewById(R.id.benchmarkSpinner);
        dateEditText = (CustomEditText) view.findViewById(R.id.dateEditText);
        measurementEditText = (CustomEditText) view.findViewById(R.id.measurementEditText);
        dateButton = (ImageButton) view.findViewById(R.id.dateButton);
        typeTextView = (TextView) view.findViewById(R.id.typeTextView);
        deleteButton = (Button) view.findViewById(R.id.deleteButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        benchmarkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arrayAdapter != null)
                    selectedBenchmark = arrayAdapter.getItem(position);

                onSelectBenchmark();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createDatePicker();

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtil.confirmationAlert(parentActivity, "", Constants.kMessageDeleteBenchmark, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDelete();
                    }
                });
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
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
        parentActivity.setNavTitle("Add Benchmark");
    }

    @Override
    public void init() {
        initBenchmark();
        initAvailableBenchmark();
    }

    private void setEditableBenchmark(boolean isEditable) {
        benchmarkSpinner.setEnabled(isEditable);
        dateEditText.setEnabled(isEditable);
        dateButton.setEnabled(isEditable);
        measurementEditText.setEnabled(isEditable);
        deleteButton.setEnabled(isEditable);
        saveButton.setEnabled(isEditable);

        if ( !isEditable )
            measurementEditText.setText("");

        if ( benchmarkHistory != null ) {
            benchmarkSpinner.setEnabled(false);
        }
    }

    private void initBenchmark() {
        if ( benchmarkHistory == null ) {
            deleteButton.setVisibility(View.GONE);
        }
        setEditableBenchmark(false);
    }

    private void onSelectBenchmark() {
        if ( selectedBenchmark != null ) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            if ( benchmarkHistory != null && benchmarkHistory.getType().equals(selectedBenchmark.getbType()) ) {
                measurementEditText.setText(benchmarkHistory.getValue());
                System.out.println(measurementEditText.getText());
            }
            else if ( benchmark != null && benchmark.getType().equals(selectedBenchmark.getbType()) ) {
                measurementEditText.setText(benchmark.getCurrentScore());
            }
            else {
                if (selectedBenchmark.getbType().equals("minutes:seconds") ||
                        selectedBenchmark.getbType().equals("min:sec")) {
                    measurementEditText.setText("00:00");
                } else {
                    measurementEditText.setText("0");
                }
            }
            typeTextView.setText(selectedBenchmark.getbType());
            setMeasurementEditableType();
        }
    }

    private void setMeasurementEditableType() {
        if ( selectedBenchmark != null ) {
            if ( selectedBenchmark.getbType().equals("minutes:seconds") ||
                    selectedBenchmark.getbType().equals("min:sec") ) {
                measurementEditText.setFocusable(false);
                measurementEditText.setFocusableInTouchMode(false);
                measurementEditText.setClickable(true);
                measurementEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] minsec = measurementEditText.getText().toString().split(":");
                        int min = 0;
                        int sec = 0;
                        if ( minsec.length == 2 ) {
                            min = Integer.parseInt(minsec[0]);
                            sec = Integer.parseInt(minsec[1]);
                        }
                        else {
                            min = Integer.parseInt(measurementEditText.getText().toString());
                            sec = 0;
                        }
                        DurationPicker durationPicker = new DurationPicker(parentActivity, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String min1 = hourOfDay > 9 ? "" + hourOfDay : "0" + hourOfDay;
                                String sec1 = minute > 9 ? "" + minute : "0" + minute;
                                measurementEditText.setText(min1 + ":" + sec1);
                            }
                        }, min, sec);
                        durationPicker.show();
                    }
                });
            }
            else {
                measurementEditText.setFocusable(true);
                measurementEditText.setFocusableInTouchMode(true);
                measurementEditText.setClickable(false);
            }
        }
    }

    private void getAvailableBenchmark() {
        if ( availableBenchmarkArrayList == null ) {
            waitingLayout.showLoadingProgressBar();

            AvailableBenchmarks.getAvailableBenchmarks(parentActivity, new AvailableBenchmarks.AvailableBenchmarksListener() {
                @Override
                public void didLoadAvailableBenchmarks(ArrayList<AvailableBenchmark> availableBenchmarks1, String errorMsg) {
                    if (availableBenchmarks1 == null) {
                        if (errorMsg == null)
                            errorMsg = "Failure get available benchmarks";
                        waitingLayout.showResult(errorMsg);
                    } else {
                        if (availableBenchmarks1.size() == 0) {
                            waitingLayout.showResult(Constants.kMessageNoAvailableBenchmarks);
                        } else {
                            waitingLayout.setVisibility(View.GONE);

                            availableBenchmarkArrayList = availableBenchmarks1;

                            initAvailableBenchmark();
                        }
                    }
                }
            });
        }
    }

    private void initAvailableBenchmark() {
        if ( availableBenchmarkArrayList == null ) {
            setEditableBenchmark(false);
            getAvailableBenchmark();
        }
        else if ( availableBenchmarkArrayList.size() == 0 ) {
            waitingLayout.showResult(Constants.kMessageNoAvailableBenchmarks);
        }
        else {
            waitingLayout.setVisibility(View.GONE);
            setEditableBenchmark(true);

            arrayAdapter = new AvailableBenchmarkSpinnerArrayAdapter(parentActivity, R.layout.row_available_benchmark_spinner, availableBenchmarkArrayList);
            benchmarkSpinner.setAdapter(arrayAdapter);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            if ( benchmark != null ) {
                int i = 0;
                for ( AvailableBenchmark availableBenchmark1:availableBenchmarkArrayList ) {
                    if ( availableBenchmark1.getBenchmarkId() == benchmark.getBenchmarkId() ) {
                        selectedBenchmark = availableBenchmark1;
                        benchmarkSpinner.setSelection(i, true);

                        break;
                    }
                    i++;
                }

                if ( benchmarkHistory != null ) {
                    dateEditText.setText(sdf.format(benchmarkHistory.getDate()));
                    measurementEditText.setText(benchmarkHistory.getValue());
                }
                else {
                    dateEditText.setText(sdf.format(benchmark.getCurrentDate()));
                    measurementEditText.setText(benchmark.getCurrentScore());
                }
            }
            else {
                benchmarkSpinner.setSelection(0, true);
                selectedBenchmark = availableBenchmarkArrayList.get(0);
                onSelectBenchmark();
                dateEditText.setText(sdf.format(currentDate));
            }
            typeTextView.setText(selectedBenchmark.getbType());
        }
    }

    private void createDatePicker() {
        if ( currentDate == null )
            currentDate = new Date();

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(currentDate);
        datePickerDialog = new DatePickerDialog(parentActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                currentDate = newDate.getTime();
                setDateEditText();
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setDateEditText() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        dateEditText.setText(sdf.format(currentDate));
    }

    private void onDelete() {
        final ProgressDialog progressDialog = new ProgressDialog(parentActivity);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();

        WebService.deleteBenchmark(parentActivity,
                benchmark.getBenchmarkId(),
                benchmarkHistory.getDate(),
                benchmarkHistory.getValue(),
                benchmarkHistory.getBenchmarkDataId(),
                new InterfaceHttpRequest.HttpRequestArrayListener() {
                    @Override
                    public void complete(JSONArray result, String errorMsg) {
                        progressDialog.dismiss();
                        if ( result != null ) {
                            if ( listener != null )
                                listener.onDeleteBenchmark();

                            parentActivity.onBackPressed();
                        }
                        else {
                            if ( errorMsg == null )
                                errorMsg = "Failure delete benchmark";

                            AlertUtil.messageAlert(parentActivity, "Error", errorMsg);
                        }
                    }
                }).onRun();
    }
    private void onSave() {
        final ProgressDialog progressDialog = new ProgressDialog(parentActivity);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        int dataId = -1;
        if ( benchmarkHistory != null )
            dataId = benchmarkHistory.getBenchmarkDataId();
        final String measureString = measurementEditText.getText().toString();

        WebService.addBenchmark(parentActivity,
                selectedBenchmark.getBenchmarkId(),
                currentDate,
                measureString,
                dataId,
                new InterfaceHttpRequest.HttpRequestJsonListener() {
                    @Override
                    public void complete(JSONObject result, String errorMsg) {
                        progressDialog.dismiss();
                        if ( result != null && !JSONModel.isNull(result, Constants.kParamId) ) {
                            NewBenchmark newBenchmark = new NewBenchmark(selectedBenchmark, currentDate, measureString);
                            if ( benchmarkHistory != null ) {
                                benchmarkHistory.setBenchmarkDataId(JSONModel.getIntFromJson(result, Constants.kParamId));
                                benchmarkHistory.setDate(currentDate);
                                benchmarkHistory.setValue(measureString);
                            }
                            if ( listener != null )
                                listener.onAddBenchmark(newBenchmark);

                            parentActivity.onBackPressed();
                        }
                        else {
                            if ( errorMsg == null ) {
                                errorMsg = "Failure add new benchmark";
                            }
                            AlertUtil.messageAlert(parentActivity, "Error", errorMsg);
                        }
                    }
                }).onRun();
    }

    public class NewBenchmark {
        AvailableBenchmark selectedBenchmark;
        Date date;
        String measurement;

        public NewBenchmark(AvailableBenchmark availableBenchmark, Date date, String measurement) {
            this.selectedBenchmark = availableBenchmark;
            this.date = date;
            this.measurement = measurement;
        }

        public AvailableBenchmark getSelectedBenchmark() {
            return selectedBenchmark;
        }

        public void setSelectedBenchmark(AvailableBenchmark selectedBenchmark) {
            this.selectedBenchmark = selectedBenchmark;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getMeasurement() {
            return measurement;
        }

        public void setMeasurement(String measurement) {
            this.measurement = measurement;
        }
    }
    public interface AddBenchmarkListener {
        public void onAddBenchmark(NewBenchmark newBenchmark);
        public void onDeleteBenchmark();
    }
}
