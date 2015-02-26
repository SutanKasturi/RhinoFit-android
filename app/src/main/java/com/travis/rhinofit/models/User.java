package com.travis.rhinofit.models;

import android.os.Parcel;

import com.travis.rhinofit.global.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class User extends BaseModel {

    JSONObject jsonObject;

    String userAddress1;
    String userAddress2;
    String userCity;
    String userCountry;
    String userFirstName;
    String userLastName;
    String userPhone1;
    String userPhone2;
    String userState;
    String userZip;
    String userEmail;
    String userPicture;

    public User(JSONObject jsonObject) {
        super();
        parseDictionary(jsonObject);
    }

    public void parseDictionary(JSONObject jsonObject) {
        if ( jsonObject != null ) {
            userAddress1 = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserAddress1);
            userAddress2 = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserAddress2);
            userCity = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserCity);
            userCountry = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserCountry);
            userFirstName = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserFirstName);
            userLastName = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserLastName);
            userPhone1 = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserPhone1);
            userPhone2 = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserPhone2);
            userState = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserState);
            userZip = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserZip);
            userEmail = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserName);
            userPicture = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyUserPicture);
        }

        this.jsonObject = jsonObject;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel in) {
        super(in);
        userAddress1 = in.readString();
        userAddress2 = in.readString();
        userCity = in.readString();
        userCountry = in.readString();
        userFirstName = in.readString();
        userLastName = in.readString();
        userPhone1 = in.readString();
        userPhone2 = in.readString();
        userState = in.readString();
        userZip = in.readString();
        userEmail = in.readString();
        userPicture = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(userAddress1);
        out.writeString(userAddress2);
        out.writeString(userCity);
        out.writeString(userCountry);
        out.writeString(userFirstName);
        out.writeString(userLastName);
        out.writeString(userPhone1);
        out.writeString(userPhone2);
        out.writeString(userState);
        out.writeString(userZip);
        out.writeString(userEmail);
        out.writeString(userPicture);
    }

    public String getUserAddress1() {
        return userAddress1;
    }

    public void setUserAddress1(String userAddress1) {
        this.userAddress1 = userAddress1;
    }

    public String getUserAddress2() {
        return userAddress2;
    }

    public void setUserAddress2(String userAddress2) {
        this.userAddress2 = userAddress2;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserPhone1() {
        return userPhone1;
    }

    public void setUserPhone1(String userPhone1) {
        this.userPhone1 = userPhone1;
    }

    public String getUserPhone2() {
        return userPhone2;
    }

    public void setUserPhone2(String userPhone2) {
        this.userPhone2 = userPhone2;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getUserZip() {
        return userZip;
    }

    public void setUserZip(String userZip) {
        this.userZip = userZip;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public JSONObject getJsonObject() {
        try {
            jsonObject.put(Constants.kResponseKeyUserAddress1, userAddress1);
            jsonObject.put(Constants.kResponseKeyUserAddress2, userAddress2);
            jsonObject.put(Constants.kResponseKeyUserCity, userCity);
            jsonObject.put(Constants.kResponseKeyUserCountry, userCountry);
            jsonObject.put(Constants.kResponseKeyUserFirstName, userFirstName);
            jsonObject.put(Constants.kResponseKeyUserLastName, userLastName);
            jsonObject.put(Constants.kResponseKeyUserPhone1, userPhone1);
            jsonObject.put(Constants.kResponseKeyUserPhone2, userPhone2);
            jsonObject.put(Constants.kResponseKeyUserState, userState);
            jsonObject.put(Constants.kResponseKeyUserZip, userZip);
            jsonObject.put(Constants.kResponseKeyUserName, userEmail);
            jsonObject.put(Constants.kResponseKeyUserPicture, userPicture);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
