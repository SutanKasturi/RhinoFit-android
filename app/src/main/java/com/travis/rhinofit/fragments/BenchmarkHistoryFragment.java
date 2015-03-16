package com.travis.rhinofit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.BenchmarkHistoryArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.BenchmarkHistory;
import com.travis.rhinofit.models.MyBenchmark;
import com.travis.rhinofit.models.RhinofitClass;
import com.travis.rhinofit.rowlayout.BenchmarkHistoryRow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Sutan Kasturi on 2/13/15.
 */
public class BenchmarkHistoryFragment extends BaseFragment implements BenchmarkHistoryRow.BenchmarkHistoryListener, AddBenchmarkFragment.AddBenchmarkListener {

    TextView typeTextView;
    ListView benchmarkHistoryListView;
    WaitingLayout waitingLayout;

    MyBenchmark benchmark;
    BenchmarkHistory selectedBenchmarkHistory;
    ArrayList<BenchmarkHistory> benchmarkHistorys;
    BenchmarkHistoryArrayAdapter arrayAdapter;

    BenchmarkHistoryListener historyListener;

    public static BenchmarkHistoryFragment newInstance(MyBenchmark benchmark, BenchmarkHistoryListener historyCallback) {
        BenchmarkHistoryFragment benchmarkHistoryFragment = new BenchmarkHistoryFragment();
        benchmarkHistoryFragment.benchmark = benchmark;
        benchmarkHistoryFragment.historyListener = historyCallback;
        return benchmarkHistoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.benchmark_history_frame, container, false);

        typeTextView = (TextView) view.findViewById(R.id.typeTextView);
        benchmarkHistoryListView = (ListView) view.findViewById(R.id.benchmarkHistoryListView);
        waitingLayout = (WaitingLayout) view.findViewById(R.id.waitingLayout);
        waitingLayout.setVisibility(View.GONE);

        benchmarkHistorys = new ArrayList<BenchmarkHistory>();
        arrayAdapter = new BenchmarkHistoryArrayAdapter(parentActivity, R.layout.row_benchmark_history, benchmarkHistorys, this);
        benchmarkHistoryListView.setAdapter(arrayAdapter);

        typeTextView.setText(benchmark.getType());

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
        getBenchmarkHistorys();
    }

    @Override
    public void setNavTitle() {
        parentActivity.setNavTitle(String.format("%s - History", benchmark.getTitle()));
    }

    private void getBenchmarkHistorys() {
        if ( benchmark == null )
            return;

        waitingLayout.showLoadingProgressBar();
        task = WebService.getBenchmarkHistories(parentActivity, benchmark.getBenchmarkId(), new InterfaceHttpRequest.HttpRequestArrayListener() {
            @Override
            public void complete(JSONArray result, String errorMsg) {
                task = null;
                if (result == null) {
                    if (errorMsg == null)
                        errorMsg = "Failure get my benchmark histories";
                    waitingLayout.showResult(errorMsg);
                } else {
                    if (result.length() == 0) {
                        waitingLayout.showResult(Constants.kMessageNoMyBenchmarkHistories);
                    } else {
                        waitingLayout.setVisibility(View.GONE);
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                JSONObject jsonObject = result.getJSONObject(i);
                                BenchmarkHistory benchmarkHistory = new BenchmarkHistory(jsonObject, benchmark.getType());
                                benchmarkHistorys.add(benchmarkHistory);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Collections.sort(benchmarkHistorys);
                        arrayAdapter.notifyDataSetChanged();

                        setNavTitle();
                    }
                }
            }
        });

        task.onRun();
    }


    // History Row Listener
    @Override
    public void onUpdateBenchmarkHistory(BenchmarkHistory benchmarkHistory) {
        selectedBenchmarkHistory = benchmarkHistory;
        parentActivity.changeFragment(AddBenchmarkFragment.newInstance(benchmark, benchmarkHistory, this));
    }

    // Add benchmark Listener
    @Override
    public void onAddBenchmark(AddBenchmarkFragment.NewBenchmark newBenchmark) {
        arrayAdapter.notifyDataSetChanged();
        setBenchmark();
        if ( historyListener != null )
            historyListener.didChangeBenchmark(false);
    }

    @Override
    public void onDeleteBenchmark() {
        arrayAdapter.remove(selectedBenchmarkHistory);
        if (arrayAdapter.getCount() == 0 ) {
            if ( historyListener != null )
                historyListener.didChangeBenchmark(true);
            parentActivity.onBackPressed();
        }
    }

    private void setBenchmark() {
        if ( benchmarkHistorys.size() > 0 ) {
            Collections.sort(benchmarkHistorys);
            BenchmarkHistory history = benchmarkHistorys.get(0);
            benchmark.setLastDate(history.getDate());
            benchmark.setLastScore(history.getValue());
            for ( BenchmarkHistory benchmarkHistory:benchmarkHistorys ) {
                if ( benchmark.getType().equals("minutes:seconds")
                        || benchmark.getType().equals("min:sec") ) {
                    if ( MyBenchmarksFragment.getSeconds(history.getValue()) < MyBenchmarksFragment.getSeconds(benchmarkHistory.getValue()) ) {
                        history = benchmarkHistory;
                    }
                }
                else {
                    if ( Integer.parseInt(history.getValue()) < Integer.parseInt(benchmarkHistory.getValue()) ) {
                        history = benchmarkHistory;
                    }
                }
            }
            benchmark.setCurrentDate(history.getDate());
            benchmark.setCurrentScore(history.getValue());
        }
    }

    public interface BenchmarkHistoryListener {
        public void didChangeBenchmark(boolean isDeleted);
    }
}
