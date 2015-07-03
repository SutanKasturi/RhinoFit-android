package com.travis.rhinofit.models;

import android.os.Parcel;
import android.util.Log;

import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.utils.DateUtils;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class RhinofitClass extends BaseModel implements Comparable<RhinofitClass> {

    private static final String LOG_TAG = "RhinofitClass";

    String classId;
    int aId;
    int instructorId;
    int reservationId;
    int day;
    boolean allDay;
    String title;
    String instructorName;
    String color;
    String origColor;
    Date startDate;
    Date endDate;
    String startDateString;

    boolean isActionReservation;
    boolean isActionAttendance;

    public RhinofitClass(JSONObject jsonObject) {
        if ( jsonObject != null ) {
            classId             = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyClassId);
            aId                 = JSONModel.getIntFromJson(jsonObject, Constants.kResponseKeyAttendanceId);
            instructorId        = JSONModel.getIntFromJson(jsonObject, Constants.kResponseKeyInstructorId);
            reservationId       = JSONModel.getIntFromJson(jsonObject, Constants.kResponseKeyReservation);
            day                 = JSONModel.getIntFromJson(jsonObject, Constants.kResponseKeyDay);
            allDay              = JSONModel.getBooleanFromJson(jsonObject, Constants.kResponseKeyAllDay);
            title               = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyTitle);
            instructorName      = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyInstructorName);
            color               = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyColor);
            origColor           = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyOrigColor);
            startDate           = JSONModel.getDateFromJson(jsonObject, Constants.kResponseKeyStartDate);
            endDate             = JSONModel.getDateFromJson(jsonObject, Constants.kResponseKeyEndDate);
            startDateString     = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyStartDate);
        }


        isActionAttendance = false;
        isActionReservation = false;
    }

    public RhinofitClass(Parcel in) {
        super(in);
        classId = in.readString();
        aId = in.readInt();
        instructorId = in.readInt();
        reservationId = in.readInt();
        day = in.readInt();
        boolean[] booleans = new boolean[1];
        in.readBooleanArray(booleans);
        allDay = booleans[0];
        title = in.readString();
        instructorName = in.readString();
        color = in.readString();
        origColor = in.readString();
        startDate = (Date)in.readValue(ClassLoader.getSystemClassLoader());
        endDate = (Date)in.readValue(ClassLoader.getSystemClassLoader());

        isActionReservation = false;
        isActionAttendance = false;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(classId);
        out.writeInt(aId);
        out.writeInt(instructorId);
        out.writeInt(reservationId);
        out.writeInt(day);
        boolean[] booleans = new boolean[1];
        booleans[0] = allDay;
        out.writeBooleanArray(booleans);
        out.writeString(title);
        out.writeString(instructorName);
        out.writeString(color);
        out.writeString(origColor);
        out.writeValue(startDate);
        out.writeValue(endDate);
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public int getaId() {
        return aId;
    }

    public void setaId(int aId) {
        this.aId = aId;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOrigColor() {
        return origColor;
    }

    public void setOrigColor(String origColor) {
        this.origColor = origColor;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDate) {
        this.startDateString = startDate;
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

    public boolean isActionReservation() {
        return isActionReservation;
    }

    public void setActionReservation(boolean isActionReservation) {
        this.isActionReservation = isActionReservation;
    }

    public boolean isActionAttendance() {
        return isActionAttendance;
    }

    public void setActionAttendance(boolean isActionAttendance) {
        this.isActionAttendance = isActionAttendance;
    }

    @Override
    public int compareTo(RhinofitClass another) {
        if ( this.getStartDate() == null )
            return -1;
        if ( another.getStartDate() == null )
            return 1;
        return this.getStartDate().compareTo(another.getStartDate());
    }
}
