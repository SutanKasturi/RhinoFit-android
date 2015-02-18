package com.travis.rhinofit.rowlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import com.travis.rhinofit.models.Attendance;
import com.travis.rhinofit.utils.AlertUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class AttendanceRow extends LinearLayout {

    Context context;
    Attendance attendance;
    AttendanceListener attendanceListener;

    TextView titleTextView;
    TextView timeTextView;
    TextView whenTextView;
    Button attendanceButton;
    ProgressBar attendanceProgressBar;

    public AttendanceRow(Context context, Attendance attendance, AttendanceListener attendanceListener) {
        super(context);
        this.context = context;
        this.attendance = attendance;
        this.attendanceListener = attendanceListener;
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(final Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_attendance, this);

        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        whenTextView = (TextView) view.findViewById(R.id.whenTextView);
        attendanceButton = (Button) view.findViewById(R.id.attendanceButton);

        attendanceProgressBar = (ProgressBar) view.findViewById(R.id.attendanceProgressBar);

        attendanceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtil.confirmationAlert(context, "", Constants.kMessageCancelAttendance, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onCancelAttendance();
                    }
                });
            }
        });

        setAttendance();
    }

    private void setAttendance() {

        if ( attendance.isActionAttendance() ) {
            attendanceButton.setEnabled(false);
            attendanceProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            attendanceButton.setEnabled(true);
            attendanceProgressBar.setVisibility(View.GONE);
        }

        titleTextView.setText(attendance.getTitle());
        if ( attendance.getWhen() != null ) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            timeTextView.setText(sdf.format(attendance.getWhen()));
            sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            whenTextView.setText(sdf.format(attendance.getWhen()));
        }
    }
    
    private void onCancelAttendance() {
        attendanceButton.setEnabled(false);
        attendanceProgressBar.setVisibility(View.VISIBLE);
        WebService.deleteAttendance(context, attendance.getAttendanceId(), new InterfaceHttpRequest.HttpRequestJsonListener() {
            @Override
            public void complete(JSONObject result, String errorMsg) {
                attendance.setActionAttendance(false);
                if (errorMsg == null || errorMsg.isEmpty()) {
                    attendance.setAttendanceId(-1);
                    if (attendanceListener != null)
                        attendanceListener.onCancelAttendance(attendance);
                } else {
                    if (errorMsg == null)
                        errorMsg = "Failure cancel attendance request";
                    AlertUtil.messageAlert(context, "Cancel Attendance", errorMsg);
                }
                attendanceButton.setEnabled(true);
            }
        }).onRun();
    }

    public interface AttendanceListener {
        public void onCancelAttendance(Attendance attendance);
    }
}
