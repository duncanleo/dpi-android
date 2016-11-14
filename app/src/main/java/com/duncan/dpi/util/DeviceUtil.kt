package com.duncan.dpi.util

import android.content.Context
import com.duncan.dpi.model.Device
import com.squareup.moshi.Moshi

/**
 * Created by duncan on 11/3/15.
 */
class DeviceUtil(context: Context) {
    private val context: Context

    init {
        this.context = context
    }

    fun getDeviceList(): Array<Device> {
        val inputStream = context.assets.open("devices.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Array<Device>::class.java)
        val result = adapter.fromJson(String(buffer))
        return result
    }
}
