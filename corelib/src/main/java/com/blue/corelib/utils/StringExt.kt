package com.blue.corelib.utils

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.blue.corelib.R
import java.text.SimpleDateFormat
import java.util.*

fun String?.toast(duration: Int = Toast.LENGTH_LONG) =
    if (isNullOrBlank()) {
        ""
    } else {
        ToastUtils.setToastView(appContext)
        ToastUtils.showToast(this, duration, appContext)
        this!!
    }

fun Long.formatDate(pattern: String = "yyyy-MM-dd HH:mm:ss") =
    Date(this).format(pattern)

fun Date?.format(pattern: String = "yyyy-MM-dd HH:mm:ss") =
    SimpleDateFormat(pattern, Locale.CHINA).format(this)

/**
 * 将格式化后的时间字符串转为 Date 对象，如果转换失败就会返回 defaultTime 的 Date
 */
fun String?.toDate(
    format: String,
    locale: Locale = Locale.CHINA,
    defaultTime: Long = System.currentTimeMillis()
) =
    if (isNullOrBlank()) {
        Date(defaultTime)
    } else
        try {
            SimpleDateFormat(format, locale).parse(this) ?: Date(defaultTime)
        } catch (e: Exception) {
            Date(defaultTime)
        }

/**
 * 将格式化后的时间字符串转为 Date 对象，如果转换失败就会返回空
 */
fun String?.toDateEmpty(
    format: String,
    locale: Locale = Locale.CHINA,
    defaultTime: Long = System.currentTimeMillis()
) =
    if (isNullOrBlank()) {
        null
    } else
        try {
            SimpleDateFormat(format, locale).parse(this) ?: Date(defaultTime)
        } catch (e: Exception) {
            null
        }

/**
 * 格式化数字
 */
fun Number.format(pattern: String = "0.##"): String = NumFormatUtil.format(this.toDouble(), pattern)

fun Number.formatRMB(): String = NumFormatUtil.format(this.toDouble(), "¥###,###,##0.00")