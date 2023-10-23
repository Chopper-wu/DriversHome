package com.blue.corelib.http

import com.blue.corelib.AppManager
import com.blue.corelib.utils.appContext
import com.blue.corelib.utils.toast
import com.blue.xrouter.XRouter
import com.google.gson.JsonParseException
import io.reactivex.subscribers.DisposableSubscriber
import org.greenrobot.eventbus.EventBus
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseSubscriber<T> : DisposableSubscriber<T>() {

    override fun onNext(t: T) {
    }

    override fun onComplete() {
    }

    override fun onError(t: Throwable?) {
//        if (BuildConfig.DEBUG) {
//            t?.printStackTrace()
//        }
        var info = t?.message
        if (t is TokenException) {
            //登录失效后，重置缓存
            AppManager.getInstance().exitUser(appContext)
        } else if (t is StopException) {
            //账号停用
            AppManager.getInstance().exitUser(appContext)
        }else if (t is SingleLoginException) {
            //单点登录
            AppManager.getInstance().singleLoginDialog(info)
            return
        } else if (t is SocketTimeoutException || t is ConnectException || t is UnknownHostException) {
            info = "网络异常,请检查网络状况!"
        } else if (t is JsonParseException) {
            info = "数据异常"
        } else if (t is NullPointerException) {
            info = "数据为空"
        }
        // 底层统一toast提示
        info.toast()
    }

}