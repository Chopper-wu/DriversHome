package com.blue.corelib.extensions

import android.content.Context
import android.net.ConnectivityManager
import com.blue.corelib.utils.appContext
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
fun <T> Any.getGenericClass(index: Int): Class<T> {
    return (javaClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.get(index) as? Class<T>
        ?: throw IllegalStateException()
}

fun tryCatch(block: () -> Unit) {
    return try {
        block.invoke()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

/**
 * 检查是否有网络
 */
fun isNetEnable(): Boolean {
    val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (connectivityManager != null) {
        val info = connectivityManager.activeNetworkInfo
        if (info != null && info.isConnected) {
            return true
        }
        return false
    }
    return false
}

/**
 * 检查是否wifi
 */
fun isWifi(): Boolean {
    val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (connectivityManager != null) {
        val info = connectivityManager.activeNetworkInfo
        if (info != null && info.type == ConnectivityManager.TYPE_WIFI) {
            return true
        }
        return false
    }
    return false
}