package com.blue.corelib.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * 加载圆形图片
 */
@BindingAdapter(
    value = ["circleImg", "circleImgPlace", "circleImgMargin", "circleImgMarginColor"],
    requireAll = false
)
fun setCircleImg(
    imgView: ImageView,
    url: Any?,
    def: Drawable?,
    margin: Int,
    margin_Color: Int
) {
    imgView.displayCircle(url, margin, margin_Color, def)
}

/**
 * 加载方形图片
 */
@BindingAdapter(value = ["normalImg", "normalImgPlace"], requireAll = false)
fun setNormalImg(
    imgView: ImageView,
    url: Any?,
    def: Drawable?
) {
    imgView.display(url, def)
}

/**
 * 加载圆角图片
 *
 * @param imgView
 * @param url
 */
@BindingAdapter(
    value = ["roundImg", "roundImgPlace", "roundImgRadius"],
    requireAll = false
)
fun setRoundImg(
    imgView: ImageView,
    url: Any?,
    def: Drawable?,
    radius: Int
) {
    imgView.displayRound(url, radius.toFloat(), def)
}

/**
 * view显示或者隐藏
 */
@BindingAdapter(value = ["visible", "invisible"], requireAll = false)
fun setVisible(
    view: View,
    visible: Boolean,
    invisible: Boolean
) {
    view.visibility =
        if (visible) View.VISIBLE else if (invisible) View.INVISIBLE else View.GONE
}

/**
 * 根据字符串控制view显示或者隐藏
 */
@BindingAdapter(value = ["visibleTxt", "invisible"], requireAll = false)
fun setVisibleTxt(view: View, visibleTxt: String?, invisible: Boolean) {
    view.visibility = if (visibleTxt.isNullOrEmpty()) {
        if (invisible) {
            View.INVISIBLE
        } else {
            View.GONE
        }
    } else {
        View.VISIBLE
    }
}