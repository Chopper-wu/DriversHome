package com.blue.corelib.http

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blue.corelib.R
import com.blue.corelib.utils.Logger
import com.blue.corelib.utils.appContext
import com.blue.corelib.utils.toPx
import com.orhanobut.dialogplus.DialogPlus
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import retrofit2.Response

/**
 * 线程切换
 */
class ObservableSchedulers<T> : ObservableTransformer<T, T> {
    override fun apply(@NonNull p0: Observable<T>): ObservableSource<T> {
        return p0.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

class FlowableSchedulers<T> : FlowableTransformer<T, T> {
    override fun apply(@NonNull p0: Flowable<T>): Publisher<T> {
        return p0.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

class SingleSchedulers<T> : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

class CompletableSchedulers : CompletableTransformer {
    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

/**
 * 数据转换
 */
class DataTransformer<T> : FlowableTransformer<Response<BaseResponse<T>>, T> {

    override fun apply(p0: Flowable<Response<BaseResponse<T>>>): Publisher<T> {
        return p0.map(HttpBodyFun())
            .map(AppDataFun())
    }
}


class DataTransformerIncludeNull<T> :
    FlowableTransformer<Response<BaseResponse<T>>, BaseResponse<T>> {

    override fun apply(p0: Flowable<Response<BaseResponse<T>>>): Publisher<BaseResponse<T>> {
        return p0.map(HttpBodyFun())
            .map(AppDataFunIncludeNull())
    }
}

class DataTransformerIncludeError<T> :
    FlowableTransformer<Response<BaseResponse<T>>, BaseResponse<T>> {

    override fun apply(p0: Flowable<Response<BaseResponse<T>>>): Publisher<BaseResponse<T>> {
        return p0.map(HttpBodyFun())
    }
}

class HttpBodyFun<T> : Function<Response<BaseResponse<T>>, BaseResponse<T>> {

    override fun apply(p0: Response<BaseResponse<T>>): BaseResponse<T>? {
        if (!p0.isSuccessful) {
            if (p0.code()==404){
                Logger.e("HttpBodyFun", "11111111111111111111:" + p0.code())
            }else if (p0.code()==500){
                Logger.e("HttpBodyFun", "11111111111111111111:" + p0.code())
            }
            Logger.e("HttpBodyFun", "11111111111111111111111:" + p0.message())
            throw HttpBodyException()
        } else {
            // p0.body().data
        }
        return p0.body()
    }
}

class AppDataFun<T> : Function<BaseResponse<T>, T> {

    override fun apply(p0: BaseResponse<T>): T? {
        if (p0.code != 0) {
            when (p0.code) {
                // 请登录
                401 -> throw TokenException(p0.msg ?: "token error")
                // 系统错误
                500 -> throw SystemException(p0.msg ?: "system error")
                // 请求参数错误
                1003 -> throw ParamsException(p0.msg ?: "params error")
                // 业务错误
                400 -> throw BusinessException(p0.msg ?: "business error")
                // 账号停用
                1001 -> throw StopException(p0.msg ?: "user error")
                // 单点登录
                1002 -> throw SingleLoginException(p0.msg ?: "login error")
            }
        }
        return p0.data
    }

}

class AppDataFunIncludeNull<T> : Function<BaseResponse<T>, BaseResponse<T>> {

    override fun apply(p0: BaseResponse<T>): BaseResponse<T> {
        if (p0.code != 0) {
            when (p0.code) {
                // 请登录
                401 -> throw TokenException(p0.msg ?: "token error")
                // 系统错误
                500 -> throw SystemException(p0.msg ?: "system error")
                // 请求参数错误
                1003 -> throw ParamsException(p0.msg ?: "params error")
                // 业务错误
                400 -> throw BusinessException(p0.msg ?: "business error")
                // 账号停用
                1001 -> throw StopException(p0.msg ?: "user error")
                // 单点登录
                1002 -> throw SingleLoginException(p0.msg ?: "login error")
            }
        }
        if (p0.data == null) {

        }
        return p0
    }

}

/**
 * 通用情况:线程切换+数据转换
 */
@Deprecated("server will return null data")
class SchedulersAndBodyTransformer<T> : FlowableTransformer<Response<BaseResponse<T>>, T> {

    override fun apply(p0: Flowable<Response<BaseResponse<T>>>): Publisher<T> {
        return p0.compose(FlowableSchedulers()).compose(DataTransformer())
    }
}

class SchedulersAndBodyTransformerIncludeNull<T> :
    FlowableTransformer<Response<BaseResponse<T>>, BaseResponse<T>> {

    override fun apply(p0: Flowable<Response<BaseResponse<T>>>): Publisher<BaseResponse<T>> {
        return p0.compose(FlowableSchedulers()).compose(DataTransformerIncludeNull())
    }
}

class SchedulersAndBodyTransformerIncludeError<T> :
    FlowableTransformer<Response<BaseResponse<T>>, BaseResponse<T>> {

    override fun apply(p0: Flowable<Response<BaseResponse<T>>>): Publisher<BaseResponse<T>> {
        return p0.compose(FlowableSchedulers()).compose(DataTransformerIncludeError())
    }
}
