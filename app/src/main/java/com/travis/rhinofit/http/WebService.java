package com.travis.rhinofit.http;

import android.content.Context;
import android.graphics.Bitmap;

import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.listener.InterfaceHttpRequest.HttpRequestArrayListener;
import com.travis.rhinofit.listener.InterfaceHttpRequest.HttpRequestJsonListener;
import com.travis.rhinofit.listener.InterfaceHttpRequest.HttpRequestSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/5/2015.
 */
public class WebService {

    // Login
    public static CustomAsyncHttpRequest login(Context context, String email, String password, final HttpRequestJsonListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestLogin);
            params.put(Constants.kParamEmail, email);
            params.put(Constants.kParamPassword, password);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonObjectResult(context, "", params, callback);
    }

    // Get UserInfo
    public static CustomAsyncHttpRequest getUserInfo(Context context, final HttpRequestJsonListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestGetUserInfo);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonObjectResult(context, "", params, callback);
    }


    // Classes
    public static CustomAsyncHttpRequest getClasses(Context context, String startDate, String endDate, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestGetClasses);
            params.put(Constants.kParamStartDate, startDate);
            params.put(Constants.kParamEndDate, endDate);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest makeReservation(Context context, String classId, Date reservationDate, final HttpRequestJsonListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestMakeReservation);
            params.put(Constants.kParamClassTimeId, classId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            params.put(Constants.kParamDate, sdf.format(reservationDate));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonObjectResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest getReservations(Context context, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestListReservations);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest deleteReservation(Context context, int resId, final HttpRequestJsonListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestDeleteReservation);
            params.put(Constants.kParamResId, resId);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonObjectResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest makeAttendance(Context context, String classId, Date attendanceDate, final HttpRequestJsonListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestMakeAttendance);
            params.put(Constants.kParamClassTimeId, classId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            params.put(Constants.kParamDate, sdf.format(attendanceDate));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonObjectResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest deleteAttendance(Context context, int attendanceId, final HttpRequestJsonListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestDeleteAttendance);
            params.put(Constants.kParamAId, attendanceId);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonObjectResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest getAttendances(Context context, String startDate, String endDate, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestGetAttendance);
            params.put(Constants.kParamStartDate, startDate);
            params.put(Constants.kParamEndDate, endDate);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest getWodInfo(Context context, String classId, Date startDate, final HttpRequestJsonListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestGetWodInfo);
            params.put(Constants.kParamId, classId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            params.put(Constants.kParamStart, sdf.format(startDate));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonObjectResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest trackWod(Context context, String classId, Date startDate, String wod, String title, String result, final HttpRequestJsonListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestTrackWod);
            params.put(Constants.kParamId, classId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            params.put(Constants.kParamStart, sdf.format(startDate));
            params.put(Constants.kParamWod, wod);
            params.put(Constants.kParamTitle, title);
            params.put(Constants.kParamResults, result);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonObjectResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest getMyWOD(Context context, String startDate, String endDate, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestGetMyWods);
            params.put(Constants.kParamStartDate, startDate);
            params.put(Constants.kParamEndDate, endDate);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest getMyBenchmarks(Context context, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestMyBenchmarks);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest getBenchmarkHistories(Context context, int benchmarkId, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestMyBenchmarkData);
            params.put(Constants.kParamId, benchmarkId);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest getAvailableBenchmark(Context context, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestAvailableBenchmarks);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest deleteBenchmark(Context context, int benchmarkId, Date date, String value, int dataId, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestDeleteBenchmarkData);
            params.put(Constants.kParamId, benchmarkId);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy'T'HH:mm:ss'Z'");
            params.put(Constants.kParamDate, sdf.format(date));
            params.put(Constants.kParamValue, value);
            params.put(Constants.kParamDataId, dataId);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest addBenchmark(Context context, int benchmarkId, Date date, String value, int dataId, final HttpRequestJsonListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestNewBenchmark);
            params.put(Constants.kParamId, benchmarkId);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy'T'HH:mm:ss'Z'");
            params.put(Constants.kParamDate, sdf.format(date));
            params.put(Constants.kParamValue, value);
            if ( dataId > 0 )
                params.put(Constants.kParamDataId, dataId);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonObjectResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest getMemberships(Context context, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestMyMemberships);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest getWalls(Context context, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestGetWallPosts);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest addWallPostWithImage(Context context, Bitmap image, String filePath, String message, final HttpRequestJsonListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestAddWallPost);
            params.put(Constants.kParamMSG, message);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostImageJsonObjectResult(context, "", params, Constants.kParamFile, image, filePath, callback);
    }

    public static CustomAsyncHttpRequest getCountries(Context context, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestGetCountries);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest getStates(Context context, String country, final HttpRequestArrayListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestGetStates);
            params.put(Constants.kParamCountry, country);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostJsonArrayResult(context, "", params, callback);
    }

    public static CustomAsyncHttpRequest updateUserInfo(Context context,
                                                        String paramString,
                                                        Bitmap image,
                                                        String filePath,
                                                        String firstName,
                                                        String lastName,
                                                        String address1,
                                                        String address2,
                                                        String city,
                                                        String state,
                                                        String postal,
                                                        String country,
                                                        String phone1,
                                                        String phone2,
                                                        String email,
                                                        final HttpRequestJsonListener callback) {
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.kParamAction, Constants.kRequestUpdateUserInfo);
            params.put(Constants.kParamUserFirstName, firstName);
            params.put(Constants.kParamUserLastName, lastName);
            params.put(Constants.kParamUserAddress1, address1);
            params.put(Constants.kParamUserAddress2, address2);
            params.put(Constants.kParamUserCity, city);
            params.put(Constants.kParamUserState, state);
            params.put(Constants.kParamUserZip, postal);
            params.put(Constants.kParamUserCountry, country);
            params.put(Constants.kParamUserPhone1, phone1);
            params.put(Constants.kParamUserPhone2, phone2);
            params.put(Constants.kParamUserName, email);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new HttpPostImageJsonObjectResult(context, "", params, paramString, image, filePath, callback);
    }
}
