package com.duncan.dpi.util

import com.duncan.dpi.model.Device

import java.util.ArrayList

/**
 * Created by duncan on 11/3/15.
 */
object DeviceUtil {
    private val deviceList = ArrayList<Device>()

    fun getDeviceList(): List<Device> {
        //Populate list if empty
        if (deviceList.size == 0) {
            populateList()
        }
        return deviceList
    }

    /**
     * Populate the list
     */
    private fun populateList() {
        deviceList.add(Device("Apple iPhone 5", 640, 1136, 4.0))
        deviceList.add(Device("Apple iPhone 6", 750, 1334, 4.7))
        deviceList.add(Device("Apple iPhone 6 Plus", 1080, 1920, 5.5))
        deviceList.add(Device("HTC Evo 3D", 540, 960, 4.3))
        deviceList.add(Device("Sony Xperia S", 720, 1080, 4.3))
        deviceList.add(Device("Samsung Galaxy S6", 1440, 2560, 5.1))
        deviceList.add(Device("Macbook Air 11\"", 1366, 768, 11.6))
        deviceList.add(Device("Macbook 12\"", 2304, 1440, 12.0))
        deviceList.add(Device("Macbook Pro (Retina) 13\"", 2560, 1600, 13.3))
        deviceList.add(Device("Macbook Pro (Retina) 15\"", 2880, 1800, 15.4))
        deviceList.add(Device("Chromebook Pixel", 2560, 1700, 12.85))
        deviceList.add(Device("ASUS Zenbook UX51VZ", 2880, 1620, 15.6))
    }
}
