package com.blue.corelib.utils.span;

import android.text.style.QuoteSpan;

import androidx.annotation.ColorInt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class QuoteSpanBuilder implements SpanBuilder {
   private final Integer color;

   @Override
   @NotNull
   public Object build() {
      return this.color == null?new QuoteSpan():new QuoteSpan(this.color);
   }

   public QuoteSpanBuilder(@ColorInt @Nullable Integer color) {
      this.color = color;
   }
}
