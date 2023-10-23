package com.blue.corelib.utils.span;

import android.text.style.LeadingMarginSpan.Standard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LeadingMarginSpanBuilder implements SpanBuilder {
   private final int everyOrFirst;
   private final Integer rest;

   @Override
   @NotNull
   public Object build() {
      return this.rest != null?new Standard(this.everyOrFirst, this.rest):new Standard(this.everyOrFirst);
   }

   public LeadingMarginSpanBuilder(int everyOrFirst, @Nullable Integer rest) {
      this.everyOrFirst = everyOrFirst;
      this.rest = rest;
   }
}
