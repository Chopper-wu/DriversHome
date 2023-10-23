package com.blue.corelib.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView

class NestedScrollX5WebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    init {
        //标准化初始化
        initWebSettings()
        setInitialScale(0)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebSettings() {
        settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            setSupportZoom(false)
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = true
            setSupportMultipleWindows(true)
            domStorageEnabled = true
            setAppCacheEnabled(true)
            setGeolocationEnabled(false)
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            setAppCacheMaxSize(Long.MAX_VALUE)
            pluginState = WebSettings.PluginState.ON_DEMAND
            cacheMode = WebSettings.LOAD_NO_CACHE
            setAppCachePath(context.getDir("AppCache", 0).path)
            databasePath = context.getDir("databases", 0).path
            defaultTextEncodingName = Charsets.UTF_8.name()
        }
    }
}