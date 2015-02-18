package com.travis.rhinofit.customs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.travis.rhinofit.R;

public class WaitingLayout extends RelativeLayout {
	
	Context context;

    ProgressBar loadingProgressBar;
    TextView    resultTextView;

	public WaitingLayout(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	public WaitingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public WaitingLayout(Context context, AttributeSet attrs,
                         int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
	public void init() {
    	LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_waiting, this);

        loadingProgressBar = (ProgressBar) view.findViewById(R.id.loadingProgressBar);
        resultTextView = (TextView) view.findViewById(R.id.resultTextView);
        loadingProgressBar.setVisibility(View.GONE);
        resultTextView.setVisibility(View.GONE);
    }

    public void showLoadingProgressBar() {
        this.setBackgroundColor(Color.parseColor("#22000000"));
        loadingProgressBar.setVisibility(View.VISIBLE);
        resultTextView.setVisibility(View.GONE);
        this.setVisibility(View.VISIBLE);
    }

    public void showResult(String result) {
        this.setBackgroundColor(Color.parseColor("#ffffff"));
        loadingProgressBar.setVisibility(View.GONE);
        resultTextView.setText(result);
        resultTextView.setVisibility(View.VISIBLE);
        this.setVisibility(View.VISIBLE);
    }
}
