package com.blue.corelib.utils

import android.graphics.Typeface

/**
 * 自定义字体
 */
object TypeFaceUtils {

    private var numTypeface: Typeface? = null

    fun getNumTypeface(): Typeface {
        if (numTypeface == null) {
            numTypeface = Typeface.createFromAsset(appContext.assets, "fonts/VeneerCleanSoft.otf")
        }
        return numTypeface!!
    }
}