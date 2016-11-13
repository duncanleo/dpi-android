package com.duncan.dpi.util;

import com.duncan.dpi.model.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duncan on 11/3/15.
 */
public class DeviceUtil {
    private static List<Device> deviceList = new ArrayList<>();

    public static List<Device> getDeviceList() {
        //Populate list if empty
        if (deviceList.size() == 0) {
            populateList();
        }
        return deviceList;
    }

    /**
     * Populate the list
     */
    private static void populateList() {
        deviceList.add(new Device("Apple iPhone 5", 640, 1136, 4.0));
        deviceList.add(new Device("Apple iPhone 6", 750, 1334, 4.7));
        deviceList.add(new Device("Apple iPhone 6 Plus", 1080, 1920, 5.5));
        deviceList.add(new Device("HTC Evo 3D", 540, 960, 4.3));
        deviceList.add(new Device("Sony Xperia S", 720, 1080, 4.3));
        deviceList.add(new Device("Samsung Galaxy S6", 1440, 2560, 5.1));
        deviceList.add(new Device("Macbook Air 11\"", 1366, 768, 11.6));
        deviceList.add(new Device("Macbook 12\"", 2304, 1440, 12));
        deviceList.add(new Device("Macbook Pro (Retina) 13\"", 2560, 1600, 13.3));
        deviceList.add(new Device("Macbook Pro (Retina) 15\"", 2880, 1800, 15.4));
        deviceList.add(new Device("Chromebook Pixel", 2560, 1700, 12.85));
        deviceList.add(new Device("ASUS Zenbook UX51VZ", 2880, 1620, 15.6));
    }
}
