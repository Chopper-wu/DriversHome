package com.blue.corelib.utils.span;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.style.DrawableMarginSpan;
import android.text.style.IconMarginSpan;
import android.text.style.ParagraphStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ImageSpanBuilder implements SpanBuilder {
   private final Drawable drawable;
   private final Bitmap bitmap;
   private final Integer pad;

   @Override
   @NotNull
   public Object build() {
      ParagraphStyle style;
      if(this.drawable != null && this.pad != null) {
         style = new DrawableMarginSpan(this.drawable, this.pad);
      } else if(this.drawable != null) {
         style = new DrawableMarginSpan(this.drawable);
      } else {
         IconMarginSpan span;
         if(this.bitmap != null && this.pad != null) {
            span = new IconMarginSpan(this.bitmap, this.pad);
         } else {
            span = new IconMarginSpan(this.bitmap);
         }
         style = span;
      }
      return style;
   }

   public ImageSpanBuilder(@Nullable Drawable drawable, @Nullable Bitmap bitmap, @Nullable Integer pad) {
      this.drawable = drawable;
      this.bitmap = bitmap;
      this.pad = pad;
   }
}
