package com.travis.rhinofit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.MembershipArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.Membership;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class MyMembershipsFragment extends BaseFragment {
    ListView membershipListView;
    WaitingLayout waitingLayout;

    ArrayList<Membership> memberships;
    MembershipArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mymembership_frame, container, false);

        membershipListView = (ListView) view.findViewById(R.id.membershipListView);
        waitingLayout = (WaitingLayout) view.findViewById(R.id.waitingLayout);
        waitingLayout.setVisibility(View.GONE);

        memberships = new ArrayList<Membership>();
        arrayAdapter = new MembershipArrayAdapter(parentActivity, R.layout.row_mymembership, memberships);
        membershipListView.setAdapter(arrayAdapter);

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
        getMemberships();
    }

    @Override
    public void setNavTitle() {
        parentActivity.setNavTitle("My Memberships");
    }

    private void getMemberships() {
        waitingLayout.showLoadingProgressBar();
        task = WebService.getMemberships(parentActivity, new InterfaceHttpRequest.HttpRequestArrayListener() {
            @Override
            public void complete(JSONArray result, String errorMsg) {
                task = null;
                if (result == null) {
                    if (errorMsg == null)
                        errorMsg = "Failure get my memberships";
                    waitingLayout.showResult(errorMsg);
                } else {
                    if (result.length() == 0) {
                        waitingLayout.showResult(Constants.kMessageNoMyMemberships);
                    } else {
                        waitingLayout.setVisibility(View.GONE);
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                JSONObject jsonObject = result.getJSONObject(i);
                                Membership reservation = new Membership(jsonObject);
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
