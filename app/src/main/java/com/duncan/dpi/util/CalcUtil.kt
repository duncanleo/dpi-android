package com.duncan.dpi.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

import java.text.DecimalFormat
import java.util.ArrayList

/**
 * Created by duncan on 29/12/14.
 */
object CalcUtil {
    private var dm: DisplayMetrics? = null

    //Others
    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun calculateResolutions(dpi: Double, screenSize: Double, standard: Boolean): List<String> {
        val ret = ArrayList<String>()
        val bothSquaredSum = Math.pow(dpi * screenSize, 2.0)
        for (i in 0..9999) {
            val other = Math.sqrt(bothSquaredSum - Math.pow(i.toDouble(), 2.0))
            val higher = Math.max(i.toDouble(), other)
            val lower = Math.min(i.toDouble(), other)
            if (java.lang.Double.isNaN(lower)) {
                continue
            }
            if (standard) {
                val ratio = higher / lower
                if (ratio == 4.0 / 3.0 || ratio == 16.0 / 9.0 || ratio == 16.0 / 10.0) {
                    ret.add(String.format("%.0fx%.0f", higher, lower))
                }
            } else {
                ret.add(String.format("%.0fx%.0f", higher, lower))
            }
        }
        return ret
    }

    //Other
    fun calculateDPI(width: Int, height: Int, screenSize: Double): Double {
        return Math.sqrt(Math.pow(width.toDouble(), 2.0) + Math.pow(height.toDouble(), 2.0)) / screenSize
    }

    //My Screen methods
    fun getDPI(activity: Activity): Double {
        return calculateDPI(getWidth(activity), getHeight(activity), java.lang.Double.valueOf(DecimalFormat("#.##").format(getScreenSize(activity)))!!)
    }

    fun getWidth(activity: Activity): Int {
        if (dm == null) {
            dm = DisplayMetrics()
        }
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return dm!!.widthPixels
    }

    fun getHeight(activity: Activity): Int {
        if (dm == null) {
            dm = DisplayMetrics()
        }
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return dm!!.heightPixels
    }

    fun getScreenSize(activity: Activity): Double {
        if (dm == null) {
            dm = DisplayMetrics()
        }
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val x = Math.pow((dm!!.widthPixels / dm!!.densityDpi).toDouble(), 2.0)
        val y = Math.pow((dm!!.heightPixels / dm!!.densityDpi).toDouble(), 2.0)
        return Math.sqrt(x + y)
    }
}
