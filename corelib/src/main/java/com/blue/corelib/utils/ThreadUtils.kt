package com.blue.corelib.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ThreadUtils {
    private val handler = Handler(Looper.getMainLooper())
    private val ioThread = Executors.newScheduledThreadPool(3)
    fun ui(callBack: () -> Unit, delay: Long) {
        if (delay == 0L && isUIThread()) {
            callBack.invoke()
        } else {
            handler.postDelayed(callBack, delay)
        }
    }

    fun io(callBack: () -> Unit, delay: Long) {
        ioThread.schedule(callBack, delay, TimeUnit.MILLISECONDS)
    }

    private fun isUIThread() =
        Thread.currentThread().id == Looper.getMainLooper().thread.id
}

@JvmOverloads
fun UI(delay: Long = 0, callBack: () -> Unit) {
    ThreadUtils.ui(callBack, delay)
}

@JvmOverloads
fun IO(delay: Long = 0, callBack: () -> Unit) {
    ThreadUtils.io(callBack, delay)
}