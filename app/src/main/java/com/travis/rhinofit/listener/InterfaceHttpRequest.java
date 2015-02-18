package com.travis.rhinofit.listener;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public interface InterfaceHttpRequest {
	public interface HttpRequestListener {
		public void complete(ArrayList<?> resultArray);
	}
	
	public interface HttpRequestJsonListener {
		public void complete(JSONObject result, String errorMsg);
	}
	public interface HttpRequestArrayListener {
		public void complete(JSONArray result, String errorMsg);
	}
	
	public interface HttpRequestSuccessListener {
		public void complete(boolean isSuccess);
	}
}
