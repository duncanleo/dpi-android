package com.duncan.dpi.`interface`

import android.view.View

/**
 * Created by duncanleo on 13/11/16.
 */
interface ItemClickListener<T> {
    fun onItemClick(view: View, data: T)
}