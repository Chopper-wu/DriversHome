package com.blue.corelib.utils.transform

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest


/**
 * 圆形变换
 * 1、支持自定义边框宽度
 * 2、支持自定义边框颜色
 */
class GlideCircleTransform(
    var margin: Int = 0,
    val marginColor: Int = 0
) : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("javaClass.canonicalName$margin-$marginColor".toByteArray(Charset.defaultCharset()))
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val size = Math.min(toTransform.width, toTransform.height)

        val width = (toTransform.width - size) / 2
        val height = (toTransform.height - size) / 2

        var result = pool.get(size, size, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(result)
        val bitmapShader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        if (width != 0 || height != 0) {
            // source isn't square, move viewport to center
            val matrix = Matrix()
            matrix.setTranslate((-width).toFloat(), (-height).toFloat())
            bitmapShader.setLocalMatrix(matrix)
        }
        val r = size / 2f
        val paint = Paint().apply {
            isAntiAlias = true
            shader = bitmapShader
        }
        if (margin > 0 && marginColor != 0) {
            val marginPaint = Paint().apply {
                isAntiAlias = true
                color = marginColor
                style = Paint.Style.FILL
            }
            canvas.drawCircle(r, r, r, marginPaint)
        }
        canvas.drawCircle(r, r, if (r - margin < 0) r else r - margin, paint)

        return result
    }

}