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
import com.travis.rhinofit.models.Reservation;
import com.travis.rhinofit.utils.AlertUtil;
import com.travis.rhinofit.utils.DateUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class ReservationRow extends LinearLayout {

    Context context;
    Reservation reservation;
    ReservationListener reservationListener;

    TextView titleTextView;
    TextView timeTextView;
    TextView whenTextView;
    Button reservationButton;
    ProgressBar reservationProgressBar;

    public ReservationRow(Context context, Reservation reservation, ReservationListener reservationListener) {
        super(context);
        this.context = context;
        this.reservation = reservation;
        this.reservationListener = reservationListener;
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(final Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_reservation, this);

        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        whenTextView = (TextView) view.findViewById(R.id.whenTextView);
        reservationButton = (Button) view.findViewById(R.id.reservationButton);

        reservationProgressBar = (ProgressBar) view.findViewById(R.id.reservationProgressBar);

        reservationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtil.confirmationAlert(context, "", Constants.kMessageCancelReservation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onCancelReservation();
                    }
                });
            }
        });

        setReservation();
    }

    private void setReservation() {

        if ( reservation.isActionReservation() ) {
            reservationButton.setEnabled(false);
            reservationProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            reservationButton.setEnabled(true);
            reservationProgressBar.setVisibility(View.GONE);
        }

        titleTextView.setText(reservation.getTitle());
        if ( reservation.getWhen() != null ) {
//            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            timeTextView.setText(DateUtils.formatTime(reservation.getWhenString()));
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            whenTextView.setText(sdf.format(reservation.getWhen()));
        }
    }
    
    private void onCancelReservation() {
        reservationButton.setEnabled(false);
        reservationProgressBar.setVisibility(View.VISIBLE);
        WebService.deleteReservation(context, reservation.getReservationId(), new InterfaceHttpRequest.HttpRequestJsonListener() {
            @Override
            public void complete(JSONObject result, String errorMsg) {
                reservation.setActionReservation(false);
                if (errorMsg == null || errorMsg.isEmpty()) {
                    reservation.setReservationId(-1);
                    if (reservationListener != null)
                        reservationListener.onCancelReservation(reservation);
                } else {
                    if (errorMsg == null)
                        errorMsg = "Failure cancel reservation request";
                    AlertUtil.messageAlert(context, "Cancel Reservation", errorMsg);
                }
                reservationButton.setEnabled(true);
            }
        }).onRun();
    }

    public interface ReservationListener {
        public void onCancelReservation(Reservation reservation);
    }
}
