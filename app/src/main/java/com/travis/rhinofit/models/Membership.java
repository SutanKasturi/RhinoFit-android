package com.travis.rhinofit.models;

import android.os.Parcel;

import com.travis.rhinofit.global.Constants;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class Membership extends BaseModel {

    int membershipId;
    String membershipName;
    Date startDate;
    Date endDate;
    String renewal;
    int attended;
    int attendedLimit;
    String limit;

    public Membership(JSONObject jsonObject) {
        if ( jsonObject != null ) {
            membershipId = JSONModel.getIntFromJson(jsonObject, Constants.kParamMId);
            membershipName = JSONModel.getStringFromJson(jsonObject, Constants.kParamMName);
            startDate = JSONModel.getDateFromJson(jsonObject, Constants.kParamMStartDate);
            endDate = JSONModel.getDateFromJson(jsonObject, Constants.kParamMEndDate);
            renewal = JSONModel.getStringFromJson(jsonObject, Constants.kParamMRenewal);
            attended = JSONModel.getIntFromJson(jsonObject, Constants.kParamMAttended);
            attendedLimit = JSONModel.getIntFromJson(jsonObject, Constants.kParamMAttendedLimit);
            limit = JSONModel.getStringFromJson(jsonObject, Constants.kParamMLimit);
        }
    }

    public Membership(Parcel in) {
        super(in);
        membershipId = in.readInt();
        membershipName = in.readString();
        startDate = (Date)in.readValue(ClassLoader.getSystemClassLoader());
        endDate = (Date)in.readValue(ClassLoader.getSystemClassLoader());
        renewal = in.readString();
        attended = in.readInt();
        attendedLimit = in.readInt();
        limit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(membershipId);
        out.writeString(membershipName);
        out.writeValue(startDate);
        out.writeValue(endDate);
        out.writeString(renewal);
        out.writeInt(attended);
        out.writeInt(attendedLimit);
        out.writeString(limit);
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public String getMembershipName() {
        return membershipName;
    }

    public void setMembershipName(String membershipName) {
        this.membershipName = membershipName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRenewal() {
        return renewal;
    }

    public void setRenewal(String renewal) {
        this.renewal = renewal;
    }

    public int getAttended() {
        return attended;
    }

    public void setAttended(int attended) {
        this.attended = attended;
    }

    public int getAttendedLimit() {
        return attendedLimit;
    }

    public void setAttendedLimit(int attendedLimit) {
        this.attendedLimit = attendedLimit;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
