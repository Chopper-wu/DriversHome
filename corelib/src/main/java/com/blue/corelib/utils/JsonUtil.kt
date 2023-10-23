package com.blue.corelib.utils

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.reflect.Type
import java.util.*

/**
 * 所有json解析请都用该类
 */
object JsonUtil {
    const val TAG = "JsonUtil"
    val gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(
                object : TypeToken<TreeMap<String, Any>>() {

                }.type, MapTypeAdapter()
            ).create()
    }

    fun toJson(o: Any?): String {
        return gson.toJson(o)
    }

    fun <T> fromJson(json: String?, type: Type): T? {
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            Log.e(TAG, "fromJson ", e)
            null
        }
    }

    inline fun <reified T> mapToBean(map: Map<out Any?, Any?>?): T? {
        if (map == null) {
            return null
        }
        return map.toJson().toObject<T>(T::class.java)
    }

    class MapTypeAdapter : TypeAdapter<Any>() {

        @Throws(IOException::class)
        override fun read(input: JsonReader): Any? {
            val token = input.peek()
            when (token) {
                JsonToken.BEGIN_ARRAY -> {
                    val list = ArrayList<Any?>()
                    input.beginArray()
                    while (input.hasNext()) {
                        list.add(read(input))
                    }
                    input.endArray()
                    return list
                }

                JsonToken.BEGIN_OBJECT -> {
                    val map = LinkedTreeMap<String, Any>()
                    input.beginObject()
                    while (input.hasNext()) {
                        map[input.nextName()] = read(input)
                    }
                    input.endObject()
                    return map
                }

                JsonToken.STRING -> return input.nextString()

                JsonToken.NUMBER -> {
                    /**
                     * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    val dbNum = input.nextDouble()

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > java.lang.Long.MAX_VALUE) {
                        return dbNum
                    }

                    // 判断数字是否为整数值
                    val lngNum = dbNum.toLong()
                    return if (dbNum == lngNum.toDouble()) {
                        lngNum
                    } else {
                        dbNum
                    }
                }

                JsonToken.BOOLEAN -> return input.nextBoolean()

                JsonToken.NULL -> {
                    input.nextNull()
                    return null
                }

                else -> throw IllegalStateException()
            }
        }

        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: Any) {
            // 序列化无需实现
        }

    }
}

fun <T> String.toObject(type: Type): T? = JsonUtil.fromJson(this, type)

fun Any?.toJson() = JsonUtil.toJson(this)
