package com.duncan.dpi;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by duncan on 11/3/15.
 */
public class DeviceListAdapter extends BaseAdapter {
    Context context;
    List<Device> data;

    public DeviceListAdapter(Context context, List<Device> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceListViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_entry, parent, false);
            viewHolder = new DeviceListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DeviceListViewHolder)convertView.getTag();
        }
        Device device = data.get(position);
        viewHolder.mainText.setText(device.getTitle());
        viewHolder.mainText.setSelected(true);
        viewHolder.mainText.requestFocus();
        viewHolder.subText.setText(String.format("%dx%d @ %.1f\"", device.getScreenWidth(), device.getScreenHeight(), device.getScreenSize()));
        if (device.isTitleHighlighted()) {
            viewHolder.mainText.setTypeface(null, Typeface.BOLD);
        } else {
            viewHolder.mainText.setTypeface(null, Typeface.NORMAL);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    class DeviceListViewHolder {
        TextView mainText, subText;

        public DeviceListViewHolder(View root) {
            this.mainText = (TextView)root.findViewById(R.id.mainText);
            this.subText = (TextView)root.findViewById(R.id.subText);
        }
    }
}
