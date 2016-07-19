package com.travis.rhinofit.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.WallArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.AppManager;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.interfaces.WallCellButtonDelegate;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.Wall;
import com.travis.rhinofit.utils.AlertUtil;
import com.travis.rhinofit.utils.UtilsValues;

import org.apache.http.Header;
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

    AppManager appManager;

    WallCellButtonDelegate buttonDelegate = new WallCellButtonDelegate() {
        @Override
        public void onFlagClicked(int index) {
            flagTapped(index);
        }

        @Override
        public void onDelteClicked(int index) {
            removeTapped(index);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wall_frame, container, false);

        wallListView = (ListView) view.findViewById(R.id.wallListView);
        addWallPostButton = (Button) view.findViewById(R.id.addWallPostButton);
        waitingLayout = (WaitingLayout) view.findViewById(R.id.waitingLayout);
        waitingLayout.setVisibility(View.GONE);

        appManager = AppManager.getInstance(getActivity());

        UtilsValues.messageHandler = new Handler() {

            public void handleMessage(Message msg) {

                if (msg.what == 2) {
                    init();
                }
            }
        };

        walls = new ArrayList<Wall>();
        arrayAdapter = new WallArrayAdapter(parentActivity, R.layout.row_wall_left, walls, buttonDelegate);
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
        getWalls();
    }

    @Override
    public void setNavTitle() {
        parentActivity.setNavTitle("The Wall");
    }

    private void getWalls() {
        if ( arrayAdapter != null ) {
            arrayAdapter.clear();
            wallListView.setAdapter(null);
        }

        if (walls.size() > 0)
            walls = new ArrayList<>();
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
                                walls.add(wall);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    arrayAdapter = new WallArrayAdapter(parentActivity, R.layout.row_wall_left, walls, buttonDelegate);
                    wallListView.setAdapter(arrayAdapter);
                    setNavTitle();

                    scrollMyListViewToBottom();
                }
            }
        });

        task.onRun();
    }

    private void scrollMyListViewToBottom() {
        wallListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                wallListView.setSelection(arrayAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void didPostWallMessageWithImage() {
        init();
    }

    private void flagTapped(int index)  {
        final Wall wall = walls.get(index);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("Are you sure want to report this user for inappropriate content?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        reportWallPost(wall);
                    }
                });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void reportWallPost(Wall wall) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put(Constants.kParamAction, Constants.kRequestReportPost);
        params.put(Constants.kParamToken, appManager.getToken());
        params.put(Constants.kParamWallID, wall.getWallId());

        client.post(getActivity(), "https://my.rhinofit.ca/api.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                AlertUtil.errorAlert(getActivity());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String msg = "";
                try {
                    JSONObject response = new JSONObject(responseString);
                    msg = response.getString("error");
                    if (msg.length() > 0)
                        AlertUtil.messageAlert(getActivity(), "error", msg);
                    else
                        getWalls();

                } catch (JSONException e) {
                    e.printStackTrace();
                    getWalls();
                }
            }
        });
    }

    private void removeTapped(int index) {
        final Wall wall = walls.get(index);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("Are you sure want to Delete this post?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        removeWall(wall);
                    }
                });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void removeWall(Wall wall)   {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put(Constants.kParamAction, Constants.kRequestDeletePost);
        params.put(Constants.kParamToken, appManager.getToken());
        params.put(Constants.kParamWallID, wall.getWallId());

        client.post(getActivity(), "https://my.rhinofit.ca/api.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                AlertUtil.errorAlert(getActivity());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String msg = "";
                try {
                    JSONObject response = new JSONObject(responseString);
                    msg = response.getString("error");
                    if (msg.length() > 0)
                        AlertUtil.messageAlert(getActivity(), "error", msg);
                    else
                        getWalls();

                } catch (JSONException e) {
                    e.printStackTrace();
                    getWalls();
                }
            }
        });
    }
}
