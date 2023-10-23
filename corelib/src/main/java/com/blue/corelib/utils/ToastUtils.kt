package com.blue.corelib.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.blue.corelib.utils.appContext
import com.blue.corelib.view.ToastView

/**
 *created by chopper on 2021/9/1
 *Description:  弹出toast提示工具类
 */
object ToastUtils {
    private var mToast: Toast? = null  //toast样式
    private val mMsg: String? = null  //上一次弹出的内容
    private var mToastView: ToastView? = null  //自定义view
    private var mToastGravity:Int = -1  //位置

    /**
     * 弹出提示
     * @param msg  提示信息
     * @param time  显示时间
     */
    fun showToast(msg: String?, time: Int, context: Context?) {
        if (mToast == null || mMsg != null && msg != mMsg) {
            mToast = Toast.makeText(context, msg, time)
            if (mToastView != null) {
                mToast!!.view = mToastView
                mToastView!!.setText(msg!!)
            } else {
                mToast!!.setText(msg)
            }
        } else {
            if (mToastView != null && mToast!!.view != mToastView) {
                mToast!!.view = mToastView
            }
            if (mToastView != null) {
                mToastView!!.setText(msg!!)
            } else {
                mToast!!.setText(msg)
            }
            mToast!!.duration = time
        }
        if (mToastGravity != -1) {
            mToast!!.setGravity(mToastGravity, 0, 0)
        }

        //不设置的话，最高显示到状态栏下面
        mToast!!.view?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        mToast!!.show()
    }

    /**
     * 弹出提示信息
     * @param msgId  提示信息id
     * @param time  显示时间
     */
    fun showToast(msgId: Int, time: Int, context: Context?) {
        showToast(context?.getString(msgId), time, context)
    }

    /**
     * 弹出短时间提示
     * @param msg  提示信息
     */
    fun showShortToast(msg: String, context: Context?) {
        showToast(msg, Toast.LENGTH_SHORT, context)
    }

    fun showShortToast(msgId:Int, context: Context?) {
        showToast(msgId, Toast.LENGTH_SHORT, context)
    }

    /**
     * 弹出长时间提示
     * @param msg  提示信息
     */
    fun showLongToast(msg: String, context: Context?) {
        showToast(msg, Toast.LENGTH_LONG, context)
    }

    /**
     * 关闭当前Toast
     */
    fun cancelCurrentToast() {
        if (mToast != null) {
            mToast!!.cancel()
        }
    }

    fun reToast(msg: String) {
        Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun reToast(msgId: Int) {
        Toast.makeText(appContext, msgId, Toast.LENGTH_SHORT).show()
    }

    fun setToastView(context: Context?) {
        mToastView = ToastView(context!!)
    }

    fun setToastGravity(gravity: Int) {
        mToastGravity = gravity
    }

    /**
     * 重置toast 信息
     */
    fun resetToast() {
        mToastView = null
        mToastGravity = -1
        mToast = null
    }
}