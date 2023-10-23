package com.blue.corelib.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat

/**
 * 字符串转数字
 */
object NumFormatUtil {

    var format = DecimalFormat()

    init {
        // 如果用户当前语言环境为西欧国家，format后的会被加上分隔符","，导致无法再被转为number
        format.isGroupingUsed = false
        val formatSymbols = DecimalFormatSymbols()
        formatSymbols.decimalSeparator = '.'
        format.decimalFormatSymbols = formatSymbols
    }

    fun format(value: Double, pattern: String?): String {
        format.applyPattern(pattern)
        return format.format(value)
    }

    fun parse(number: String?): Number {
        return try {
            NumberFormat.getInstance().parse(number ?: "0") ?: 0
        } catch (e: Exception) {
            0
        }
    }

}