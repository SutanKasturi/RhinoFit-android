package com.travis.rhinofit.rowlayout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
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
import com.travis.rhinofit.utils.AlertUtil;
import com.travis.rhinofit.utils.DateUtils;
import com.travis.rhinofit.utils.DeviceSize;
import com.travis.rhinofit.utils.ResourceSize;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ClassRow extends LinearLayout {

    Context context;
    RhinofitClass rfClass;
    ClassListener classListener;
    Date selectedDate;

    TextView titleTextView;
    TextView timeTextView;
    TextView descriptionTextView;
    Button trackWodButton;
    Button reservationButton;
    Button attendanceButton;
    ProgressBar reservationProgressBar;
    ProgressBar attendanceProgressBar;

    boolean isConfirmReservation;

    static int maxDescriptionWidth = -1;

	public ClassRow(Context context, RhinofitClass rfClass, ClassListener classListener, Date selectedDate) {
        super(context);
        this.context = context;
        this.rfClass = rfClass;
        this.classListener = classListener;
        this.selectedDate = selectedDate;
        init(context);
    }

	@SuppressLint("InflateParams")
	private void init(final Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_class, this);

        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        trackWodButton = (Button) view.findViewById(R.id.trackwodButton);
        reservationButton = (Button) view.findViewById(R.id.reservationButton);
        attendanceButton = (Button) view.findViewById(R.id.attendanceButton);
        reservationProgressBar = (ProgressBar) view.findViewById(R.id.reservationProgressBar);
        attendanceProgressBar = (ProgressBar) view.findViewById(R.id.attendanceProgressBar);

        trackWodButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrackWod();
            }
        });

        reservationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onReservation();
            }
        });

        attendanceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onAttendance();
            }
        });

        if ( maxDescriptionWidth <= 0 ) {
            maxDescriptionWidth = (int)(DeviceSize.getDeviceSize(context).x - ResourceSize.convertDpToPixel(context, 120));
        }
        descriptionTextView.setMaxWidth(maxDescriptionWidth);

        setClass();
    }

    private void setClass() {

        descriptionTextView.setMaxWidth(maxDescriptionWidth);

        if ( rfClass.isActionReservation() ) {
            reservationButton.setEnabled(false);
            reservationProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            reservationButton.setEnabled(true);
            reservationProgressBar.setVisibility(View.GONE);
        }

        if ( rfClass.isActionAttendance() ) {
            attendanceButton.setEnabled(false);
            trackWodButton.setEnabled(false);
            attendanceProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            attendanceButton.setEnabled(true);
            trackWodButton.setEnabled(true);
            attendanceProgressBar.setVisibility(View.GONE);
        }

        titleTextView.setText(rfClass.getTitle());
        if ( rfClass.getStartDate() != null ) {
//            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            timeTextView.setText(DateUtils.formatTime(rfClass.getStartDateString()));
        }

        if ( rfClass.getReservationId() > 0 && rfClass.getaId() > 0 ) {
            descriptionTextView.setText(Constants.kMessageIsReservationAndAttended);
            setMaxWidth(Constants.kMessageIsReservationAndAttended);
            reservationButton.setText(Constants.kButtonCancelReservation);
            attendanceButton.setText(Constants.kButtonCancelAttendance);
            descriptionTextView.setVisibility(View.VISIBLE);
            trackWodButton.setVisibility(View.VISIBLE);
        }
        else if ( rfClass.getReservationId() > 0 ) {
            descriptionTextView.setText(Constants.kMessageIsReservation);
            reservationButton.setText(Constants.kButtonCancelReservation);
            attendanceButton.setText(Constants.kButtonMakeAttendance);
            descriptionTextView.setVisibility(View.VISIBLE);
            trackWodButton.setVisibility(View.GONE);
        }
        else if ( rfClass.getaId() > 0 ) {
            descriptionTextView.setText(Constants.kMessageIsAttended);
            reservationButton.setText(Constants.kButtonMakeReservation);
            attendanceButton.setText(Constants.kButtonCancelAttendance);
            descriptionTextView.setVisibility(View.VISIBLE);
            trackWodButton.setVisibility(View.VISIBLE);
        }
        else {
            descriptionTextView.setVisibility(View.GONE);
            reservationButton.setText(Constants.kButtonMakeReservation);
            attendanceButton.setText(Constants.kButtonMakeAttendance);
            descriptionTextView.setVisibility(View.GONE);
            trackWodButton.setVisibility(View.GONE);
        }
    }

    private void setMaxWidth(String text) {
        Rect bounds = new Rect();
        Paint textPaint = descriptionTextView.getPaint();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.width();
        int max = Math.min(width, maxDescriptionWidth);
        descriptionTextView.setMaxWidth(max);
    }

    private void onReservation() {
        isConfirmReservation = true;
        rfClass.setActionReservation(true);
        if ( rfClass.getReservationId() > 0 ) {
            confirmAlert(Constants.kMessageCancelReservation);
        }
        else {
            confirmAlert(Constants.kMessageMakeReservation);
        }
    }

    private void onAttendance() {
        isConfirmReservation = false;
        rfClass.setActionAttendance(true);
        if ( rfClass.getReservationId() > 0 ) {
            confirmAlert(Constants.kMessageCancelAttendance);
        }
        else {
            confirmAlert(Constants.kMessageMarkAttendance);
        }
    }

    private void onTrackWod() {
        if ( classListener != null ) {
            classListener.onTrackWod(rfClass);
        }
    }

    private void confirmAlert(String message) {
        AlertUtil.confirmationAlert(context, "", message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ( isConfirmReservation ) {
                    reservationButton.setEnabled(false);
                    reservationProgressBar.setVisibility(View.VISIBLE);
                    if ( rfClass.getReservationId() > 0 ) {
                        cancelReservation();
                    }
                    else {
                        makeReservation();
                    }
                }
                else {
                    attendanceButton.setEnabled(false);
                    trackWodButton.setEnabled(false);
                    attendanceProgressBar.setVisibility(View.VISIBLE);
                    if ( rfClass.getaId() > 0 ) {
                        cancelAttendance();
                    }
                    else {
                        markAttendance();
                    }
                }
            }
        });
    }

    private void makeReservation() {
        WebService.makeReservation(context, rfClass.getClassId(), selectedDate, new InterfaceHttpRequest.HttpRequestJsonListener() {
            @Override
            public void complete(JSONObject result, String errorMsg) {
                rfClass.setActionReservation(false);
                if ( result != null ) {
                    int resId = JSONModel.getIntFromJson(result, Constants.kResponseKeyReservationId);
                    rfClass.setReservationId(resId);
                }
                else {
                    if ( errorMsg == null  )
                        errorMsg = "Failure make reservation request";
                    AlertUtil.messageAlert(context, "Make Reservation", errorMsg);
                }
                setClass();
            }
        }).onRun();
    }

    private void cancelReservation() {
        WebService.deleteReservation(context, rfClass.getReservationId(), new InterfaceHttpRequest.HttpRequestJsonListener() {
            @Override
            public void complete(JSONObject result, String errorMsg) {
                rfClass.setActionReservation(false);
                if ( errorMsg == null || errorMsg.isEmpty() ) {
                    rfClass.setReservationId(-1);
                }
                else {
                    if ( errorMsg == null  )
                        errorMsg = "Failure cancel reservation request";
                    AlertUtil.messageAlert(context, "Cancel Reservation", errorMsg);
                }
                setClass();
            }
        }).onRun();
    }

    private void markAttendance() {
        WebService.makeAttendance(context, rfClass.getClassId(), selectedDate, new InterfaceHttpRequest.HttpRequestJsonListener() {
            @Override
            public void complete(JSONObject result, String errorMsg) {
                rfClass.setActionAttendance(false);
                if ( result != null ) {
                    int aId = JSONModel.getIntFromJson(result, Constants.kResponseKeyAttendanceId);
                    rfClass.setaId(aId);
                }
                else {
                    if ( errorMsg == null  )
                        errorMsg = "Failure make attendance request";
                    AlertUtil.messageAlert(context, "Make Attendance", errorMsg);
                }
                setClass();
            }
        }).onRun();
    }

    private void cancelAttendance() {
        WebService.deleteAttendance(context, rfClass.getaId(), new InterfaceHttpRequest.HttpRequestJsonListener() {
            @Override
            public void complete(JSONObject result, String errorMsg) {
                rfClass.setActionAttendance(false);
                if ( errorMsg == null || errorMsg.isEmpty() ) {
                    rfClass.setaId(-1);
                }
                else {
                    if ( errorMsg == null  )
                        errorMsg = "Failure cancel attendance request";
                    AlertUtil.messageAlert(context, "Cancel Attendance", errorMsg);
                }
                setClass();
            }
        }).onRun();
    }

    public interface ClassListener {
        public void refreshRow();
        public void onTrackWod(RhinofitClass rfClass);
    }

}
