package com.travis.rhinofit.rowlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.MyBenchmark;
import com.travis.rhinofit.utils.AlertUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class BenchmarkRow extends LinearLayout {

    Context context;
    MyBenchmark benchmark;
    BenchmarkListener benchmarkListener;

    TextView titleTextView;
    TextView currentScoreTextView;
    TextView currentDateTextView;
    TextView lastScoreTextView;
    TextView lastDateTextView;
    Button historyButton;
    Button updateButton;

    public BenchmarkRow(Context context, MyBenchmark benchmark, BenchmarkListener benchmarkListener) {
        super(context);
        this.context = context;
        this.benchmark = benchmark;
        this.benchmarkListener = benchmarkListener;
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(final Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_benchmark, this);

        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        currentScoreTextView = (TextView) view.findViewById(R.id.currentScoreTextView);
        currentDateTextView = (TextView) view.findViewById(R.id.currentDateTextView);
        lastScoreTextView = (TextView) view.findViewById(R.id.lastScoreTextView);
        lastDateTextView = (TextView) view.findViewById(R.id.lastDateTextView);
        historyButton = (Button) view.findViewById(R.id.historyButton);
        updateButton = (Button) view.findViewById(R.id.updatebutton);

        historyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( benchmarkListener != null ) {
                    benchmarkListener.onHistoryBenchmark(benchmark);
                }
            }
        });

        updateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( benchmarkListener != null ) {
                    benchmarkListener.onUpdateBenchmark(benchmark);
                }
            }
        });

        setBenchmark();
    }

    private void setBenchmark() {

        if ( benchmark != null ) {
            titleTextView.setText(benchmark.getTitle());
            currentScoreTextView.setText(String.format("Current PR: %s %s", benchmark.getCurrentScore(), benchmark.getType()));
            lastScoreTextView.setText(String.format("Last Benchmark: %s %s", benchmark.getLastScore(), benchmark.getType()));

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            currentDateTextView.setText(sdf.format(benchmark.getCurrentDate()));
            lastDateTextView.setText(sdf.format(benchmark.getLastDate()));
        }
    }


    public interface BenchmarkListener {
        public void onHistoryBenchmark(MyBenchmark benchmark);
        public void onUpdateBenchmark(MyBenchmark benchmark);
    }
}
