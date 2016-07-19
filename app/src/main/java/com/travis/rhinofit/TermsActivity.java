package com.travis.rhinofit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.travis.rhinofit.global.AppManager;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.utils.AlertUtil;
import com.travis.rhinofit.utils.UtilsMethod;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class TermsActivity extends Activity {

    ProgressDialog progressDialog;
    ImageView checkBox;
    WebView termsContent;

    AppManager appManager;

    boolean isSelected = false;

    String versionID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        appManager = AppManager.getInstance(this);

        checkBox = (ImageView) findViewById(R.id.checkbox_terms);
        termsContent = (WebView) findViewById(R.id.termsContent);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckBox();
            }
        });

        loadEula();
    }

    private void loadEula() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Getting Eula...");
        }

        progressDialog.show();

        RequestParams params = new RequestParams();
        params.put(Constants.kParamAction, Constants.kRequestEula);
        params.put(Constants.kParamToken, appManager.getToken());

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this, "https://my.rhinofit.ca/api.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                AlertUtil.errorAlert(TermsActivity.this);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(responseString);

                    if (object.getString("success").equals("1"))
                    {
                        versionID = object.getString("versionid");
                        final String html = object.getString("html");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                loadTerms(html);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadTerms(String html)    {
        termsContent.loadData(html, "text/html; charset=UTF-8", null);
    }

    public void tapSubmit(View view)    {

        if (!isSelected)
            return;
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Getting Eula...");
        }

        progressDialog.show();

        RequestParams params = new RequestParams();
        params.put(Constants.kParamAction, Constants.kAcceptEula);
        params.put(Constants.kParamToken, appManager.getToken());
        params.put(Constants.kParamVersionID, versionID);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this, "https://my.rhinofit.ca/api.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                AlertUtil.errorAlert(TermsActivity.this);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                progressDialog.dismiss();
                UtilsMethod.saveBooleanInSharedPreferences(TermsActivity.this, "isFirstUser", true);

                startActivity(new Intent(TermsActivity.this, MainActivity.class));
            }
        });
    }

    private void setCheckBox()  {
        if (isSelected)
            checkBox.setImageResource(R.drawable.check_box_false);
        else
            checkBox.setImageResource(R.drawable.check_box_true);

        isSelected = !isSelected;
    }
}
