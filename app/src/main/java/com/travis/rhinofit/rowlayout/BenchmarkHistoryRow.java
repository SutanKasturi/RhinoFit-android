package com.travis.rhinofit.rowlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.models.BenchmarkHistory;

import java.text.SimpleDateFormat;

/**
 * Created by Sutan Kasturi on 2/13/15.
 */
public class BenchmarkHistoryRow extends LinearLayout {

    Context context;
    BenchmarkHistory benchmark;
    BenchmarkHistoryListener benchmarkListener;

    TextView dateTextView;
    TextView valueTextView;
    Button updateButton;

    public BenchmarkHistoryRow(Context context, BenchmarkHistory benchmark, BenchmarkHistoryListener benchmarkListener) {
        super(context);
        this.context = context;
        this.benchmark = benchmark;
        this.benchmarkListener = benchmarkListener;
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(final Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_benchmark_history, this);

        dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        valueTextView = (TextView) view.findViewById(R.id.valueTextView);
        updateButton = (Button) view.findViewById(R.id.updateHistoryButton);

        updateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( benchmarkListener != null ) {
                    benchmarkListener.onUpdateBenchmarkHistory(benchmark);
                }
            }
        });

        setBenchmarkHistory();
    }

    private void setBenchmarkHistory() {
        if ( benchmark != null ) {
            if ( benchmark.getDate() != null ) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                dateTextView.setText(sdf.format(benchmark.getDate()));
            }
            valueTextView.setText(benchmark.getValue());
        }
    }


    public interface BenchmarkHistoryListener {
        public void onUpdateBenchmarkHistory(BenchmarkHistory benchmark);
    }
}
