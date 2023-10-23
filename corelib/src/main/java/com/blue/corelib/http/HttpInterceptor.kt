package com.blue.corelib.http

import com.blue.corelib.extensions.isNetEnable
import com.blue.corelib.utils.CacheDemo
import com.blue.corelib.utils.DeviceUtil
import com.blue.corelib.utils.appContext
import com.newcw.component.utils.ConfigManager
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 检查网络拦截器
 */
class CheckNetworkInterceptor : Interceptor {

    override fun intercept(p0: Interceptor.Chain): Response {
        if (isNetEnable()) {
            return p0.proceed(p0.request())
        } else {
            throw NoNetWorkException()
        }
    }

}

/**
 * 动态修改BaseUrl
 */
class BaseUrlInterceptor : Interceptor {

    override fun intercept(p0: Interceptor.Chain): Response {
        val originRequest = p0.request()
        // 获取现有的HttpUrl
        val oldHttpUrl = originRequest.url()

        val requestBuild = originRequest.newBuilder()

        val newHttpUrl = oldHttpUrl
                .newBuilder()
                .addQueryParameter("source", "Android")
                .addQueryParameter("appVersion", "${DeviceUtil.getVersionCode(appContext)}")
                .addQueryParameter("timestamp", "${System.currentTimeMillis()}")
                .build()
        return p0.proceed(requestBuild.url(newHttpUrl).build())
    }

}

/**
 * 添加头部拦截器
 */
class HeaderInterceptor : Interceptor {

    override fun intercept(p0: Interceptor.Chain): Response {
        val originRequest = p0.request()
        val requestBuild = originRequest.newBuilder()
        requestBuild.header("token", (CacheDemo.getToken() ?: ""))
        return p0.proceed(requestBuild.build())
    }

}