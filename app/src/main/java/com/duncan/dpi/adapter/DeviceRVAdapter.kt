package com.duncan.dpi.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.duncan.dpi.R
import com.duncan.dpi.`interface`.ItemClickListener
import com.duncan.dpi.model.Device
import kotlinx.android.synthetic.main.list_entry.view.*

/**
 * Created by duncanleo on 13/11/16.
 */
class DeviceRVAdapter(data: List<Device>) : RecyclerView.Adapter<DeviceRVAdapter.ViewHolder>() {
    private val data: List<Device>
    var itemClickListener: ItemClickListener<Device>? = null

    init {
        this.data = data
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val device = data[position]
        holder?.deviceName?.text = device.title
        holder?.deviceSpecs?.text = "${device.screenWidth}x${device.screenHeight} @ ${String.format("%.1f", device.screenSize)}\""

        when(position) {
            0 -> holder?.deviceName?.setTypeface(null, Typeface.BOLD)
            else -> holder?.deviceName?.setTypeface(null, Typeface.NORMAL)
        }

        holder?.itemView?.setOnClickListener { view ->
            itemClickListener?.onItemClick(view, device)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_entry, parent, false))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView
        val deviceSpecs: TextView

        init {
            this.deviceName = itemView.labelDeviceName
            this.deviceSpecs = itemView.labelDeviceSpecs
        }
    }
}