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
public class WodInfo extends BaseModel {

    private static final String LOG_TAG = "WodInfo";
    
    String classId;
    String name;
    String title;
    String wod;
    String wodId;
    String results;
    Date startDate;
    boolean canEdit;

    public WodInfo(JSONObject jsonObject) {
        if ( jsonObject != null ) {
            classId = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyWodId);
            name = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyWodName);
            title = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyWodTitle);
            wod = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyWodWod);
            wodId = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyWodWodId);
            results = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyWodResults);
            startDate = JSONModel.getDateFromJson(jsonObject, Constants.kResponseKeyWodStart);
            canEdit = JSONModel.getIntFromJson(jsonObject, Constants.kResponseKeyWodCanEdit) == 1 ? true : false;
        }
    }
    public WodInfo(Parcel in) {
        super(in);
        classId = in.readString();
        name = in.readString();
        title = in.readString();
        wod = in.readString();
        wodId = in.readString();
        results = in.readString();
        startDate = (Date)in.readValue(ClassLoader.getSystemClassLoader());
        boolean[] booleans = new boolean[1];
        in.readBooleanArray(booleans);
        canEdit = booleans[0];
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(classId);
        out.writeString(name);
        out.writeString(title);
        out.writeString(wod);
        out.writeString(wodId);
        out.writeString(results);
        out.writeValue(startDate);
        out.writeBooleanArray(new boolean[]{canEdit});
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWod() {
        return wod;
    }

    public void setWod(String wod) {
        this.wod = wod;
    }

    public String getWodId() {
        return wodId;
    }

    public void setWodId(String wodId) {
        this.wodId = wodId;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
}
