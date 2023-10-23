package com.blue.corelib.utils.span;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public final class Spanner extends SpannableStringBuilder {


    public Spanner(@Nullable CharSequence text) {
        super(text);
    }

    public Spanner() {
        this("");
    }

    @Override
    @NotNull
    public Spanner append(@Nullable CharSequence text) {
        if (text != null) {
            super.append(text);
        }
        return this;
    }

    @Override
    @NotNull
    public Spanner append(@NotNull CharSequence text, int start, int end) {
        super.append(text, start, end);
        return this;
    }

    @Override
    @NotNull
    public Spanner append(char text) {
        super.append(text);
        return this;
    }

    @Override
    @NotNull
    public Spanner insert(int where, @Nullable CharSequence text) {
        if (text != null) {
            super.insert(where, text);
        }
        return this;
    }

    @Override
    @RequiresApi(api = 21)
    @NotNull
    public Spanner append(@Nullable CharSequence text, @Nullable Object what, int flags) {
        if (text != null) {
            super.append(text, what, flags);
        }
        return this;
    }

    @Override
    @NotNull
    public Spanner replace(int start, int end, @Nullable CharSequence replace) {
        if (replace != null) {
            super.replace(start, end, replace);
        }
        return this;
    }

    @Override
    @NotNull
    public Spanner replace(int start, int end, @Nullable CharSequence replace, int tbstart, int tbend) {
        if (replace != null) {
            super.replace(start, end, replace, tbstart, tbend);
        }
        return this;
    }

    @Override
    @NotNull
    public Spanner delete(int start, int end) {
        super.delete(start, end);
        return this;
    }


    @NotNull
    public final Spanner append(@Nullable CharSequence text, @NotNull Span... spans) {
        if (text != null) {
            int start = this.length();
            this.append(text);
            this.setSpans(start, this.length(), (Span[]) Arrays.copyOf(spans, spans.length));
        }
        return this;
    }

    @NotNull
    public final Spanner insert(int where, @NotNull CharSequence text, @NotNull Span... spans) {
        super.insert(where, text);
        this.setSpans(where, where + text.length(), (Span[]) Arrays.copyOf(spans, spans.length));
        return this;
    }

    @NotNull
    public final Spanner replace(int start, int end, @Nullable CharSequence text, @NotNull Span... spans) {
        if (text == null) {
            text = "";
        }
        super.replace(start, end, text);
        this.setSpans(start, start + text.length(), (Span[]) Arrays.copyOf(spans, spans.length));
        return this;
    }

    @NotNull
    public final Spanner replace(@NotNull CharSequence search, @NotNull CharSequence replace, @NotNull Span... spans) {
        if (length() <= 0) {
            return this;
        }
        int lastIndex = 0;
        while (true) {
            CharSequence source = subSequence(lastIndex, length());
            int start = TextUtils.indexOf(source, search);
            if (start == -1) {
                return this;
            }
            start += lastIndex;
            if (start + search.length() > length()) {
                return this;
            }
            lastIndex = start + replace.length();
            this.replace(start, start + search.length(), replace, (Span[]) Arrays.copyOf(spans, spans.length));
        }
    }

    @NotNull
    public final Spanner setSpans(int start, int end, @NotNull Span... spans) {

        for (int var5 = 0; var5 < spans.length; ++var5) {
            Span span = spans[var5];
            this.setSpan(span.build(), start, end, 0);
        }
        return this;
    }

    @NotNull
    public final Spanner span(@NotNull CharSequence search, @NotNull Span... spans) {
        if (TextUtils.isEmpty(search)) {
            this.setSpans(0, this.length(), (Span[]) Arrays.copyOf(spans, spans.length));
            return this;
        } else {
            int lastPos = -1;

            while (true) {
                lastPos = TextUtils.indexOf(this, search, lastPos + 1);
                if (lastPos == -1) {
                    return this;
                }

                this.setSpans(lastPos, lastPos + search.length(), (Span[]) Arrays.copyOf(spans, spans.length));
            }
        }
    }

    public char get(int var1) {
        return super.charAt(var1);
    }

    @Override
    public final char charAt(int var1) {
        return this.get(var1);
    }

    public int getLength() {
        return super.length();
    }

    @Override
    public final int length() {
        return this.getLength();
    }
}
