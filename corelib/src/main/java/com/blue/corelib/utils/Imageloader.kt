package com.blue.corelib.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.blue.corelib.utils.transform.GlideCircleTransform
import com.blue.corelib.utils.transform.GlideRoundTransform
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

fun <T> ImageView.display(url: T?, placeholder: Drawable? = null) {
    Glide.with(this)
        .load(url)
        .placeholder(placeholder)
        .error(placeholder)
        .into(this)
}

fun <T> ImageView.displayCircle(
    url: T?,
    margin: Int = 0,
    marginColor: Int = 0,
    placeholder: Drawable? = null
) {
    Glide.with(this)
        .load(url)
        .placeholder(placeholder)
        .error(placeholder)
        .transform(GlideCircleTransform(margin, marginColor))
        .into(this)
}

fun <T> ImageView.displayRound(
    url: T?,
    radius: Float,
    placeholder: Drawable? = null
) {
    Glide.with(this)
        .load(url)
        .placeholder(placeholder)
        .error(placeholder)
        .transform(CenterCrop(), GlideRoundTransform(radius))
        .into(this)
}

