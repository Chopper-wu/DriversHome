package com.blue.corelib.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blue.corelib.base.BaseViewModel
import com.blue.corelib.http.BaseResponse
import com.blue.corelib.http.BaseSubscriber
import com.uber.autodispose.*
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.*
import io.reactivex.disposables.Disposable

fun Disposable?.disposeSafely() =
    this?.apply {
        if (!isDisposed) {
            dispose()
        }
    }

fun <T> FlowableSubscribeProxy<T>?.subscribeNet(
    errorBlock: ((errorMsg: String) -> Unit)? = null,
    successBlock: ((T) -> Unit)? = null
) =
    this?.subscribe(object : BaseSubscriber<T>() {
        override fun onNext(t: T) {
            successBlock?.invoke(t)
        }

        override fun onError(t: Throwable?) {
            super.onError(t)
            errorBlock?.invoke(t?.message ?: "")
        }
    })



fun <T> FlowableSubscribeProxy<BaseResponse<T>>?.subscribeNetIncludeNull(
    errorBlock: ((errorMsg: String) -> Unit)? = null,
    successBlock: ((T?) -> Unit)? = null
) =
    this?.subscribe(object : BaseSubscriber<BaseResponse<T>>() {
        override fun onNext(t: BaseResponse<T>) {
            successBlock?.invoke(t.data)
        }

        override fun onError(t: Throwable?) {
            super.onError(t)
            errorBlock?.invoke(t?.message ?: "")
        }
    })

fun <T> Observable<T>.autoDisposableDefault(lifecycleOwner: LifecycleOwner): ObservableSubscribeProxy<T> {
    val event =
        if (lifecycleOwner is Fragment) Lifecycle.Event.ON_STOP else Lifecycle.Event.ON_DESTROY
    return this.`as`(
        AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                event
            )
        )
    )
}

fun <T> Flowable<T>.autoDisposableDefault(lifecycleOwner: LifecycleOwner): FlowableSubscribeProxy<T> {
    val event =
        if (lifecycleOwner is Fragment) Lifecycle.Event.ON_STOP else Lifecycle.Event.ON_DESTROY
    return this.`as`(
        AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                event
            )
        )
    )
}

fun Completable.autoDisposableDefault(lifecycleOwner: LifecycleOwner): CompletableSubscribeProxy {
    val event =
        if (lifecycleOwner is Fragment) Lifecycle.Event.ON_STOP else Lifecycle.Event.ON_DESTROY
    return this.`as`(
        AutoDispose.autoDisposable<Any>(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                event
            )
        )
    )
}

fun <T> Single<T>.autoDisposableDefault(lifecycleOwner: LifecycleOwner): SingleSubscribeProxy<T> {
    val event =
        if (lifecycleOwner is Fragment) Lifecycle.Event.ON_STOP else Lifecycle.Event.ON_DESTROY
    return this.`as`(
        AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                event
            )
        )
    )
}

fun <T> Maybe<T>.autoDisposableDefault(lifecycleOwner: LifecycleOwner): MaybeSubscribeProxy<T> {
    val event =
        if (lifecycleOwner is Fragment) Lifecycle.Event.ON_STOP else Lifecycle.Event.ON_DESTROY
    return this.`as`(
        AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                event
            )
        )
    )
}

fun <T> Observable<T>.autoDisposableVM(
    viewModel: BaseViewModel,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) =
    this.`as`(
        AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                viewModel.lifecycleOwner,
                untilEvent
            )
        )
    )

fun <T> Flowable<T>.autoDisposableVM(
    viewModel: BaseViewModel,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) =
    this.`as`(
        AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                viewModel.lifecycleOwner,
                untilEvent
            )
        )
    )

fun <T> Single<T>.autoDisposableVM(
    viewModel: BaseViewModel,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) =
    this.`as`(
        AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                viewModel.lifecycleOwner,
                untilEvent
            )
        )
    )

fun <T> Maybe<T>.autoDisposableVM(
    viewModel: BaseViewModel,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) =
    this.`as`(
        AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                viewModel.lifecycleOwner,
                untilEvent
            )
        )
    )

fun Completable.autoDisposableVM(
    viewModel: BaseViewModel,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) =
    this.`as`(
        AutoDispose.autoDisposable<Any>(
            AndroidLifecycleScopeProvider.from(
                viewModel.lifecycleOwner,
                untilEvent
            )
        )
    )

/**
 * 取消时间订阅
 *
 * @param disposable
 */
fun cancelTimer(disposable: Disposable?) {
    if (disposable != null && !disposable.isDisposed) {
        disposable.dispose()
    }
}

/**
 * 取消时间订阅
 *
 * @param disposableList
 */
fun cancelTimer(disposableList: List<Disposable>?) {
    if (disposableList != null && disposableList.size > 0) {
        for (i in disposableList.indices) {
            if (disposableList[i].isDisposed) disposableList[i].dispose()
        }
    }
}