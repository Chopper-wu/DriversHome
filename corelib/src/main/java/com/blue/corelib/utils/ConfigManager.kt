package com.newcw.component.utils

import com.blue.corelib.extensions.SharedPreferencesExt

class ConfigManager {

    companion object {
        private const val xmlFile = "jianhu_config_xml"
        private val sharedPreferencesExt by lazy {
            SharedPreferencesExt(xmlFile)
        }

        @JvmStatic
        @JvmOverloads
        fun getIntValue(key: String, defaultValue: Int = 0): Int =
            sharedPreferencesExt.getValue(key, defaultValue) as Int

        @JvmStatic
        fun setIntValue(key: String, value: Int) =
            sharedPreferencesExt.setValue(key, value)


        @JvmStatic
        @JvmOverloads
        fun getLongValue(key: String, defalut: Long = 0): Long =
            sharedPreferencesExt.getValue(key, defalut) as Long

        @JvmStatic
        fun setLongValue(key: String, value: Long) =
            sharedPreferencesExt.setValue(key, value)



        @JvmStatic
        @JvmOverloads
        fun getFloatValue(key: String, defalut: Float = 0f): Float =
            sharedPreferencesExt.getValue(key, defalut) as Float

        @JvmStatic
        fun setFloatValue(key: String, value: Float) =
            sharedPreferencesExt.setValue(key, value)


        @JvmStatic
        @JvmOverloads
        fun getStringValue(key: String, defaultStr: String? = ""): String =
            sharedPreferencesExt.getValue(key, defaultStr ?: "") as String

        @JvmStatic
        fun setStringValue(key: String, value: String) =
            sharedPreferencesExt.setValue(key, value)


        @JvmStatic
        @JvmOverloads
        fun getBooleanValue(key: String, defalut: Boolean = false): Boolean =
            sharedPreferencesExt.getValue(key, defalut) as Boolean

        @JvmStatic
        fun setBooleanValue(key: String, value: Boolean) =
            sharedPreferencesExt.setValue(key, value)

        @JvmStatic
        fun remove(key: String) {
            sharedPreferencesExt.remove(key)
        }

        @JvmStatic
        @JvmOverloads
        fun getAnyValue(key: String, defalut: Any): Any =
            sharedPreferencesExt.getValue(key, defalut) as Any

        @JvmStatic
        fun setAnyValue(key: String, value: Any) =
            sharedPreferencesExt.setValue(key, value)
    }

}


