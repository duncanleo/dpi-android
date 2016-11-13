package com.duncan.dpi.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.ListView

import com.duncan.dpi.R
import com.duncan.dpi.adapter.DeviceListAdapter
import com.duncan.dpi.model.Device
import com.duncan.dpi.util.CalcUtil
import com.duncan.dpi.util.DeviceUtil

import java.util.ArrayList


class DeviceListActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var list: ListView
    internal var deviceList: MutableList<Device> = ArrayList()
    lateinit var adapter: DeviceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devicelist)

        //Vars
        toolbar = findViewById(R.id.toolbar) as Toolbar
        list = findViewById(R.id.deviceList) as ListView

        setSupportActionBar(toolbar)

        adapter = DeviceListAdapter(this, deviceList)
        list.adapter = adapter
        deviceList.add(Device(Build.MANUFACTURER + " " + Build.MODEL + " (Your device)", CalcUtil.getWidth(this), CalcUtil.getHeight(this), CalcUtil.getScreenSize(this), true))
        deviceList.addAll(DeviceUtil.getDeviceList())
        adapter.notifyDataSetChanged()

        list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val resultIntent = Intent()
            resultIntent.putExtra("device", deviceList[position])
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
