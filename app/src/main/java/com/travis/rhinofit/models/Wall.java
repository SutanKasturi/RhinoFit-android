package com.travis.rhinofit.models;

import android.os.Parcel;

import com.travis.rhinofit.global.Constants;

import org.json.JSONObject;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class Wall extends BaseModel {

    int wallId;
    String msg;
    String name;
    String profileImage;
    String image;
    boolean yours;
    boolean flaggable;

    public Wall(JSONObject jsonObject) {
        if ( jsonObject != null ) {
            wallId = JSONModel.getIntFromJson(jsonObject, Constants.kResponseKeyWallId);
            msg = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyWallMessage);
            name = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyWallUserName);
            profileImage = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyWallUserPicture);
            image = JSONModel.getStringFromJson(jsonObject, Constants.kResponseKeyWallPic);
            yours = JSONModel.getIntFromJson(jsonObject, Constants.kResponseKeyWallYours) == 1 ? true : false;
            flaggable = JSONModel.getIntFromJson(jsonObject, Constants.kResponseKeyWallFlaggable) == 1 ? true : false;
        }
    }
    public Wall(Parcel in) {
        super(in);
        wallId = in.readInt();
        msg = in.readString();
        name = in.readString();
        profileImage = in.readString();
        image = in.readString();
        boolean[] booleanValue = new boolean[1];
        in.readBooleanArray(booleanValue);
        yours = booleanValue[0];
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(wallId);
        out.writeString(msg);
        out.writeString(name);
        out.writeString(profileImage);
        out.writeString(image);
        out.writeBooleanArray(new boolean[]{yours});
    }

    public int getWallId() {
        return wallId;
    }

    public void setWallId(int wallId) {
        this.wallId = wallId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isYours() {
        return yours;
    }

    public boolean getFlaggable()  {
        return flaggable;
    }

    public void setFlaggable(boolean flaggable) {
        this.flaggable = flaggable;
    }

    public void setYours(boolean yours) {
        this.yours = yours;
    }
}
