package com.blue.corelib.utils.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.ColorInt;

import org.jetbrains.annotations.NotNull;

public final class ClickSpanBuilder implements SpanBuilder {
    private final OnClickListener clickListener;
    private @ColorInt
    int textColor = 0xff00bc71;

    @Override
    @NotNull
    public Object build() {
        return new EasyClickableSpan(this.clickListener, textColor);
    }

    public ClickSpanBuilder(@NotNull OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ClickSpanBuilder color(@ColorInt int color) {
        textColor = color;
        return this;
    }

    private static final class EasyClickableSpan extends ClickableSpan {
        private final OnClickListener clickListener;
        private int textColor;

        @Override
        public void onClick(@NotNull View widget) {
            this.clickListener.onClick(widget);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(textColor);
            ds.linkColor = textColor;
            ds.setUnderlineText(false);
        }

        public EasyClickableSpan(@NotNull OnClickListener clickListener, int textColor) {
            this.clickListener = clickListener;
            this.textColor = textColor;
        }
    }
}
