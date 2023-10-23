package com.blue.corelib.utils.span;

import android.text.style.BulletSpan;

import androidx.annotation.ColorInt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BulletSpanBuilder implements SpanBuilder {
    private final Integer gapWidth;
    private final Integer color;

    @Override
    @NotNull
    public Object build() {
        return this.gapWidth != null && this.color != null
                ? new BulletSpan(this.gapWidth, this.color)
                : (this.gapWidth != null
                ? new BulletSpan(this.gapWidth)
                : new BulletSpan());
    }

    public BulletSpanBuilder(@Nullable Integer gapWidth, @ColorInt @Nullable Integer color) {
        this.gapWidth = gapWidth;
        this.color = color;
    }
}
