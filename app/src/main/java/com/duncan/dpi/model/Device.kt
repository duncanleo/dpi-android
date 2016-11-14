package com.duncan.dpi.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

/**
 * Created by duncan on 11/3/15.
 */
data class Device(
        @Json(name = "name") var name: String,
        @Json(name = "width") var width: Int,
        @Json(name = "height") var height: Int,
        @Json(name = "screenSize") var screenSize: Double
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Device> = object : Parcelable.Creator<Device> {
            override fun createFromParcel(source: Parcel): Device = Device(source)
            override fun newArray(size: Int): Array<Device?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readInt(), source.readInt(), source.readDouble())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeInt(width)
        dest?.writeInt(height)
        dest?.writeDouble(screenSize)
    }
}