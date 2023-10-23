package com.blue.corelib.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatTextView
import com.blue.corelib.utils.TypeFaceUtils
import com.blue.corelib.utils.format

/**
 * 自定义字体样式+数字动画
 */
class CommonTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        try {
            typeface = TypeFaceUtils.getNumTypeface()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setNumberWithAnimation(from: Float, to: Float, animTime: Long = 300L, block: () -> Unit) {
        with(ValueAnimator.ofFloat(from, to)) {
            interpolator = LinearInterpolator()
            duration = animTime
            addUpdateListener { animation -> text = (animation.animatedValue as Float).format("0.#") }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    text = "$to"
                    block.invoke()
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
            start()
        }
    }

    fun setIntNumberWithAnimation(from: Int, to: Int, animTime: Long = 300L) {
        with(ValueAnimator.ofInt(from, to)) {
            interpolator = LinearInterpolator()
            duration = animTime
            addUpdateListener { animation -> text = "${animation.animatedValue as Int}" }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    text = "$to"
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
            start()
        }
    }
}