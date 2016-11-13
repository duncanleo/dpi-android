package com.duncan.dpi.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.duncan.dpi.R
import com.duncan.dpi.model.Device

/**
 * Created by duncan on 11/3/15.
 */
class DeviceListAdapter(internal var context: Context, internal var data: List<Device>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: DeviceListViewHolder
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_entry, parent, false)
            viewHolder = DeviceListViewHolder(view)
            view!!.tag = viewHolder
        } else {
            viewHolder = view.tag as DeviceListViewHolder
        }
        val device = data[position]
        viewHolder.mainText.text = device.title
        viewHolder.mainText.isSelected = true
        viewHolder.mainText.requestFocus()
        viewHolder.subText.text = String.format("%dx%d @ %.1f\"", device.screenWidth, device.screenHeight, device.screenSize)
        if (device.isTitleHighlighted) {
            viewHolder.mainText.setTypeface(null, Typeface.BOLD)
        } else {
            viewHolder.mainText.setTypeface(null, Typeface.NORMAL)
        }
        return view
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    internal inner class DeviceListViewHolder(root: View) {
        var mainText: TextView
        var subText: TextView

        init {
            this.mainText = root.findViewById(R.id.mainText) as TextView
            this.subText = root.findViewById(R.id.subText) as TextView
        }
    }
}
