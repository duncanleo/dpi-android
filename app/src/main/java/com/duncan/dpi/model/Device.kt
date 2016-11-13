package com.duncan.dpi.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by duncan on 11/3/15.
 */
class Device : Parcelable {
    var title: String? = null
        private set
    var screenWidth: Int = 0
        private set
    var screenHeight: Int = 0
        private set
    var screenSize: Double = 0.toDouble()
        private set
    var isTitleHighlighted = false

    constructor(title: String, screenWidth: Int, screenHeight: Int, screenSize: Double) {
        this.title = title
        this.screenWidth = screenWidth
        this.screenHeight = screenHeight
        this.screenSize = screenSize
    }

    constructor(title: String, screenWidth: Int, screenHeight: Int, screenSize: Double, titleHighlighted: Boolean) {
        this.title = title
        this.screenWidth = screenWidth
        this.screenHeight = screenHeight
        this.screenSize = screenSize
        this.isTitleHighlighted = titleHighlighted
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.title)
        dest.writeInt(this.screenWidth)
        dest.writeInt(this.screenHeight)
        dest.writeDouble(this.screenSize)
    }

    private constructor(`in`: Parcel) {
        this.title = `in`.readString()
        this.screenWidth = `in`.readInt()
        this.screenHeight = `in`.readInt()
        this.screenSize = `in`.readDouble()
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<Device> = object : Parcelable.Creator<Device> {
            override fun createFromParcel(source: Parcel): Device {
                return Device(source)
            }

            override fun newArray(size: Int): Array<Device?> {
                return arrayOfNulls(size)
            }
        }
    }
}
