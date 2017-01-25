package com.ruslankishai.unmaterialtab;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by Ruslan Kishai aka creageek on 1/2/2017.
 */

public class DimensUtils {

    public static int spToPx(Context context, int sps) {
        return Math.round(context.getResources().getDisplayMetrics().scaledDensity * sps);
    }

    public static int dpToPx(Context context, int dps) {
        return Math.round(context.getResources().getDisplayMetrics().density * dps);
    }

    public static float spToPx(Context context, float spValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, dm);
    }

    public static float dpToPx(Context context, float dpValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, dm);
    }

}
