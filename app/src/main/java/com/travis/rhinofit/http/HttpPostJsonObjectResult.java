package com.travis.rhinofit.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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

public class HttpPostJsonObjectResult extends CustomAsyncHttpRequest {

	private static final String LOG_TAG = "HttpGetJsonResult";

	HttpRequestJsonListener 	requestListener;

	public HttpPostJsonObjectResult(Context context, String url, JSONObject params, HttpRequestJsonListener callback) {
		super(context, url, params);
		this.requestListener = callback;
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
            	HttpPostJsonObjectResult.this.onSuccess(LOG_TAG, statusCode, headers, response);
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
            	HttpPostJsonObjectResult.this.onFailure(LOG_TAG, statusCode, headers, errorResponse, e);
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
        Log.d(LOG_TAG, URL + "\n" + params.toString());
		return client.post(_context, URL, params, responseHandler);
	}

}
