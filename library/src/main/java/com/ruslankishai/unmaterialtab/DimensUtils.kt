package com.ruslankishai.unmaterialtab

import android.content.Context
import android.util.TypedValue

/**
 * Dimension and measurement methods and values.
 *
 * Created by Ruslan Kishai on 1/2/2017.
 */

object DimensUtils {

    fun spToPx(context: Context, sps: Int): Int {
        return Math.round(context.resources.displayMetrics.scaledDensity * sps)
    }

    fun dpToPx(context: Context, dps: Int): Int {
        return Math.round(context.resources.displayMetrics.density * dps)
    }

    fun spToPx(context: Context, sps: Float): Float {
        val dm = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sps, dm)
    }

    fun dpToPx(context: Context, dps: Float): Float {
        val dm = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, dm)
    }
}
