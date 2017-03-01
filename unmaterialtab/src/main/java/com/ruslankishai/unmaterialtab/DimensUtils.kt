package com.ruslankishai.unmaterialtab

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

/**
* Created by Ruslan Kishai on 1/2/2017.
*/

object DimensUtils {

    fun spToPx(context: Context, sps: Int): Int {
        return Math.round(context.resources.displayMetrics.scaledDensity * sps)
    }

    fun dpToPx(context: Context, dps: Int): Int {
        return Math.round(context.resources.displayMetrics.density * dps)
    }

    fun spToPx(context: Context, spValue: Float): Float {
        val dm = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, dm)
    }

    fun dpToPx(context: Context, dpValue: Float): Float {
        val dm = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, dm)
    }

}
