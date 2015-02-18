package com.travis.rhinofit;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.base.BaseFragmentActivity;
import com.travis.rhinofit.fragments.MenuFragment;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Stack;

public class MainActivity extends BaseFragmentActivity {

    public static FragmentManager fragmentManager;
    BaseFragment mContent;
    MenuFragment menuFragment;

    // This is needed in order to manage the fragments.
    public static Stack<String> _mFragmentStack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();

        // set the Above View
        if (savedInstanceState != null)
            mContent = (BaseFragment)fragmentManager.getFragment(savedInstanceState, "mContent");
        if (mContent == null) {
            _mFragmentStack = new Stack<String>();
            mContent = MenuFragment.getFragment();
        }

        // set the Above View
        setContentView(R.layout.activity_main);
        addFragment(mContent);

        menuFragment = new MenuFragment();
        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, menuFragment)
                .commit();
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentManager.putFragment(outState, "mContent", mContent);
    }

    @Override
    public void onNavButtonClicked() {
        if ( _mFragmentStack.size() > 1 ) {
            onBackPressed();
        }
        else {
            toggle();
        }
    }

    public void switchContent(BaseFragment fragment) {
        // remove the current fragment from the stack.
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment f;
        while ( _mFragmentStack.size() > 0 &&  (f = fragmentManager.findFragmentByTag(_mFragmentStack.pop())) != null ) {
            transaction.remove(f);
        }
        transaction.commit();

        addFragment(fragment);

        getSlidingMenu().showContent();
    }

    public void switchContentWithAnimate(BaseFragment fragment) {
        String tag = fragment.toString();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        Fragment currentFragment = fragmentManager.findFragmentByTag(_mFragmentStack.peek());
        transaction.hide(currentFragment);

        transaction.add(R.id.content_frame, fragment, tag);
        transaction.addToBackStack(tag);

        mContent = fragment;

        Fragment f;
        while ( _mFragmentStack.size() > 0 && (f = fragmentManager.findFragmentByTag(_mFragmentStack.pop())) != null ) {
            transaction.remove(f);
        }
        transaction.commit();

        onSetNavButton(true);
        _mFragmentStack.add(tag);

        getSlidingMenu().showContent();
    }

    private void addFragment(BaseFragment fragment) {
        String tag = fragment.toString();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, fragment, tag);
        transaction.addToBackStack(tag);
        _mFragmentStack.add(tag);
        transaction.commit();

        onSetNavButton(true);

        mContent = fragment;
    }

    public void changeFragment(BaseFragment fragment) {

        String tag = fragment.toString();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        Fragment currentFragment = fragmentManager.findFragmentByTag(_mFragmentStack.peek());
        transaction.hide(currentFragment);

        transaction.add(R.id.content_frame, fragment, tag);
        transaction.addToBackStack(tag);
        _mFragmentStack.add(tag);

        transaction.commit();

        onSetNavButton(false);

        mContent = fragment;
    }

    @Override
    public void onBackPressed(){
        if (_mFragmentStack.size() > 1 ){
            // Remove the fragment
            removeFragment();
        }
        mContent.setNavTitle();
    }
    private void removeFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.findFragmentByTag(_mFragmentStack.pop());
        Fragment fragment = fragmentManager.findFragmentByTag(_mFragmentStack.peek());
        transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        transaction.remove(currentFragment);
        transaction.show(fragment);
        transaction.commit();

        mContent = (BaseFragment)fragment;

        if ( _mFragmentStack.size() == 1 ) {
            onSetNavButton(true);
        }
    }
}
