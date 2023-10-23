package com.blue.corelib.utils.span;

import android.text.style.AbsoluteSizeSpan;

import org.jetbrains.annotations.NotNull;

public final class AbsoluteSizeSpanBuilder implements SpanBuilder {
   private final int size;
   private final boolean dip;

   @Override
   @NotNull
   public Object build() {
      return new AbsoluteSizeSpan(this.size, this.dip);
   }

   public AbsoluteSizeSpanBuilder(int size, boolean dip) {
      this.size = size;
      this.dip = dip;
   }
}
