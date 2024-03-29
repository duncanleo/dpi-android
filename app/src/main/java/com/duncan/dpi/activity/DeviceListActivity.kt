package com.duncan.dpi.activity

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.duncan.dpi.adapter.DeviceRVAdapter
import com.duncan.dpi.databinding.ActivityDevicelistBinding
import com.duncan.dpi.`interface`.ItemClickListener
import com.duncan.dpi.model.Device
import com.duncan.dpi.util.CalcUtil
import com.duncan.dpi.util.DeviceUtil
import java.util.*


class DeviceListActivity : AppCompatActivity() {
    internal var deviceList: MutableList<Device> = ArrayList()
    private lateinit var binding: ActivityDevicelistBinding

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) // getRealSize is actually available in API 14+. This warning sidesteps the bug where it's flagged otherwise
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDevicelistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager =
            LinearLayoutManager(this@DeviceListActivity)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this@DeviceListActivity,
                layoutManager.orientation
            )
        )

        val point = Point()
        windowManager.defaultDisplay.getRealSize(point)
        val widthPixels = point.x
        val heightPixels = point.y
        val densityDPI = resources.displayMetrics.densityDpi

        deviceList.add(Device("${Build.MANUFACTURER} ${Build.MODEL} (Your device)",  widthPixels, heightPixels, CalcUtil.calculateScreenSize(widthPixels, heightPixels, densityDPI)))
        deviceList.addAll(DeviceUtil(this@DeviceListActivity).getDeviceList())

        val adapter = DeviceRVAdapter(deviceList)
        binding.recyclerView.adapter = adapter

        adapter.itemClickListener = object : ItemClickListener<Device> {
            override fun onItemClick(view: View, data: Device) {
                val resultIntent = Intent()
                resultIntent.putExtra("device", data)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
