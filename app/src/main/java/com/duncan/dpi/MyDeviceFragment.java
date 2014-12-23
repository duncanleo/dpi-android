package com.duncan.dpi;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by duncan on 24/10/14.
 */
public class MyDeviceFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.mydevice, container, false);

        TextView manufacturer = (TextView)v.findViewById(R.id.manufacturer);
        TextView model = (TextView)v.findViewById(R.id.model);

        TextView res = (TextView)v.findViewById(R.id.phone_res);
        TextView dpi = (TextView)v.findViewById(R.id.phone_dpi);
        TextView size = (TextView)v.findViewById(R.id.phone_size);

        manufacturer.setText(Build.BRAND.toUpperCase());
        model.setText(Build.MODEL);

        //WxH
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        res.setText(String.format("%dx%d", width, height));

        //Screen size
        double x = Math.pow(width/dm.densityDpi,2);
        double y = Math.pow(height/dm.densityDpi,2);
        double screenInches = Math.sqrt(x+y);
        size.setText(String.format("%.2f", screenInches));

        //DPI
        double dpiValue = Math.sqrt((width * width) + (height * height)) / screenInches;
        dpi.setText(String.format("%.2f", dpiValue));
        return v;
    }
}
