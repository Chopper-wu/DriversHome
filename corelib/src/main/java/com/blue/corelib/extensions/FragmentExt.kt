@file:JvmName("FragmentExt")

package com.blue.corelib.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * 替换fragment
 */
fun FragmentActivity.replaceFragment(containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(containerViewId, fragment)
        .commitAllowingStateLoss()
}

fun Fragment.replaceFragment(containerViewId: Int, fragment: Fragment) {
    childFragmentManager.beginTransaction()
        .replace(containerViewId, fragment)
        .commitAllowingStateLoss()
}

/**
 * 添加fragment
 */
fun FragmentActivity.addFragment(containerViewId: Int, fragment: Fragment, tag: String) {
    supportFragmentManager.beginTransaction()
        .add(containerViewId, fragment, tag)
        .commitAllowingStateLoss()
}

fun Fragment.addFragment(containerViewId: Int, fragment: Fragment, tag: String) {
    childFragmentManager.beginTransaction()
        .add(containerViewId, fragment, tag)
        .commitAllowingStateLoss()
}

/**
 * 隐藏fragment
 */
fun FragmentActivity.hideFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .hide(fragment)
        .commitAllowingStateLoss()
}

fun Fragment.hideFragment(fragment: Fragment) {
    childFragmentManager.beginTransaction()
        .hide(fragment)
        .commitAllowingStateLoss()
}

/**
 * 显示fragment
 */
fun FragmentActivity.showFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .show(fragment)
        .commitAllowingStateLoss()
}

fun Fragment.showFragment(fragment: Fragment) {
    childFragmentManager.beginTransaction()
        .show(fragment)
        .commitAllowingStateLoss()
}

/**
 * 查找fragment
 */
fun FragmentActivity.findFragmentByTag(tag: String): Fragment? =
    supportFragmentManager.findFragmentByTag(tag)

fun Fragment.findFragmentByTag(tag: String): Fragment? =
    childFragmentManager.findFragmentByTag(tag)

/**
 * 查找fragment
 */
fun FragmentActivity.findFragmentById(id: Int): Fragment? =
    supportFragmentManager.findFragmentById(id)

fun Fragment.findFragmentById(id: Int): Fragment? =
    childFragmentManager.findFragmentById(id)
