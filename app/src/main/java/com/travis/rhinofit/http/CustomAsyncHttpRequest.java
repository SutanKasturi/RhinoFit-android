package com.travis.rhinofit.http;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.travis.rhinofit.global.AppManager;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.global.HttpConstants;
import com.travis.rhinofit.listener.InterfaceAsyncHttpRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public abstract class CustomAsyncHttpRequest implements InterfaceAsyncHttpRequest {

	public AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	private final List<RequestHandle> requestHandles = new LinkedList<RequestHandle>();
	private String _headers, _body;
	
	protected Context _context;
	
	protected boolean isRunning = false; 
	
	protected boolean isDebuggable = true;
	
	protected JSONObject paramObject;
	
	protected String requestUrl;

    AppManager  appManager;
	
	public CustomAsyncHttpRequest(Context context, String requestUrl) {
		_context = context;
		this.requestUrl = requestUrl;
		paramObject = null;
        init();
	}
	
	public CustomAsyncHttpRequest(Context context, String reuqestUrl, JSONObject object) {
		_context = context;
		this.requestUrl = reuqestUrl;
		this.paramObject = object;
        init();
	}

    private void init() {
        appManager = AppManager.getInstance(_context);

        // use our own, SNI-capable LayeredSocketFactory for https://
        SchemeRegistry schemeRegistry = asyncHttpClient.getHttpClient().getConnectionManager().getSchemeRegistry();
        schemeRegistry.register(new Scheme("https", new TlsSniSocketFactory(), 443));
    }
	
	public List<RequestHandle> getRequestHandles() {
        return requestHandles;
    }

    @Override
    public void addRequestHandle(RequestHandle handle) {
        if (null != handle) {
            requestHandles.add(handle);
        }
    }

    public void onRun() {
    	ConnectionDetector cd = new ConnectionDetector(_context);
		if ( cd.isConnectingToInternet() ) {
			isRunning = true;
			RequestHandle handle = executeSample(getAsyncHttpClient(),
	                HttpConstants.HOST_URL + getDefaultURL(),
	                getRequestHeaders(),
	                getRequestEntity(),
	                getResponseHandler());
			if ( handle != null ) {
				addRequestHandle(handle);
			}
		}
    }
    
    @Override
	public String getDefaultURL() {
		return this.requestUrl;
	}

	@Override
	public void getDefaultValue() {

	}

	@Override
	public boolean isRequestHeadersAllowed() {
		return true;
	}

	@Override
	public boolean isRequestBodyAllowed() {
		return true;
	}

    public void onCancel() {
        asyncHttpClient.cancelRequests(_context, true);
    }

    public List<Header> getRequestHeadersList() {
        List<Header> headers = new ArrayList<Header>();
        String headersRaw = _headers == null ? null : _headers;

        if (headersRaw != null && headersRaw.length() > 3) {
            String[] lines = headersRaw.split("\\r?\\n");
            for (String line : lines) {
                try {
                    int equalSignPos = line.indexOf('=');
                    if (1 > equalSignPos) {
                        throw new IllegalArgumentException("Wrong header format, may be 'Key=Value' only");
                    }

                    String headerName = line.substring(0, equalSignPos).trim();
                    String headerValue = line.substring(1 + equalSignPos).trim();

                    headers.add(new BasicHeader(headerName, headerValue));
                } catch (Throwable t) {
                    Log.e("SampleParentActivity", "Not a valid header line: " + line, t);
                }
            }
        }
        return headers;
    }

    public Header[] getRequestHeaders() {
        List<Header> headers = getRequestHeadersList();
//        headers.add(new BasicHeader("Content-Type", "application/json"));
//        headers.add(new BasicHeader("Accept", "application/json"));
        return headers.toArray(new Header[headers.size()]);
    }

    public HttpEntity getRequestEntity() {
        if (isRequestBodyAllowed() && _body != null) {
            try {
                return new StringEntity(_body);
            } catch (UnsupportedEncodingException e) {
                Log.e("SampleParentActivity", "cannot create String entity", e);
            }
        }
        return null;
    }

    protected final void debugHeaders(String TAG, Header[] headers) {
        if (headers != null) {
            Log.d(TAG, "Return Headers:");
            StringBuilder builder = new StringBuilder();
            for (Header h : headers) {
                String _h = String.format(Locale.US, "%s : %s", h.getName(), h.getValue());
                Log.d(TAG, _h);
                builder.append(_h);
                builder.append("\n");
            }
        }
    }

    protected static String throwableToString(Throwable t) {
        if (t == null)
            return null;

        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    protected final void debugThrowable(String TAG, Throwable t) {
        if (t != null) {
            Log.e(TAG, "AsyncHttpClient returned error", t);
            Log.e(TAG, throwableToString(t));
        }
    }

    protected final void debugResponse(String TAG, String response) {
        if (response != null) {
            Log.d(TAG, "Response data:");
            Log.d(TAG, response);
        }
    }

    protected final void debugStatusCode(String TAG, int statusCode) {
        String msg = String.format(Locale.US, "Return Status Code: %d", statusCode);
        Log.d(TAG, msg);
    }

    public static int getContrastColor(int color) {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }

    public boolean isCancelButtonAllowed() {
        return false;
    }

    public AsyncHttpClient getAsyncHttpClient() {
        return this.asyncHttpClient;
    }

    @Override
    public void setAsyncHttpClient(AsyncHttpClient client) {
        this.asyncHttpClient = client;
    }
    
    public Drawable getLocalImage(String filename) {
		if ( filename != "" ) {
			File f = new File(filename);
		    if(f.exists())
		    { 
		    	return Drawable.createFromPath(filename);
		    }
		}
		return null;
	}
    
    public void onSuccess(String logTag, int statusCode, Header[] headers, byte[] response) {
    	isRunning = false;
    	
    	if ( isDebuggable == true ) {
    		String json = new String(response);
    		Log.d(logTag, "\n\n\n");
    		Log.d(logTag, "/***************************************************************/");
    		Log.d(logTag, requestUrl);
    		Log.d(logTag, "/***************************************************************/");
            debugHeaders(logTag, headers);
            debugStatusCode(logTag, statusCode);
            debugResponse(logTag, json);
            Log.d(logTag, "\n\n\n");
    	}
    }
    
    public void onFailure(String logTag, int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
    	isRunning = false;
    	if ( isDebuggable == true ) {
    		Log.d(logTag, "\n\n\n");
    		Log.d(logTag, "/***************************************************************/");
    		Log.d(logTag, requestUrl);
    		Log.d(logTag, "/***************************************************************/");
	    	
	        debugHeaders(logTag, headers);
	        debugStatusCode(logTag, statusCode);
	        debugThrowable(logTag, e);
	        if (errorResponse != null) {
	            debugResponse(logTag, new String(errorResponse));
	        }
	        Log.d(logTag, "\n\n\n");
    	}
    }
    
    public RequestParams authorizationParams() {
    	RequestParams params = new RequestParams();
        if ( appManager.isLoggedIn() ) {
            params.put(Constants.kParamToken, appManager.getToken());
        }
    	return params;
    }
    
    public RequestParams getRequestParams() {
    	RequestParams params = authorizationParams();
		
		if (params != null && paramObject != null ) {
			Iterator<String> iter = paramObject.keys();
			while ( iter.hasNext() ) {
				String key = iter.next();
				try {
					Object value = paramObject.get(key);
					params.put(key, value);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		if ( params == null )
			params = new RequestParams();
		return params;
    }
}
