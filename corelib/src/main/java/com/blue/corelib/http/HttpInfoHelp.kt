package com.blue.corelib.http

import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.*

/**
 * Created by Chopper on 2021/3/17
 * desc : body参数封装类
 */
object HttpInfoHelp {
    fun getRequestBody(hashMap: HashMap<String, String>): RequestBody? {
        val data = StringBuffer()
        if (hashMap != null && hashMap.size > 0) {
            val iter: Iterator<*> = hashMap.iterator()
            while (iter.hasNext()) {
                val entry = iter.next() as Map.Entry<*, *>
                val key = entry.key
                val `val` = entry.value
                data.append(key).append("=").append(`val`).append("&")
            }
        }
        val jsonObj = JSONObject(hashMap as Map<*, *>?)
        return RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            jsonObj.toString()
        )
    }

    fun getRequestBodyForm(hashMap: HashMap<String, String>): RequestBody? {
        val data = StringBuffer()
        if (hashMap != null && hashMap.size > 0) {
            val iter: Iterator<*> = hashMap.iterator()
            while (iter.hasNext()) {
                val entry = iter.next() as Map.Entry<*, *>
                val key = entry.key
                val `val` = entry.value
                data.append(key).append("=").append(`val`).append("&")
            }
        }
        val jso = data.substring(0, if (data.length > 1) data.length - 1 else 0)
        return RequestBody.create(
            MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),
            jso
        )
    }

}