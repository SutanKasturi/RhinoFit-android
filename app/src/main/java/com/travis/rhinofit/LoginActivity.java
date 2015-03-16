package com.travis.rhinofit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.travis.rhinofit.customs.CustomEditText;
import com.travis.rhinofit.global.AppManager;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.JSONModel;
import com.travis.rhinofit.utils.AlertUtil;

import org.json.JSONObject;

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
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

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
}
