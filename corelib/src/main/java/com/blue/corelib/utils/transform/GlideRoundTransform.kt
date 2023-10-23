package com.blue.corelib.utils.transform

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * 圆角矩形变换
 * 1、支持自定义圆角
 */
class GlideRoundTransform(
    var radius: Float = 0f
) : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("javaClass.canonicalName$radius".toByteArray(Charset.defaultCharset()))
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val result = pool.get(width, height, Bitmap.Config.ARGB_8888)

        val paint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
        val canvas = Canvas(result)
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint)

        return result
    }

}