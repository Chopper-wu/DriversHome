package com.blue.corelib.utils.span;

import android.graphics.Color;
import android.text.Layout;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.blue.corelib.utils.ContextUtilsKt;

/**
 * @author zhaoyiding
 */
public class ColorLinkMovementMethod extends ScrollingMovementMethod {
    private static final int CLICK = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private final int mTouchSlop;

    private @ColorInt
    int normalColor = Color.BLACK;
    private @ColorInt
    int pressedColor = Color.BLACK;

    public ColorLinkMovementMethod(int normalColor, int pressedColor) {
        this.normalColor = normalColor;
        this.pressedColor = pressedColor;
        final ViewConfiguration configuration = ViewConfiguration.get(ContextUtilsKt.getAppContext());
        mTouchSlop = (int) (configuration.getScaledTouchSlop() * 2 / 3.F);
    }

    @Override
    public boolean canSelectArbitrarily() {
        return true;
    }

    @Override
    protected boolean handleMovementKey(TextView widget, Spannable buffer, int keyCode,
                                        int movementMetaState, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getRepeatCount() == 0 && action(CLICK, widget, buffer)) {
                        return true;
                    }
                }
                break;
            default:
                break;
        }
        return super.handleMovementKey(widget, buffer, keyCode, movementMetaState, event);
    }

    @Override
    protected boolean up(TextView widget, Spannable buffer) {
        return action(UP, widget, buffer) || super.up(widget, buffer);
    }

    @Override
    protected boolean down(TextView widget, Spannable buffer) {
        return action(DOWN, widget, buffer) || super.down(widget, buffer);
    }

    @Override
    protected boolean left(TextView widget, Spannable buffer) {
        return action(UP, widget, buffer) || super.left(widget, buffer);
    }

    @Override
    protected boolean right(TextView widget, Spannable buffer) {
        return action(DOWN, widget, buffer) || super.right(widget, buffer);
    }

    private boolean action(int what, TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();

        int padding = widget.getTotalPaddingTop() +
                widget.getTotalPaddingBottom();
        int areaTop = widget.getScrollY();
        int areaBot = areaTop + widget.getHeight() - padding;

        int lineTop = layout.getLineForVertical(areaTop);
        int lineBot = layout.getLineForVertical(areaBot);

        int first = layout.getLineStart(lineTop);
        int last = layout.getLineEnd(lineBot);

        ClickableSpan[] candidates = buffer.getSpans(first, last, ClickableSpan.class);

        int a = Selection.getSelectionStart(buffer);
        int b = Selection.getSelectionEnd(buffer);

        int selStart = Math.min(a, b);
        int selEnd = Math.max(a, b);

        if (selStart < 0) {
            if (buffer.getSpanStart(FROM_BELOW) >= 0) {
                selStart = selEnd = buffer.length();
            }
        }

        if (selStart > last) {
            selStart = selEnd = Integer.MAX_VALUE;
        }
        if (selEnd < first) {
            selStart = selEnd = -1;
        }

        switch (what) {
            case CLICK:
                if (selStart == selEnd) {
                    return false;
                }

                ClickableSpan[] link = buffer.getSpans(selStart, selEnd, ClickableSpan.class);

                if (link.length != 1) {
                    return false;
                }

                link[0].onClick(widget);
                break;

            case UP:
                int bestStart, bestEnd;

                bestStart = -1;
                bestEnd = -1;

                for (int i = 0; i < candidates.length; i++) {
                    int end = buffer.getSpanEnd(candidates[i]);

                    if (end < selEnd || selStart == selEnd) {
                        if (end > bestEnd) {
                            bestStart = buffer.getSpanStart(candidates[i]);
                            bestEnd = end;
                        }
                    }
                }

                if (bestStart >= 0) {
                    Selection.setSelection(buffer, bestEnd, bestStart);
                    return true;
                }

                break;

            case DOWN:
                bestStart = Integer.MAX_VALUE;
                bestEnd = Integer.MAX_VALUE;

                for (int i = 0; i < candidates.length; i++) {
                    int start = buffer.getSpanStart(candidates[i]);

                    if (start > selStart || selStart == selEnd) {
                        if (start < bestStart) {
                            bestStart = start;
                            bestEnd = buffer.getSpanEnd(candidates[i]);
                        }
                    }
                }

                if (bestEnd < Integer.MAX_VALUE) {
                    Selection.setSelection(buffer, bestStart, bestEnd);
                    return true;
                }

                break;
            default:
                break;
        }

        return false;
    }


    private int lastX = -1, lastY = -1;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        int action = event.getAction();

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (action == MotionEvent.ACTION_MOVE) {
            if (lastX == -1 || lastY == -1) {
                lastX = x;
                lastY = y;
                return super.onTouchEvent(widget, buffer, event);
            }

            if (Math.abs(x - lastX) < mTouchSlop
                    && Math.abs(y - lastY) < mTouchSlop) {
                return super.onTouchEvent(widget, buffer, event);
            }
        } else if (action == MotionEvent.ACTION_DOWN) {
            lastX = x;
            lastY = y;
        } else {
            lastX = -1;
            lastY = -1;
        }

        x -= widget.getTotalPaddingLeft();
        y -= widget.getTotalPaddingTop();

        x += widget.getScrollX();
        y += widget.getScrollY();

        Layout layout = widget.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        ClickableSpan[] links = buffer.getSpans(off, off, ClickableSpan.class);

        if (links.length != 0) {
            if (action == MotionEvent.ACTION_UP) {
                links[0].onClick(widget);

                buffer.setSpan(new ForegroundColorSpan(normalColor), buffer.getSpanStart(links[0]), buffer.getSpanEnd(links[0]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                Selection.removeSelection(buffer);
            } else if (action == MotionEvent.ACTION_DOWN) {
                buffer.setSpan(new ForegroundColorSpan(pressedColor), buffer.getSpanStart(links[0]), buffer.getSpanEnd(links[0]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                Selection.setSelection(buffer,
                        buffer.getSpanStart(links[0]),
                        buffer.getSpanEnd(links[0]));
            } else {
                buffer.setSpan(new ForegroundColorSpan(normalColor), buffer.getSpanStart(links[0]), buffer.getSpanEnd(links[0]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                Selection.removeSelection(buffer);
            }
            return true;
        } else {
            Selection.removeSelection(buffer);
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    @Override
    public void initialize(TextView widget, Spannable text) {
        widget.setHighlightColor(0x00000000);
        Selection.removeSelection(text);
        text.removeSpan(FROM_BELOW);
    }

    @Override
    public void onTakeFocus(TextView view, Spannable text, int dir) {
        Selection.removeSelection(text);

        if ((dir & View.FOCUS_BACKWARD) != 0) {
            text.setSpan(FROM_BELOW, 0, 0, Spannable.SPAN_POINT_POINT);
        } else {
            text.removeSpan(FROM_BELOW);
        }
    }

    private static Object FROM_BELOW = new NoCopySpan.Concrete();
}