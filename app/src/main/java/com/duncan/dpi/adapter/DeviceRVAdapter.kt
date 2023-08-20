package com.duncan.dpi.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duncan.dpi.databinding.ListEntryBinding
import com.duncan.dpi.`interface`.ItemClickListener
import com.duncan.dpi.model.Device

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = data[position]
        holder.binding.labelDeviceName.text = device.name
        holder.binding.labelDeviceSpecs.text = "${device.width}x${device.height} @ ${String.format("%.1f", device.screenSize)}\""

        when(position) {
            0 -> holder.binding.labelDeviceName.setTypeface(null, Typeface.BOLD)
            else -> holder.binding.labelDeviceName.setTypeface(null, Typeface.NORMAL)
        }

        holder.itemView.setOnClickListener { view ->
            itemClickListener?.onItemClick(view, device)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListEntryBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(val binding: ListEntryBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}