package com.travis.rhinofit.global;

import android.content.Context;

import com.travis.rhinofit.base.BaseAppManager;
import com.travis.rhinofit.models.JSONModel;
import com.travis.rhinofit.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class AppManager extends BaseAppManager {

    private static final String LOGIN = "com.travis.rhinofit.login";
    private static final String TOKEN = "com.travis.rhinofit.token";
    private static final String USER = "com.travis.rhinofit.user";

    boolean isLoggedIn = false;
    String token;

    User user;

    private static AppManager appManager = null;

    public static AppManager getInstance(Context context) {
        if ( appManager == null ) {
            appManager = new AppManager(context);
        }
        else if ( context != null )
            appManager.context = context;

        return appManager;
    }

    private AppManager(Context context1)
    {
        this.context = context1;
        initSharedInstance();
    }

    private void initSharedInstance() {
        this.isLoggedIn = getBooleanValue(LOGIN);
        this.token = getStringValue(TOKEN);
        if ( this.isLoggedIn )
            initUser();
    }

    private void initUser() {
        try {
            this.user = new User(new JSONObject(getStringValue(USER)));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
        setBooleanValue(LOGIN, this.isLoggedIn);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        setStringValue(TOKEN, token);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        setStringValue(USER, user.getJsonObject().toString());
    }

    public void setUser(JSONObject jsonObject) {
        this.user = new User(jsonObject);
        setStringValue(USER, jsonObject.toString());
    }

    public void removeUser() {
        this.user = null;
        setStringValue(USER, null);
    }
}
