package com.duncan.dpi;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class DeviceListActivity extends ActionBarActivity {
    Toolbar toolbar;
    ListView list;
    List<Device> deviceList = new ArrayList<>();
    DeviceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicelist);

        //Vars
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        list = (ListView)findViewById(R.id.deviceList);

        setSupportActionBar(toolbar);

        adapter = new DeviceListAdapter(this, deviceList);
        list.setAdapter(adapter);
        deviceList.add(new Device(Build.MANUFACTURER + " " + Build.MODEL + " (Your device)", CalcUtil.getWidth(this), CalcUtil.getHeight(this), CalcUtil.getScreenSize(this), true));
        deviceList.addAll(DeviceUtil.getDeviceList());
        adapter.notifyDataSetChanged();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("device", deviceList.get(position));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
