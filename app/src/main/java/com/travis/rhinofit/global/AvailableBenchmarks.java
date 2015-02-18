package com.travis.rhinofit.global;

import android.content.Context;
import android.view.View;

import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.AvailableBenchmark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/15/15.
 */
public class AvailableBenchmarks {
    private static ArrayList<AvailableBenchmark> availableBenchmarks;
    public static void getAvailableBenchmarks(Context context, final AvailableBenchmarksListener callback) {
        if ( availableBenchmarks != null )
        {
            if ( callback != null )
                callback.didLoadAvailableBenchmarks(availableBenchmarks, null);
        }
        else {
            WebService.getAvailableBenchmark(context, new InterfaceHttpRequest.HttpRequestArrayListener() {
                @Override
                public void complete(JSONArray result, String errorMsg) {
                    if (result == null) {
                        if (errorMsg == null)
                            errorMsg = "Failure get available benchmarks";
                    } else {
                        if (result.length() == 0) {
                            errorMsg = Constants.kMessageNoAvailableBenchmarks;
                        } else {

                        }
                    }

                    availableBenchmarks = new ArrayList<AvailableBenchmark>();
                    if ( errorMsg == null ) {
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                JSONObject jsonObject = result.getJSONObject(i);
                                AvailableBenchmark availableBenchmark1 = new AvailableBenchmark(jsonObject);
                                availableBenchmarks.add(availableBenchmark1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if ( callback != null ) {
                        callback.didLoadAvailableBenchmarks(availableBenchmarks, errorMsg);
                    }
                }
            }).onRun();
        }
    }

    public interface AvailableBenchmarksListener {
        public void didLoadAvailableBenchmarks(ArrayList<AvailableBenchmark> availableBenchmarks1, String errorMsg);
    }
}
