package com.newcw.component.manager

import android.app.Activity
import android.content.Context
import android.os.Process
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blue.corelib.AppManager
import com.blue.corelib.R
import com.blue.corelib.adapter.ItemOneClickListener
import com.blue.corelib.utils.*
import com.blue.xrouter.XRouter
import com.orhanobut.dialogplus.DialogPlus

/**
 *created by chopper on 2021/8/16
 *Description:
 */
class SystemManager {
    companion object {
        val INSTANCES: SystemManager by lazy {
            SystemManager()
        }
    }


    /**
     * 登录失效弹窗提示
     */
    fun TokenFailureDialog(context: Activity, msg: String) {
        if (context == null) {
            return
        }
        val inflateView: View =
            LayoutInflater.from(context).inflate(R.layout.public_confirm_dialog, null)
        inflateView.findViewById<TextView>(R.id.title)
            .setText(if (msg.isNullOrBlank()) "您的账户已在其他设备登录,请重新登录" else msg)
        inflateView.findViewById<TextView>(R.id.content).visibility = View.GONE
        inflateView.findViewById<TextView>(R.id.cancelBtn).visibility = View.GONE
        inflateView.findViewById<TextView>(R.id.postiveBtn).setText("重新登录")
        DialogPlus.newDialog(context)
            .setGravity(Gravity.CENTER)
            .setCancelable(false)
            .setContentHolder(com.orhanobut.dialogplus.ViewHolder(inflateView))
            .setMargin(20.toPx(), 0, 20.toPx(), 0)
            .setPadding(0, 0, 0, 0)
            .setContentBackgroundResource(com.blue.corelib.R.drawable.common_white_bg)
            .setOnClickListener { dialog, v ->
                when (v.id) {
                    R.id.cancelBtn -> {
                        dialog.dismiss()
                        exitApp(context)
                    }

                    R.id.postiveBtn -> {
                        dialog.dismiss()
                        AppManager.getInstance().exitUser(context)
                    }
                }
            }
            .create().show()
    }
    /**
     * 单点登录弹窗提示
     */
    fun showSingleLogin(context: Activity, msg: String) {
        if (context == null) {
            return
        }
        val inflateView: View =
            LayoutInflater.from(context).inflate(R.layout.single_login_dialog, null)
        DialogPlus.newDialog(context)
            .setGravity(Gravity.CENTER)
            .setCancelable(false)
            .setContentHolder(com.orhanobut.dialogplus.ViewHolder(inflateView))
            .setMargin(20.toPx(), 0, 20.toPx(), 0)
            .setPadding(0, 0, 0, 0)
            .setContentBackgroundResource(com.blue.corelib.R.drawable.common_white_bg)
            .setOnClickListener { dialog, v ->
                when (v.id) {
                    R.id.postiveBtn -> {
                        dialog.dismiss()
                        AppManager.getInstance().exitUser(context)
                    }
                }
            }
            .create().show()
    }

    /**
     * 文案提示
     */
    fun showSureAccount(context: Activity, msg: String) {
        if (context == null) {
            return
        }
        var title = msg
        var content = ""
        msg?.let {
            if (it.indexOf("&") != -1) {
                title = it.substring(0, it.indexOf("&"))
                content = it.substring(it.indexOf("&") + 1, it.length)
            }
        }
        val inflateView: View =
            LayoutInflater.from(context).inflate(R.layout.public_confirm_dialog, null)
        inflateView.findViewById<TextView>(R.id.title)
            .setText(title)
        inflateView.findViewById<TextView>(R.id.content)
            .setText(content)
        inflateView.findViewById<TextView>(R.id.content).visibility =
            if (content.isNullOrEmpty()) View.GONE else View.VISIBLE
        inflateView.findViewById<TextView>(R.id.cancelBtn).visibility = View.GONE
        inflateView.findViewById<TextView>(R.id.postiveBtn)
            .setText("确定")
        DialogPlus.newDialog(context)
            .setGravity(Gravity.CENTER)
            .setCancelable(false)
            .setContentHolder(com.orhanobut.dialogplus.ViewHolder(inflateView))
            .setMargin(20.toPx(), 0, 20.toPx(), 0)
            .setPadding(0, 0, 0, 0)
            .setContentBackgroundResource(com.blue.corelib.R.color.transparent)
            .setOnClickListener { dialog, v ->
                when (v.id) {
                    R.id.cancelBtn -> {
                        dialog.dismiss()
                    }

                    R.id.postiveBtn -> {
                        dialog.dismiss()
                    }
                }
            }
            .create().show()
    }


    fun showDialogSureInfo(
        context: Activity,
        msg: String,
        callBack: ItemOneClickListener<Int>?
    ) {
        showSureInfo(context, msg, null, "确定", true, null, callBack)
    }

    fun showDialogSureInfo(
        context: Activity,
        msg: String,
        postiveStr: String? = "确定",
        callBack: ItemOneClickListener<Int>?
    ) {
        showSureInfo(context, msg, null, postiveStr, true, null, callBack)
    }

    fun showDialogSureInfo(
        context: Activity,
        msg: String,
        postiveStr: String? = "确定",
        cancelFlag: Boolean,
        callBack: ItemOneClickListener<Int>?
    ) {
        showSureInfo(context, msg, null, postiveStr, cancelFlag, null, callBack)
    }

    fun showDialogSureInfo(
        context: Activity,
        msg: String,
        cancelStr: String? = "取消",
        postiveStr: String? = "确定",
        postBack: ItemOneClickListener<Int>?
    ) {
        showSureInfo(context, msg, cancelStr, postiveStr, true, null, postBack)
    }

    fun showDialogSureInfo(
        context: Activity,
        msg: String,
        cancelStr: String? = "取消",
        postiveStr: String? = "确定",
        cancelBack: ItemOneClickListener<Int>?,
        postBack: ItemOneClickListener<Int>?
    ) {
        showSureInfo(context, msg, cancelStr, postiveStr, true, cancelBack, postBack)
    }

    /**
     * 文案提示
     */
    fun showSureInfo(
        context: Activity,
        msg: String,
        cancelStr: String? = "取消",
        postiveStr: String? = "确定",
        cancelFlag: Boolean,
        cancelBack: ItemOneClickListener<Int>?,
        postBack: ItemOneClickListener<Int>?
    ) {
        if (context == null) {
            return
        }
        var title = msg
        var content = ""
        msg?.let {
            if (it.indexOf("&") != -1) {
                title = it.substring(0, it.indexOf("&"))
                content = it.substring(it.indexOf("&") + 1, it.length)
            }
        }
        val inflateView: View =
            LayoutInflater.from(context).inflate(R.layout.public_confirm_dialog, null)
        inflateView.findViewById<TextView>(R.id.title)
            .setText(title)
        inflateView.findViewById<TextView>(R.id.content)
            .setText(content)
        inflateView.findViewById<TextView>(R.id.content).visibility =
            if (content.isNullOrEmpty()) View.GONE else View.VISIBLE
        inflateView.findViewById<TextView>(R.id.cancelBtn).visibility =
            if (cancelFlag) View.VISIBLE else View.GONE
        inflateView.findViewById<TextView>(R.id.cancelBtn).setText(cancelStr ?: "取消")
        inflateView.findViewById<TextView>(R.id.postiveBtn).setText(postiveStr ?: "确定")
        DialogPlus.newDialog(context)
            .setGravity(Gravity.CENTER)
            .setCancelable(false)
            .setContentHolder(com.orhanobut.dialogplus.ViewHolder(inflateView))
            .setMargin(20.toPx(), 0, 20.toPx(), 0)
            .setPadding(0, 0, 0, 0)
            .setContentBackgroundResource(com.blue.corelib.R.color.transparent)
            .setOnClickListener { dialog, v ->
                when (v.id) {
                    R.id.cancelBtn -> {
                        dialog.dismiss()
                        cancelBack?.onClick(1)
                    }

                    R.id.postiveBtn -> {
                        dialog.dismiss()
                        postBack?.onClick(1)
                    }
                }
            }
            .create().show()
    }

    fun exitApp(context: Context, exitType: Int? = 0) {
        context?.let {
            //MobclickAgent.onKillProcess(it)
        }
        //退出程序
        AppManager.getInstance().finishAllActivity()
        // 杀掉当前进程
        Process.killProcess(Process.myPid())
        // 正常退出
        System.exit(exitType ?: 0)
    }
}