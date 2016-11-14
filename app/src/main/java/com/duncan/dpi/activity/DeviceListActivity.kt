package com.duncan.dpi.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.duncan.dpi.R
import com.duncan.dpi.`interface`.ItemClickListener
import com.duncan.dpi.adapter.DeviceRVAdapter
import com.duncan.dpi.model.Device
import com.duncan.dpi.util.CalcUtil
import com.duncan.dpi.util.DeviceUtil
import kotlinx.android.synthetic.main.activity_devicelist.*
import java.util.*


class DeviceListActivity : AppCompatActivity() {
    internal var deviceList: MutableList<Device> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devicelist)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this@DeviceListActivity)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this@DeviceListActivity, layoutManager.orientation))

        deviceList.add(Device("${Build.MANUFACTURER} ${Build.MODEL} (Your device)",  CalcUtil.getWidth(this), CalcUtil.getHeight(this), CalcUtil.getScreenSize(this)))
        deviceList.addAll(DeviceUtil.getDeviceList())

        val adapter = DeviceRVAdapter(deviceList)
        recyclerView.adapter = adapter

        adapter.itemClickListener = object : ItemClickListener<Device> {
            override fun onItemClick(view: View, data: Device) {
                val resultIntent = Intent()
                resultIntent.putExtra("device", data)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
