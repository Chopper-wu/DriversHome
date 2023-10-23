package com.blue.corelib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * created by chopper on 2022/7/17
 * Description: 带文本的进度条
 */
public class MyProgressBar extends ProgressBar {
    private String mText;
    private Paint mPaint;
    private int remain;

    public MyProgressBar(Context context) {
        super(context);
        initText();
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText();
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initText();
    }

    @Override
    public synchronized void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.mText, 0, this.mText.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.mText, x, y, this.mPaint);
    }

    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.WHITE);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextSize(34.f);
    }

    public void setText(int progress) {
        int i = (progress * 100) / this.getMax();
        //this.mText = String.valueOf(i) + "%("+getRemain()+" remaining)";
        this.mText = String.valueOf(i) ;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }
}

