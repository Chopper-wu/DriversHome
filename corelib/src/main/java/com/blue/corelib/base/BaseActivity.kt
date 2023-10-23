package com.blue.corelib.base

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.blue.corelib.AppManager
import com.blue.corelib.R
import com.blue.corelib.view.LoadingDialog
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar

/**
 * 统一 Activity
 * 支持沉浸式模式
 */
open class BaseActivity : AppCompatActivity() {

    protected lateinit var context: Context
    private var loadingDialog: LoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.getInstance().addActivity(this)
        context = this
        if (isImmerse()) {
            immerse()
        }
    }

    private fun immerse() {
        immersionBar {
            // 透明状态栏
            transparentStatusBar()
            // 状态栏字体颜色
            statusBarDarkFont(statusBarDarkFont(), 0.2f)
            // 导航栏图标颜色
            navigationBarDarkIcon(navBarDarkIcon(), 0.2f)
        }
    }

    @JvmOverloads
    fun showLoadingDialog(msg: String = "") {
        showLoadingDialog(false, msg)
    }

    fun showLoadingDialog(cancelable: Boolean, msg: String = "") {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(this, R.style.LoadingDialog, cancelable)
        }
        if (msg.isNotEmpty()) {
            loadingDialog?.showDialog(msg)
        } else {
            loadingDialog?.showDialog(R.string.common_loading)
        }
    }

    fun dismissLoadingDialog() {
        loadingDialog?.hideDialog()
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

    /**
     * 偏移导航栏高度
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun offsetNavBar(view: View) {
        view.setPadding(
            view.paddingStart,
            view.paddingTop,
            view.paddingEnd,
            view.paddingBottom + ImmersionBar.getNavigationBarHeight(this)
        )
    }

    /**
     * 默认开启沉浸式模式，可以复写方法关闭
     */
    open fun isImmerse() = true

    /**
     * 默认偏移根布局
     */
    open fun autoOffsetView() = true

    /**
     * 状态栏字体黑色
     */
    open fun statusBarDarkFont() = true

    /**
     * 导航栏图标黑色
     */
    open fun navBarDarkIcon() = false

    // 判定是否需要隐藏
    fun isHideInput(v: View?, ev: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = (left + v.getWidth())
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // TODO Auto-generated method stub
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view != null && isHideInput(view, ev)) {
                HideSoftInput(view.windowToken)
                releaseFocus(ev)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 隐藏软键盘
    open fun HideSoftInput(token: IBinder?) {
        if (token != null) {
            val manager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(
                token,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun releaseFocus(event: MotionEvent) {
        // 点击其他地方失去焦点
        val view = currentFocus
        if (view != null && view is EditText) {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]
            if (event.rawX < x || event.rawX > x + view.getWidth() || event.rawY < y || event.rawY > y + view.getHeight()
            ) {
                view.clearFocus()
                try {
                    val root = view.getParent() as View
                    if (root != null) {
                        root.isFocusable = true
                        root.isFocusableInTouchMode = true
                        root.requestFocus()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        AppManager.getInstance().removeActivity(this)
    }
}