package com.travis.rhinofit.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Sutan Kasturi on 2/11/15.
 */
public class ResourceSize {
    public static float convertDpToPixel(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
