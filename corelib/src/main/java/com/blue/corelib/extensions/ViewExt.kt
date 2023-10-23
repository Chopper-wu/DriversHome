package com.blue.corelib.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.blue.corelib.utils.AppBarStateChangeListener
import com.blue.corelib.utils.appContext
import com.google.android.material.appbar.AppBarLayout

fun View?.hideSoftInput() {
    this?.apply {
        if (windowToken != null) {
            (appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
                windowToken,
                0
            )
        }
    }
}

fun View?.showSoftInput() {
    this?.apply {
        postDelayed({
            requestFocus()
            if (windowToken != null) {
                (appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                    this,
                    0
                )
            }
        }, 100)
    }
}

fun View?.visible(visible: Boolean, invisible: Boolean = false) {
    this?.apply {
        visibility = if (visible) {
            View.VISIBLE
        } else {
            if (invisible) {
                View.INVISIBLE
            } else {
                View.GONE
            }
        }
    }
}

fun View?.isVisible(): Boolean {
    return this?.run {
        visibility == View.VISIBLE
    } ?: false
}


fun AppBarLayout?.addListener(
    eollapsedBlock: (() -> Unit)? = null,
    otherBlock: (() -> Unit)? = null
) {
    this?.apply {
        addOnOffsetChangedListener(object : AppBarStateChangeListener() {

            var isCollapsed = true

            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                when (state) {
                    // 折叠状态
                    State.COLLAPSED -> {
                        isCollapsed = true
                        eollapsedBlock?.invoke()
                    }
                    // 其他状态
                    else -> {
                        if (isCollapsed) {
                            isCollapsed = false
                            otherBlock?.invoke()
                        }
                    }
                }
            }

        })
    }
}