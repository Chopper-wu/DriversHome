package com.blue.corelib.utils.span;

import android.text.Layout.Alignment;
import android.text.style.AlignmentSpan.Standard;

import org.jetbrains.annotations.NotNull;

public final class AlignmentSpanBuilder implements SpanBuilder {
    private final Alignment alignment;

    @Override
    @NotNull
    public Object build() {
        return new Standard(this.alignment);
    }

    public AlignmentSpanBuilder(@NotNull Alignment alignment) {
        this.alignment = alignment;
    }
}
