package com.duncan.dpi.util

/**
 * Created by duncan on 29/12/14.
 */
object CalcUtil {
    /**
     * Calculate DPI from width, height and screenSize
     */
    fun calculateDPI(width: Int, height: Int, screenSize: Double): Double {
        return Math.sqrt(Math.pow(width.toDouble(), 2.0) + Math.pow(height.toDouble(), 2.0)) / screenSize
    }

    /**
     * Calculate screen size from width, height and densityDPI
     */
    fun calculateScreenSize(width: Int, height: Int, densityDPI: Int): Double {
        val x = Math.pow(width / densityDPI.toDouble(), 2.0)
        val y = Math.pow(height / densityDPI.toDouble(), 2.0)
        return Math.sqrt(x + y)
    }
}
