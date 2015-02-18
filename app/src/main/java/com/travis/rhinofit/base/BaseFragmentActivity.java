package com.travis.rhinofit.base;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.travis.rhinofit.R;
import com.travis.rhinofit.customs.CustomImageButton;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Sutan Kasturi on 1/27/2015.
 */
public abstract class BaseFragmentActivity extends SlidingFragmentActivity {

    public TextView navTitleTextView;
    public CustomImageButton navButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AsyncHttpClient.allowRetryExceptionClass(IOException.class);
        AsyncHttpClient.allowRetryExceptionClass(SocketTimeoutException.class);
        AsyncHttpClient.allowRetryExceptionClass(ConnectTimeoutException.class);

        // The following exceptions will be blacklisted, i.e.: When an exception
        // of this type is raised, the request will not be retried and it will
        // fail immediately.
        AsyncHttpClient.blockRetryExceptionClass(UnknownHostException.class);
        AsyncHttpClient.blockRetryExceptionClass(ConnectionPoolTimeoutException.class);

        HttpURLConnection.setFollowRedirects(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getActionBar().setTitle("");

        setCustomActionBar();
        setSlidingMenu();
    }

    private void setCustomActionBar() {
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.navbar, null);
        navButton = (CustomImageButton)mCustomView.findViewById(R.id.menuButton);
        navTitleTextView = (TextView) mCustomView.findViewById(R.id.navTitleTextView);

        navButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onNavButtonClicked();
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    private void setSlidingMenu() {
        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
//		sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.1f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    }

    public void onSetNavButton(boolean isMenu) {
        if ( isMenu ) {
            navButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_menu));
        }
        else {
            navButton.setImageDrawable(getResources().getDrawable(R.drawable.back_icon));
        }
    }
    public void onNavButtonClicked() {
        toggle();
    }
    public void setNavTitle(String title) {
        navTitleTextView.setText(title);
    }
}
