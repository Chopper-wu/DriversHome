package com.blue.corelib.extensions

import android.content.Context
import android.content.SharedPreferences
import com.blue.corelib.utils.appContext
import com.blue.corelib.utils.toJson

class SharedPreferencesExt(private val xmlFile: String) {

    private val sharedPreferences by lazy {
        appContext.getSharedPreferences(xmlFile, Context.MODE_MULTI_PROCESS)
    }

    fun setValue(key: String, value: Any) {
        try {
            sharedPreferences.edit(true) {
                when (value) {
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    is Boolean -> putBoolean(key, value)
                    is String -> putString(key, value)
                    else -> putString(key, value.toJson())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getValue(key: String, defaultValue: Any): Any {
        return try {
            when (defaultValue) {
                is Int -> sharedPreferences.getInt(key, defaultValue)
                is Long -> sharedPreferences.getLong(key, defaultValue)
                is Float -> sharedPreferences.getFloat(key, defaultValue)
                is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
                is String -> sharedPreferences.getString(key, defaultValue)!!
                else -> sharedPreferences.getString(key, "")!!
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return defaultValue
        }
    }

    fun remove(key: String) {
        try {
            sharedPreferences.edit() {
                this.remove(key)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clear() {
        try {
            sharedPreferences.edit().clear().apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

inline fun SharedPreferences.edit(
    commit: Boolean = false,
    action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}