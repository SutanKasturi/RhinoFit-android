package com.travis.rhinofit.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.listener.InterfaceHttpRequest.HttpRequestArrayListener;
import com.travis.rhinofit.models.JSONModel;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpPostJsonArrayResult extends CustomAsyncHttpRequest {

	private static final String LOG_TAG = "HttpPostJsonArrayResult";

	HttpRequestArrayListener 	requestListener;

	public HttpPostJsonArrayResult(Context context, String url, JSONObject params, HttpRequestArrayListener callback) {
		super(context, url, params);
		this.requestListener = callback;
	}

	public void setInterface(HttpRequestArrayListener callback) {
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
            	HttpPostJsonArrayResult.this.onSuccess(LOG_TAG, statusCode, headers, response);
            	if ( requestListener != null ) {
            		String json = new String(response);
                    try {
                        JSONObject result = new JSONObject(json);
                        String errMessage = JSONModel.getStringFromJson(result, Constants.kResponseKeyError);
                        if ( errMessage != null && !errMessage.isEmpty() ) {
                            requestListener.complete(null, errMessage);
                            return;
                        }
                    }
                    catch (JSONException e) {
                    }

                    try {
						JSONArray result = new JSONArray(json);
						requestListener.complete(result, null);
					} catch (JSONException e) {
						e.printStackTrace();
						requestListener.complete(null, e.getLocalizedMessage());
					}
            	}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            	HttpPostJsonArrayResult.this.onFailure(LOG_TAG, statusCode, headers, errorResponse, e);
            	if ( requestListener != null ) {
                    try {
                        String errMessage = new String(errorResponse);
                        requestListener.complete(null, errMessage);
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
