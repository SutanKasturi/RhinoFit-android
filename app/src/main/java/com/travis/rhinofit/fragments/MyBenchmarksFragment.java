package com.travis.rhinofit.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.BenchmarkArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.AvailableBenchmarks;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.AvailableBenchmark;
import com.travis.rhinofit.models.MyBenchmark;
import com.travis.rhinofit.rowlayout.BenchmarkRow;
import com.travis.rhinofit.utils.UtilsValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class MyBenchmarksFragment extends BaseFragment implements BenchmarkRow.BenchmarkListener, AddBenchmarkFragment.AddBenchmarkListener, BenchmarkHistoryFragment.BenchmarkHistoryListener {
    ListView benchmarkListView;
    Button addBenchmarkButton;
    WaitingLayout waitingLayout;

    ArrayList<MyBenchmark> benchmarks;
    BenchmarkArrayAdapter arrayAdapter;

    MyBenchmark selectedBenchmark;

    ArrayList<AvailableBenchmark> availableBenchmarkArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mybenchmarks_frame, container, false);

        benchmarkListView = (ListView) view.findViewById(R.id.benchmarkListView);
        addBenchmarkButton = (Button) view.findViewById(R.id.addBenchmarkButton);
        waitingLayout = (WaitingLayout) view.findViewById(R.id.waitingLayout);
        waitingLayout.setVisibility(View.GONE);

        benchmarks = new ArrayList<MyBenchmark>();
        arrayAdapter = new BenchmarkArrayAdapter(parentActivity, R.layout.row_benchmark, benchmarks, this);
        benchmarkListView.setAdapter(arrayAdapter);

        addBenchmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedBenchmark = null;
                parentActivity.changeFragment(AddBenchmarkFragment.newInstance(null, MyBenchmarksFragment.this));
            }
        });

        UtilsValues.messageHandler = new Handler() {

            public void handleMessage(Message msg) {

                if (msg.what == 1) {
                    init();
                }
            }
        };

        addBenchmarkButton.setVisibility(View.GONE);
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
        getAvailableBenchmark();
    }

    @Override
    public void setNavTitle() {
        int count = benchmarks == null ? 0 : benchmarks.size();
        parentActivity.setNavTitle(String.format("My Benchmarks (%d)", count));
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
                            addBenchmarkButton.setVisibility(View.VISIBLE);
                            getBenchmarks();
                        }
                    }
                }
            });
        }
    }

    private void getBenchmarks() {
        if ( arrayAdapter != null ) {
            arrayAdapter.clear();
            arrayAdapter.notifyDataSetChanged();
        }
        waitingLayout.showLoadingProgressBar();
        task = WebService.getMyBenchmarks(parentActivity, new InterfaceHttpRequest.HttpRequestArrayListener() {
            @Override
            public void complete(JSONArray result, String errorMsg) {
                task = null;
                if (result == null) {
                    if (errorMsg == null)
                        errorMsg = "Failure get my benchmarks";
                    waitingLayout.showResult(errorMsg);
                } else {
                    if (result.length() == 0) {
                        waitingLayout.showResult(Constants.kMessageNoMyBenchmarks);
                    } else {
                        waitingLayout.setVisibility(View.GONE);
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                JSONObject jsonObject = result.getJSONObject(i);
                                MyBenchmark benchmark = new MyBenchmark(jsonObject);
                                if ( isPossibleBenchmark(benchmark) )
                                    arrayAdapter.add(benchmark);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if ( arrayAdapter.getCount() == 0 ) {
                            waitingLayout.showResult(Constants.kMessageNoMyBenchmarks);
                        }
                    }
                    if ( isResumed() )
                        setNavTitle();
                }
            }
        });

        task.onRun();
    }

    private boolean isPossibleBenchmark(MyBenchmark benchmark) {
        for ( int i = 0; i < availableBenchmarkArrayList.size(); i++ ) {
            if ( availableBenchmarkArrayList.get(i).getBenchmarkId() == benchmark.getBenchmarkId() ) {
                return true;
            }
        }
        return false;
    }
    // ListView Listener
    @Override
    public void onHistoryBenchmark(MyBenchmark benchmark) {
        selectedBenchmark = benchmark;
        parentActivity.changeFragment(BenchmarkHistoryFragment.newInstance(benchmark, this));
    }

    @Override
    public void onUpdateBenchmark(MyBenchmark benchmark) {
        selectedBenchmark = benchmark;
        parentActivity.changeFragment(AddBenchmarkFragment.newInstance(benchmark, MyBenchmarksFragment.this));
    }

    // AddBenchmarkListener
    @Override
    public void onAddBenchmark(AddBenchmarkFragment.NewBenchmark newBenchmark) {
        getBenchmarks();
//        AvailableBenchmark availableBenchmark = newBenchmark.getSelectedBenchmark();
//        Date date = newBenchmark.getDate();
//        String result = newBenchmark.getMeasurement();
//
//        MyBenchmark selectedBenchmark = null;
//        int index = -1;
//
//        for ( int i = 0; i < benchmarks.size(); i++ ) {
//            MyBenchmark b = benchmarks.get(i);
//            if ( b.getBenchmarkId() >= availableBenchmark.getBenchmarkId() ) {
//                if ( b.getBenchmarkId() == availableBenchmark.getBenchmarkId() ) {
//                    selectedBenchmark = b;
//                }
//                index = i;
//            }
//        }
//
//        boolean isNew = false;
//        if ( selectedBenchmark == null ) {
//            isNew = true;
//            selectedBenchmark = new MyBenchmark();
//            selectedBenchmark.setBenchmarkId(availableBenchmark.getBenchmarkId());
//            selectedBenchmark.setTitle(availableBenchmark.getbDescription());
//            selectedBenchmark.setType(availableBenchmark.getbType());
//            if ( index < 0 ) {
//                index = benchmarks.size();
//            }
//        }
//
//        selectedBenchmark.setLastDate(date);
//        selectedBenchmark.setLastScore(result);
//
//        if ( isNew == false ) {
//            boolean isUpdated = false;
//            if ( availableBenchmark.getbType().equals("minutes:seconds") ||
//                    availableBenchmark.getbType().equals("min:sec") ) {
//                if ( getSeconds(result) > getSeconds(selectedBenchmark.getCurrentScore()) ) {
//                    isUpdated = true;
//                }
//            }
//            else {
//                if ( Integer.parseInt(result) > Integer.parseInt(selectedBenchmark.getCurrentScore()) ) {
//                    isUpdated = true;
//                }
//            }
//
//            if ( isUpdated ) {
//                selectedBenchmark.setCurrentDate(date);
//                selectedBenchmark.setCurrentScore(result);
//            }
//        }
//        else {
//            selectedBenchmark.setCurrentDate(date);
//            selectedBenchmark.setCurrentScore(result);
//            arrayAdapter.insert(selectedBenchmark, index);
//        }
//
//        if ( isNew ) {
//            if ( arrayAdapter.getCount() > 0 )
//                waitingLayout.setVisibility(View.GONE);
//        }
//        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteBenchmark() {
        arrayAdapter.remove(selectedBenchmark);
        if ( arrayAdapter.getCount() == 0 ) {
            waitingLayout.showResult(Constants.kMessageNoMyBenchmarks);
        }
    }

    public static int getSeconds(String minsec) {
        if ( minsec == null || minsec == "" ) {
            return 0;
        }

        String[] values = minsec.split(":");
        if ( values.length == 2 ) {
            return Integer.parseInt(values[0]) * 60 + Integer.parseInt(values[1]);
        }
        return 0;
    }

    @Override
    public void didChangeBenchmark(boolean isDeleted) {
        if ( isDeleted ) {
            onDeleteBenchmark();
        }
        else
            getBenchmarks();
//            arrayAdapter.notifyDataSetChanged();
    }
}
