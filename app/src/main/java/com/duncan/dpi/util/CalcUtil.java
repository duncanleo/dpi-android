package com.duncan.dpi.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by duncan on 29/12/14.
 */
public class CalcUtil {
    private static DisplayMetrics dm;

    //Others
    public static float pxFromDp(Context context, float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static List<String> calculateResolutions(double dpi, double screenSize, boolean standard) {
        List<String> ret = new ArrayList<>();
        double bothSquaredSum = Math.pow(dpi * screenSize, 2);
        for (int i = 0; i < 10000; i++) {
            double other = Math.sqrt(bothSquaredSum - Math.pow(i, 2));
            double higher = Math.max(i, other), lower = Math.min(i, other);
            if (Double.isNaN(lower)) {
                continue;
            }
            if (standard) {
                double ratio = higher / lower;
                if (ratio == 4.0/3.0 || ratio == 16.0/9.0 || ratio == 16.0/10.0) {
                    ret.add(String.format("%.0fx%.0f", higher, lower));
                }
            } else {
                ret.add(String.format("%.0fx%.0f", higher, lower));
            }
        }
        return ret;
    }

    //Other
    public static double calculateDPI(int width, int height, double screenSize) {
        return Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2)) / screenSize;
    }

    //My Screen methods
    public static double getDPI(Activity activity) {
        return calculateDPI(getWidth(activity), getHeight(activity), Double.valueOf(new DecimalFormat("#.##").format(getScreenSize(activity))));
    }

    public static int getWidth(Activity activity) {
        if (dm == null) {
            dm = new DisplayMetrics();
        }
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getHeight(Activity activity) {
        if (dm == null) {
            dm = new DisplayMetrics();
        }
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static double getScreenSize(Activity activity) {
        if (dm == null) {
            dm = new DisplayMetrics();
        }
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/ dm.densityDpi,2);
        double y = Math.pow(dm.heightPixels/ dm.densityDpi,2);
        return Math.sqrt(x+y);
    }
}
