package com.travis.rhinofit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.travis.rhinofit.customs.CustomEditText;
import com.travis.rhinofit.global.AppManager;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.JSONModel;
import com.travis.rhinofit.utils.AlertUtil;
import com.travis.rhinofit.utils.UtilsMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class LoginActivity extends Activity {

    CustomEditText          emailEditText;
    CustomEditText          passwordEditText;
    Button                  loginButton;
    Button                  linkButton;

    ProgressDialog          progressDialog;
    AppManager              appManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appManager = AppManager.getInstance(this);

        emailEditText = (CustomEditText) findViewById(R.id.emailEditText);
        passwordEditText = (CustomEditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        linkButton = (Button) findViewById(R.id.linkButton);

        emailEditText.setType(CustomEditText.EMAIL);

        setActions();

        setUI();
    }

    private void setUI()    {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int height = size.y;

        LinearLayout explain_view = (LinearLayout) findViewById(R.id.explain_view);
        LinearLayout fb_group = (LinearLayout) findViewById(R.id.fb_group);

        if (height < 1281)  {
            explain_view.setVisibility(View.GONE);
            fb_group.setVisibility(View.GONE);
            linkButton.setVisibility(View.GONE);
        } else  {
            explain_view.setVisibility(View.VISIBLE);
            fb_group.setVisibility(View.VISIBLE);
            linkButton.setVisibility(View.VISIBLE);
        }
    }

    private void setActions() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });

        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOpenBrowser();
            }
        });
    }

    private void onLogin() {
        if ( isValidInput() ) {
            if ( progressDialog == null ) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Logging in...");
            }
            progressDialog.show();
            WebService.login(this, emailEditText.getText().toString(), passwordEditText.getText().toString(), new InterfaceHttpRequest.HttpRequestJsonListener() {
                @Override
                public void complete(JSONObject result, String errorMsg) {
                    if ( result == null ) {
                        if ( progressDialog != null )
                            progressDialog.dismiss();
                        if ( errorMsg == null ) {
                            AlertUtil.errorAlert(LoginActivity.this);
                        }
                        else {
                            AlertUtil.messageAlert(LoginActivity.this, "Error", errorMsg);
                        }
                    }
                    else {
                        if ( JSONModel.isNull(result, Constants.kResponseKeyToken) ) {
                            if ( progressDialog != null )
                                progressDialog.dismiss();
                            if ( JSONModel.isNull(result, Constants.kResponseKeyError) ) {
                                AlertUtil.errorAlert(LoginActivity.this);
                            }
                            else {
                                AlertUtil.messageAlert(LoginActivity.this, "Error", JSONModel.getStringFromJson(result, Constants.kResponseKeyError));
                            }
                        }
                        else  {
                            String token = JSONModel.getStringFromJson(result, Constants.kResponseKeyToken);
                            appManager.setToken(token);
                            appManager.setLoggedIn(true);
                            try {
                                if (result.getInt("valideula") == 0)
                                    UtilsMethod.saveBooleanInSharedPreferences(LoginActivity.this, "isFirstUser", true);
                                else
                                    UtilsMethod.saveBooleanInSharedPreferences(LoginActivity.this, "isFirstUser", false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            getUserInfo();
                        }
                    }
                }
            }).onRun();
        }

        emailEditText.setFocusable(true);
        emailEditText.setFocusableInTouchMode(true);

        passwordEditText.setFocusable(true);
        passwordEditText.setFocusableInTouchMode(true);
    }

    private void getUserInfo() {
        WebService.getUserInfo(this, new InterfaceHttpRequest.HttpRequestJsonListener() {
            @Override
            public void complete(JSONObject result, String errorMsg) {
                if ( progressDialog != null )
                    progressDialog.dismiss();

                if ( result == null ) {
                    AlertUtil.errorAlert(LoginActivity.this);
                }
                else {
                    if ( !JSONModel.isNull(result, Constants.kResponseKeyError) ) {
                        AlertUtil.messageAlert(LoginActivity.this, "Error", JSONModel.getStringFromJson(result, Constants.kResponseKeyError));
                    }
                    else  {
                        appManager.setUser(result);
                        appManager.setLoggedIn(true);
                        loggedIn();
                    }
                }
            }
        }).onRun();
    }

    private void loggedIn() {

        if (UtilsMethod.getBooleanFromSharedPreferences(LoginActivity.this, "isFirstUser", false)) {
            Intent intent = new Intent(LoginActivity.this, TermsActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        finish();
    }

    private void onOpenBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://my.rhinofit.ca/findrhinofitgym"));
        startActivity(browserIntent);
    }

    private boolean isValidInput() {
        emailEditText.setFocusable(false);
        emailEditText.setFocusableInTouchMode(false);

        passwordEditText.setFocusable(false);
        passwordEditText.setFocusableInTouchMode(false);

        boolean valid1 = emailEditText.isValidInput();
        boolean valid2 = passwordEditText.isValidInput();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailEditText.getRootView().getWindowToken(), 0);

        return  valid1 && valid2;
    }

    public void onResetPassword(View view)  {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://my.rhinofit.ca/forgetpassword.php"));
        startActivity(browserIntent);
    }

    public void likeViaFB(View view)    {
        Intent intent;

        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+"100010131700972"));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/RhinoFitInc"));
        }

        startActivity(intent);
    }

    public void shareViaFB(View view)   {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, (String) view.getTag(R.string.app_name));
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "www.rhinofit.ca");

        PackageManager pm = view.getContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList)
        {
            if ((app.activityInfo.name).startsWith("com.facebook.katana"))
            {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                view.getContext().startActivity(shareIntent);
                break;
            }
        }
    }
}
