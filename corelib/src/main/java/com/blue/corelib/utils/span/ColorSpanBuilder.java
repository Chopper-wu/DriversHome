package com.blue.corelib.utils.span;

import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.ColorInt;

import org.jetbrains.annotations.NotNull;

public final class ColorSpanBuilder implements SpanBuilder {
   private final int type;
   private final int color;
   public static final int FOREGROUND = 0;
   public static final int BACKGROUND = 1;

   @Override
   @NotNull
   public Object build() {
      Object var;
      switch(this.type) {
      case 0:
         var = new ForegroundColorSpan(this.color);
         break;
      default:
         var = new BackgroundColorSpan(this.color);
      }

      return var;
   }

   public ColorSpanBuilder(int type, @ColorInt int color) {
      this.type = type;
      this.color = color;
   }
}
