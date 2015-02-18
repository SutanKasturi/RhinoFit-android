package com.travis.rhinofit.models;

import android.os.Parcel;

import com.travis.rhinofit.global.Constants;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class BenchmarkHistory extends BaseModel implements Comparable<BenchmarkHistory> {

    int benchmarkDataId;
    Date date;
    String value;

    String type;

    public BenchmarkHistory(JSONObject jsonObject, String type) {
        this.type = type;
        if ( jsonObject != null ) {
            benchmarkDataId = JSONModel.getIntFromJson(jsonObject, Constants.kParamId);
            date = JSONModel.getDateFromJson(jsonObject, Constants.kParamDate);
            value = JSONModel.getStringFromJson(jsonObject, Constants.kParamValue);
            if ( type.indexOf(":") > 0 && value.indexOf(":") < 0 ) {
                value = value + ":00";
            }
        }
    }

    public BenchmarkHistory(Parcel in) {
        super(in);
        benchmarkDataId = in.readInt();
        date = (Date)in.readValue(ClassLoader.getSystemClassLoader());
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(benchmarkDataId);
        out.writeValue(date);
        out.writeString(value);
    }

    @Override
    public int compareTo(BenchmarkHistory another) {
        return this.date.compareTo(another.date);
    }

    public int getBenchmarkDataId() {
        return benchmarkDataId;
    }

    public void setBenchmarkDataId(int benchmarkDataId) {
        this.benchmarkDataId = benchmarkDataId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
