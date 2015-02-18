package com.travis.rhinofit.models;

import android.os.Parcel;

import com.travis.rhinofit.global.Constants;

import org.json.JSONObject;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class AvailableBenchmark extends BaseModel {

    int benchmarkId;
    String bDescription;
    String bType;
    String bFormat;

    public AvailableBenchmark(JSONObject jsonObject) {
        if ( jsonObject != null ) {
            benchmarkId = JSONModel.getIntFromJson(jsonObject, Constants.kParamBId);
            bDescription = JSONModel.getStringFromJson(jsonObject, Constants.kParamBDesc);
            bType = JSONModel.getStringFromJson(jsonObject, Constants.kParamBType);
            bFormat = JSONModel.getStringFromJson(jsonObject, Constants.kParamBFormat);
        }
    }

    public AvailableBenchmark(Parcel in) {
        super(in);
        benchmarkId = in.readInt();
        bDescription = in.readString();
        bType = in.readString();
        bFormat = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(benchmarkId);
        out.writeString(bDescription);
        out.writeString(bType);
        out.writeString(bFormat);
    }

    public int getBenchmarkId() {
        return benchmarkId;
    }

    public void setBenchmarkId(int benchmarkId) {
        this.benchmarkId = benchmarkId;
    }

    public String getbDescription() {
        return bDescription;
    }

    public void setbDescription(String bDescription) {
        this.bDescription = bDescription;
    }

    public String getbType() {
        return bType;
    }

    public void setbType(String bType) {
        this.bType = bType;
    }

    public String getbFormat() {
        return bFormat;
    }

    public void setbFormat(String bFormat) {
        this.bFormat = bFormat;
    }
}
