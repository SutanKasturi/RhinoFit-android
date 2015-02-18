package com.travis.rhinofit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.travis.rhinofit.global.AppManager;


/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class Splash extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final AppManager appManager = AppManager.getInstance(getApplicationContext());

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if ( appManager.isLoggedIn() ) {
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(Splash.this, LoginActivity.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
