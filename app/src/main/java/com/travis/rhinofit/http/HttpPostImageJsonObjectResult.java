package com.travis.rhinofit.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.listener.InterfaceHttpRequest.HttpRequestJsonListener;
import com.travis.rhinofit.models.JSONModel;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpPostImageJsonObjectResult extends CustomAsyncHttpRequest {

	private static final String LOG_TAG = "HttpGetJsonResult";

	HttpRequestJsonListener 	requestListener;

    String paramString;
    Bitmap image;
    String filePath;

	public HttpPostImageJsonObjectResult(Context context, String url, JSONObject params, String paramString, Bitmap image, String filePath, HttpRequestJsonListener callback) {
		super(context, url, params);
		this.requestListener = callback;
        this.paramString = paramString;
        this.image = image;
        this.filePath = filePath;
	}

	public void setInterface(HttpRequestJsonListener callback) {
		requestListener = callback;
	}
	@Override
	public ResponseHandlerInterface getResponseHandler() {
		return new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            	HttpPostImageJsonObjectResult.this.onSuccess(LOG_TAG, statusCode, headers, response);
            	if ( requestListener != null ) {
            		String json = new String(response);
					try {
						JSONObject result = new JSONObject(json);
                        String errMessage = JSONModel.getStringFromJson(result, Constants.kResponseKeyError);
                        if ( errMessage != null && !errMessage.isEmpty() )
                            requestListener.complete(null, errMessage);
                        else
                            requestListener.complete(result, null);
					} catch (JSONException e) {
						e.printStackTrace();
						requestListener.complete(null, e.getLocalizedMessage());
					}
            	}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            	HttpPostImageJsonObjectResult.this.onFailure(LOG_TAG, statusCode, headers, errorResponse, e);
                if ( requestListener != null ) {
                    try {
                        String errMessage = new String(errorResponse);
                        requestListener.complete(null, new String(errorResponse));
                    }
                    catch (NullPointerException ne) {
                        ne.printStackTrace();
                        requestListener.complete(null, null);
                    }
                }
            }

            @Override
            public void onRetry(int retryNo) {
                Toast.makeText(_context,
                        String.format("Request is retried, retry no. %d", retryNo),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        };
	}

	@Override
	public RequestHandle executeSample(AsyncHttpClient client, String URL,
			Header[] headers, HttpEntity entity,
			ResponseHandlerInterface responseHandler) {
		RequestParams params = getRequestParams();
        if ( paramString != null && filePath != null ) {
            Date now = new Date();
            try {
                File file = new File(filePath);
                params.put(paramString, file, now.getTime() + ".jpg");
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Log.d(LOG_TAG, URL + "\n" + params.toString());
		return client.post(_context, URL, params, responseHandler);
	}


}
