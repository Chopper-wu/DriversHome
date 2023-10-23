package com.blue.corelib.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import java.util.*


/**
 * 日期和时间组件
 */
object TimeUtils {

    /**
     * 日期选择
     */
    fun showDatePickerDialog(
        activity: Activity,
        calendar: Calendar,
        block: () -> Unit
    ) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        DatePickerDialog(activity, DatePickerDialog.THEME_HOLO_DARK,
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                block.invoke()
            } // 设置初始日期
            , calendar[Calendar.YEAR]
            , calendar[Calendar.MONTH]
            , calendar[Calendar.DAY_OF_MONTH]).show()
    }

    /**
     * 时间选择
     */
    fun showTimePickerDialog(
        activity: Activity,
        calendar: Calendar,
        block: () -> Unit
    ) {
        TimePickerDialog(activity, DatePickerDialog.THEME_HOLO_DARK,
            OnTimeSetListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                block.invoke()
            } // 设置初始时间
            , calendar[Calendar.HOUR_OF_DAY]
            , calendar[Calendar.MINUTE]
            , true).show()
    }

}