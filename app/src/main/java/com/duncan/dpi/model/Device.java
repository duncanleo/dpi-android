package com.duncan.dpi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by duncan on 11/3/15.
 */
public class Device implements Parcelable {
    private String title;
    private int screenWidth, screenHeight;
    private double screenSize;
    private boolean titleHighlighted = false;

    public Device(String title, int screenWidth, int screenHeight, double screenSize) {
        this.title = title;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenSize = screenSize;
    }

    public Device(String title, int screenWidth, int screenHeight, double screenSize, boolean titleHighlighted) {
        this.title = title;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenSize = screenSize;
        this.titleHighlighted = titleHighlighted;
    }

    public String getTitle() {
        return title;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public double getScreenSize() {
        return screenSize;
    }

    public boolean isTitleHighlighted() {
        return titleHighlighted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.screenWidth);
        dest.writeInt(this.screenHeight);
        dest.writeDouble(this.screenSize);
    }

    private Device(Parcel in) {
        this.title = in.readString();
        this.screenWidth = in.readInt();
        this.screenHeight = in.readInt();
        this.screenSize = in.readDouble();
    }

    public static final Parcelable.Creator<Device> CREATOR = new Parcelable.Creator<Device>() {
        public Device createFromParcel(Parcel source) {
            return new Device(source);
        }

        public Device[] newArray(int size) {
            return new Device[size];
        }
    };
}
