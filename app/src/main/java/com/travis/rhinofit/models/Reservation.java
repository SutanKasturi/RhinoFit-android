package com.travis.rhinofit.models;

import android.os.Parcel;

import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.utils.DateUtils;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class Reservation extends BaseModel implements Comparable<Reservation> {

    int reservationId;
    String title;
    Date when;

    boolean isActionReservation;

    public Reservation(JSONObject jsonObject) {
        if ( jsonObject != null ) {
            reservationId = JSONModel.getIntFromJson(jsonObject, Constants.kResponseKeyReservationId);
            title = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyReservationTitle);
            when = JSONModel.getDateFromJson(jsonObject, Constants.kResponseKeyReservationWhen);
        }

        isActionReservation = false;
    }

    public Reservation(Parcel in) {
        super(in);
        reservationId = in.readInt();
        title = in.readString();
        when = (Date)in.readValue(ClassLoader.getSystemClassLoader());

        isActionReservation = false;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(reservationId);
        out.writeString(title);
        out.writeValue(when);
    }

    @Override
    public int compareTo(Reservation another) {
        return this.when.compareTo(another.when);
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
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

    public boolean isActionReservation() {
        return isActionReservation;
    }

    public void setActionReservation(boolean isActionReservation) {
        this.isActionReservation = isActionReservation;
    }
}
