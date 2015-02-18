package com.travis.rhinofit.models;

import android.os.Parcel;

import com.travis.rhinofit.global.Constants;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class Attendance extends BaseModel implements Comparable<Attendance> {

    int attendanceId;
    String title;
    Date when;

    boolean isActionAttendance;

    public Attendance(JSONObject jsonObject) {
        if ( jsonObject != null ) {
            attendanceId = JSONModel.getIntFromJson(jsonObject, Constants.kResponseKeyAttendanceId);
            title = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyAttendanceTitle);
            when = JSONModel.getDateFromJson(jsonObject, Constants.kResponseKeyAttendanceWhen);
        }

        isActionAttendance = false;
    }

    public Attendance(Parcel in) {
        super(in);
        attendanceId = in.readInt();
        title = in.readString();
        when = (Date)in.readValue(ClassLoader.getSystemClassLoader());

        isActionAttendance = false;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(attendanceId);
        out.writeString(title);
        out.writeValue(when);
    }

    @Override
    public int compareTo(Attendance another) {
        return this.when.compareTo(another.when);
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public boolean isActionAttendance() {
        return isActionAttendance;
    }

    public void setActionAttendance(boolean isActionAttendance) {
        this.isActionAttendance = isActionAttendance;
    }
}
