package com.blue.corelib.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar

/**
 * 统一 DialogFragment
 * 1.支持沉浸式模式
 * 2.修复系统自动恢复时getDialog()空指针异常
 * 3.修复Activity即将销毁时show()调用在onSaveInstanceState()之后
 */
open class BaseDialogFragment : DialogFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isImmerse()) {
            immerse()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (dialog == null) {
            showsDialog = false
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        manager.beginTransaction().also {
            it.add(this, tag)
            it.commitAllowingStateLoss()
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

    /**
     * 偏移状态栏高度
     */
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
    protected fun isImmerse() = false

    /**
     * 默认偏移根布局
     */
    open fun autoOffsetView() = true

    /**
     * 状态栏字体黑色
     */
    protected fun statusBarDarkFont() = true

    /**
     * 导航栏图标黑色
     */
    protected fun navBarDarkIcon() = true
}