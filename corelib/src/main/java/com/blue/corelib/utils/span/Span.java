package com.blue.corelib.utils.span;

import org.jetbrains.annotations.NotNull;

public final class Span {
   private final SpanBuilder builder;

   public final Object build() {
      return this.builder.build();
   }

   public Span(@NotNull SpanBuilder builder) {
      this.builder = builder;
   }
}
