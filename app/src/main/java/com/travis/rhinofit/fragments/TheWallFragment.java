package com.travis.rhinofit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.WallArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.Wall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class TheWallFragment extends BaseFragment implements PostWallMessageFragment.PostWallMessageListener {
    ListView wallListView;
    Button addWallPostButton;
    WaitingLayout waitingLayout;

    ArrayList<Wall> walls;
    WallArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wall_frame, container, false);

        wallListView = (ListView) view.findViewById(R.id.wallListView);
        addWallPostButton = (Button) view.findViewById(R.id.addWallPostButton);
        waitingLayout = (WaitingLayout) view.findViewById(R.id.waitingLayout);
        waitingLayout.setVisibility(View.GONE);

        walls = new ArrayList<Wall>();
        arrayAdapter = new WallArrayAdapter(parentActivity, R.layout.row_wall_left, walls);
        wallListView.setAdapter(arrayAdapter);

        addWallPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.changeFragment(PostWallMessageFragment.newInstance(TheWallFragment.this));
            }
        });
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
        if ( arrayAdapter != null )
            arrayAdapter.clear();
        getWalls();
    }

    @Override
    public void setNavTitle() {
        parentActivity.setNavTitle("The Wall");
    }

    private void getWalls() {
        waitingLayout.showLoadingProgressBar();
        task = WebService.getWalls(parentActivity, new InterfaceHttpRequest.HttpRequestArrayListener() {
            @Override
            public void complete(JSONArray result, String errorMsg) {
                task = null;
                if (result == null) {
                    if (errorMsg == null)
                        errorMsg = "Failure get my walls";
                    waitingLayout.showResult(errorMsg);
                } else {
                    if (result.length() == 0) {
                        waitingLayout.showResult(Constants.kMessageNoWalls);
                    } else {
                        waitingLayout.setVisibility(View.GONE);
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                JSONObject jsonObject = result.getJSONObject(i);
                                Wall wall = new Wall(jsonObject);
                                arrayAdapter.add(wall);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    setNavTitle();
                }
            }
        });

        task.onRun();
    }

    @Override
    public void didPostWallMessageWithImage() {
        init();
    }
}
