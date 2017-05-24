package com.yhl.demoother.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kissmzz on 2017/5/7.
 */

public class Pic implements Parcelable {
    private String picUrl;
    private String ctime;
    private String title;
    private String description;
    private String url;

    public Pic() {

    }

    public Pic(String picUrl, String ctime, String title, String description, String url) {
        this.picUrl = picUrl;
        this.ctime = ctime;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picUrl);
        dest.writeString(this.ctime);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.url);
    }

    protected Pic(Parcel in) {
        this.picUrl = in.readString();
        this.ctime = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Pic> CREATOR = new Parcelable.Creator<Pic>() {
        @Override
        public Pic createFromParcel(Parcel source) {
            return new Pic(source);
        }

        @Override
        public Pic[] newArray(int size) {
            return new Pic[size];
        }
    };
}
