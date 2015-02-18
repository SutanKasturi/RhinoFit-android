package com.travis.rhinofit.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.listener.InterfaceHttpRequest.HttpRequestSuccessListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.travis.rhinofit.models.JSONModel;

public class HttpPostSuccessResult extends CustomAsyncHttpRequest {

	private static final String LOG_TAG = "HttpPostSuccessResult";
	
	HttpRequestSuccessListener 	requestListener;
	
	public HttpPostSuccessResult(Context context, String url, JSONObject params, HttpRequestSuccessListener callback) {
		super(context, url, params);
		this.requestListener = callback;
	}

	public void setInterface(HttpRequestSuccessListener callback) {
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
            	HttpPostSuccessResult.this.onSuccess(LOG_TAG, statusCode, headers, response);
            	if ( requestListener != null ) {
                    String json = new String(response);
                    try {
                        JSONObject result = new JSONObject(json);
                        String errMessage = JSONModel.getStringFromJson(result, Constants.kResponseKeyError);
                        if ( errMessage != null && !errMessage.isEmpty() )
                            requestListener.complete(false);
                        else
                            requestListener.complete(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        requestListener.complete(false);
                    }
            	}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            	HttpPostSuccessResult.this.onFailure(LOG_TAG, statusCode, headers, errorResponse, e);
            	if ( requestListener != null )
            		requestListener.complete(false);
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
		return client.post(_context, URL, params, responseHandler);	
	}

}
