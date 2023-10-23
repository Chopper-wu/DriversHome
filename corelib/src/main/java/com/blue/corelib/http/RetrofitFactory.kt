package com.blue.corelib.http

import com.blue.corelib.utils.Logger
import com.blue.corelib.utils.appContext
import com.blue.xrouter.BuildConfig
import com.blue.xrouter.XRouter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit

object RetrofitFactory {

    const val BASE_URL = "https://bms.dancelife.online/"
    const val BASE_URL1 = "\"https://uat2ncwdriver.newchiwan.com/\""
    private val retrofit by lazy {

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(CheckNetworkInterceptor())
                    .addInterceptor(BaseUrlInterceptor())
                    .addInterceptor(HeaderInterceptor())
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.BUILD_TYPE != "release") {
                            HttpLoggingInterceptor.Level.BODY
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        }
                    })
                    .connectTimeout(20L, TimeUnit.SECONDS)
                    .writeTimeout(20L, TimeUnit.SECONDS)
                    .readTimeout(20L, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .proxy(if (BuildConfig.BUILD_TYPE != "release") null else Proxy.NO_PROXY)
                    .build()
            )
            .build()
    }

    fun <T> create(jClass: Class<T>): T {
        retrofit.baseUrl()
        return retrofit.create(jClass)
    }
}