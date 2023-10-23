package com.blue.corelib.base

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.blue.corelib.R
import com.blue.corelib.utils.Logger
import com.blue.corelib.view.LoadingDialog
import com.gyf.immersionbar.ImmersionBar

open abstract class BaseFragment : Fragment() {
    var TAG: String = javaClass.name
    private var loadingDialog: LoadingDialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Logger.e("onCreate", javaClass.name)

        //防止穿透事件
        view.isClickable = true
    }

    // 隐藏软键盘
    open fun HideSoftInput(token: IBinder?) {
        if (token != null) {
            val manager =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(
                    token,
                    InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    /**
     * 偏移状态栏高度
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun offsetStatusBar(view: View) {
        view.setPadding(
                view.paddingStart,
                view.paddingTop + ImmersionBar.getStatusBarHeight(this),
                view.paddingEnd,
                view.paddingBottom
        )
    }

    fun setBackgroundAlpha(bgAlpha: Float) {
        val lp = (context as Activity).window
                .attributes
        lp.alpha = bgAlpha
        (context as Activity).window.attributes = lp
    }

    fun showLoadingDialog() {
        showLoadingDialog(false)
    }

    fun showLoadingDialog(cancelable: Boolean) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(requireContext(), R.style.LoadingDialog, cancelable)
        }
        loadingDialog?.showDialog(R.string.common_loading)
    }


    fun dismissLoadingDialog() {
        loadingDialog?.hideDialog()
    }

}