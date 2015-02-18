package com.travis.rhinofit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.ReservationArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.Reservation;
import com.travis.rhinofit.rowlayout.ReservationRow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class MyReservationsFragment extends BaseFragment implements ReservationRow.ReservationListener {

    ListView            reservationListView;
    WaitingLayout       waitingLayout;

    ArrayList<Reservation>  reservations;
    ReservationArrayAdapter  arrayAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reservation_frame, container, false);

        reservationListView = (ListView) view.findViewById(R.id.reservationListView);
        waitingLayout = (WaitingLayout) view.findViewById(R.id.waitingLayout);
        waitingLayout.setVisibility(View.GONE);

        reservations = new ArrayList<Reservation>();
        arrayAdapter = new ReservationArrayAdapter(parentActivity, R.layout.row_reservation, reservations, this);
        reservationListView.setAdapter(arrayAdapter);

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
        getReservations();
    }

    @Override
    public void setNavTitle() {
        int count = reservations == null ? 0 : reservations.size();
        parentActivity.setNavTitle(String.format("My Reservations (%d)", count));
    }

    @Override
    public void onCancelReservation(Reservation reservation) {
        if ( arrayAdapter != null ) {
            arrayAdapter.remove(reservation);
            if (arrayAdapter.getCount() == 0)
                waitingLayout.showResult(Constants.kMessageNoReservations);
        }
        setNavTitle();
    }

    private void getReservations() {
        waitingLayout.showLoadingProgressBar();
        task = WebService.getReservations(parentActivity, new InterfaceHttpRequest.HttpRequestArrayListener() {
            @Override
            public void complete(JSONArray result, String errorMsg) {
                task = null;
                if (result == null) {
                    if (errorMsg == null)
                        errorMsg = "Failure get my reservations";
                    waitingLayout.showResult(errorMsg);
                } else {
                    if (result.length() == 0) {
                        waitingLayout.showResult(Constants.kMessageNoMyWods);
                    } else {
                        waitingLayout.setVisibility(View.GONE);
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                JSONObject jsonObject = result.getJSONObject(i);
                                Reservation reservation = new Reservation(jsonObject);
                                arrayAdapter.add(reservation);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setNavTitle();
                    }
                }
            }
        });

        task.onRun();
    }
}
