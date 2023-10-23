package com.blue.corelib.utils.span;

import android.text.style.StyleSpan;

import org.jetbrains.annotations.NotNull;

public final class StyleSpanBuilder implements SpanBuilder {
   private final int style;

   @Override
   @NotNull
   public Object build() {
      return new StyleSpan(this.style);
   }

   public StyleSpanBuilder(int style) {
      this.style = style;
   }
}
