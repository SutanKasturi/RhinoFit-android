package com.travis.rhinofit.listener;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

public interface InterfaceAsyncHttpRequest {

    List<RequestHandle> getRequestHandles();

    void addRequestHandle(RequestHandle handle);

    Header[] getRequestHeaders();

    HttpEntity getRequestEntity();

    AsyncHttpClient getAsyncHttpClient();

    void setAsyncHttpClient(AsyncHttpClient client);

    ResponseHandlerInterface getResponseHandler();

    String getDefaultURL();
    
    void getDefaultValue();

    boolean isRequestHeadersAllowed();

    boolean isRequestBodyAllowed();

    boolean isCancelButtonAllowed();

    RequestHandle executeSample(AsyncHttpClient client, String URL, Header[] headers, HttpEntity entity, ResponseHandlerInterface responseHandler);
}