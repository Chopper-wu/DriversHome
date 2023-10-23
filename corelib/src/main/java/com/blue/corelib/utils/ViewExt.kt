package com.blue.corelib.utils

import android.view.View
import com.blue.corelib.base.config.RegexConfig
import com.jakewharton.rxbinding.view.RxView
import java.util.concurrent.TimeUnit

/**
 *
 * @description:
 * @date :2020/10/17 16:20
 */

fun View.click(block: () -> Unit) {
    RxView.clicks(this).throttleFirst(RegexConfig.SPACING_TIME, TimeUnit.MILLISECONDS)
        .subscribe { block.invoke() }
}