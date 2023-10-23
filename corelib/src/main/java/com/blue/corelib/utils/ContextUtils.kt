package com.blue.corelib.utils

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference
import kotlin.math.ceil

val appContext: Context = ContextProvider.getContext()
val res: Resources = appContext.resources

val screenWidth
    get() = res.displayMetrics.widthPixels

val screenHeight
    get() = res.displayMetrics.heightPixels

inline val Number.dpF
    get() = toPxF()

inline val Number.dp
    get() = toPx()

fun Number.toPx() = toPxF().toInt()

fun Number.toPxF() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), res.displayMetrics)

fun Number.toDp() = toDpF().toInt()

fun Number.toDpF() = this.toFloat() * DisplayMetrics.DENSITY_DEFAULT / res.displayMetrics.densityDpi

fun getString(@StringRes res: Int, vararg obj: Any?) =
    ContextProvider.getContext().getString(res, *obj)

fun getDrawable(@DrawableRes res: Int) =
    ContextCompat.getDrawable(ContextProvider.getContext(), res)
fun getColor(@ColorRes res: Int) = ContextCompat.getColor(ContextProvider.getContext(), res)

/**
 * 采用弱引用形式持有context
 */
interface ContextProvider {
    companion object {
        internal var contextRef = WeakReference<Context?>(null)
        fun getContext() = contextRef.get() ?: throw NullPointerException()
    }
}

class ContextProviderImpl : ContentProvider() {

    override fun onCreate(): Boolean {
        ContextProvider.contextRef = WeakReference(context)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?) = null

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ) = null

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ) = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0

    override fun getType(uri: Uri) = null
}