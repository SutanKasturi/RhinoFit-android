package com.travis.rhinofit.models;

import android.os.Parcel;

import com.travis.rhinofit.global.Constants;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class MyBenchmark extends BaseModel {

    int benchmarkId;
    String title;
    String type;
    String currentScore;
    String lastScore;
    Date currentDate;
    Date lastDate;

    public MyBenchmark() {

    }

    public MyBenchmark(JSONObject jsonObject) {
        if ( jsonObject != null ) {
            benchmarkId = JSONModel.getIntFromJson(jsonObject, Constants.kParamBId);
            title = JSONModel.getStringFromJson(jsonObject, Constants.kParamBDesc);
            type = JSONModel.getStringFromJson(jsonObject, Constants.kParamBType);
            currentScore = JSONModel.getStringFromJson(jsonObject, Constants.kParamBBestResults);
            lastScore = JSONModel.getStringFromJson(jsonObject, Constants.kParamBLastResults);
            currentDate = JSONModel.getDateFromJson(jsonObject, Constants.kParamBBestDate);
            lastDate = JSONModel.getDateFromJson(jsonObject, Constants.kParamBLastDate);
        }
    }

    public MyBenchmark(Parcel in) {
        super(in);
        benchmarkId = in.readInt();
        title = in.readString();
        type = in.readString();
        currentScore = in.readString();
        lastScore = in.readString();
        currentDate = (Date)in.readValue(ClassLoader.getSystemClassLoader());
        lastDate= (Date)in.readValue(ClassLoader.getSystemClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(benchmarkId);
        out.writeString(title);
        out.writeString(type);
        out.writeString(currentScore);
        out.writeString(lastScore);
        out.writeValue(currentDate);
        out.writeValue(lastDate);
    }

    public int getBenchmarkId() {
        return benchmarkId;
    }

    public void setBenchmarkId(int benchmarkId) {
        this.benchmarkId = benchmarkId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(String currentScore) {
        this.currentScore = currentScore;
    }

    public String getLastScore() {
        return lastScore;
    }

    public void setLastScore(String lastScore) {
        this.lastScore = lastScore;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }
}
