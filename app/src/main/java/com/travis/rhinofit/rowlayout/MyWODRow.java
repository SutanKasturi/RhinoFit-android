package com.travis.rhinofit.rowlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.JSONModel;
import com.travis.rhinofit.models.RhinofitClass;
import com.travis.rhinofit.models.WodInfo;
import com.travis.rhinofit.utils.AlertUtil;
import com.travis.rhinofit.utils.DeviceSize;
import com.travis.rhinofit.utils.ResourceSize;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class MyWODRow extends LinearLayout {

    Context context;
    WodInfo wodInfo;
    MyWODListener myWODListener;

    TextView titleTextView;
    TextView startDateTextView;
    TextView resultsTextView;
    Button editButton;

    public MyWODRow(Context context, WodInfo wodInfo, MyWODListener myWODListener) {
        super(context);
        this.context = context;
        this.wodInfo = wodInfo;
        this.myWODListener = myWODListener;
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(final Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_my_wod, this);

        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        startDateTextView = (TextView) view.findViewById(R.id.startDateTextView);
        resultsTextView = (TextView) view.findViewById(R.id.resultsTextView);
        editButton = (Button) view.findViewById(R.id.editButton);

        editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit();
            }
        });

        setMyWOD();
    }

    private void setMyWOD() {
        if ( wodInfo != null ) {
            titleTextView.setText(wodInfo.getTitle());
            if ( wodInfo.getStartDate() != null ) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                startDateTextView.setText(sdf.format(wodInfo.getStartDate()));
            }
            else {
                startDateTextView.setText("Unknown");
            }
            resultsTextView.setText(wodInfo.getResults());
        }
    }

    private void onEdit() {
        if ( myWODListener != null ) {
            myWODListener.onEdit(wodInfo);
        }
    }

    public interface MyWODListener {
        public void onEdit(WodInfo wodInfo);
    }

}
