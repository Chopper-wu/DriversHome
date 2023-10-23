package com.blue.corelib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by Chopper on 2021/3/31
 * desc :
 */
public class PwdEditTest extends AppCompatEditText {
    private static final String TAG = "MyEditTest";
    private int width;
    private int height;
    private int divideLineWStartX;
    private int firstCircleW;
    private int maxCount = 6;
    private int inputLength;
    private Paint pLine;
    private Paint pCircle;
    private Context context;

    public PwdEditTest(Context context) {
        this(context, null);
    }

    public PwdEditTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setBackgroundColor(Color.TRANSPARENT);//========================>>1
        this.setCursorVisible(false);//========================================>>2
        initPaint();

    }

    private void initPaint() {
        pLine = new Paint();
        pLine.setColor(Color.parseColor("#666666"));
        pLine.setAntiAlias(true);
        pLine.setStyle(Paint.Style.STROKE);

        pCircle = new Paint();
        pCircle.setColor(Color.parseColor("#333333"));
        pCircle.setAntiAlias(true);
        pCircle.setStyle(Paint.Style.FILL);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(length());
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        //第一条竖线 x位置
        divideLineWStartX = w / maxCount;
        //第一个圆x 位置
        firstCircleW = divideLineWStartX / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);=======================================>>3
        drawRect(canvas);
        drawLine(canvas);
        drawCircle(canvas);
    }

    private void drawLine(Canvas canvas) {
  /*      //通过循环画出每个分割线
        if (getEditableText().length() > 0 && pLine != null) {
            pLine.setColor(Color.parseColor("#4C69FF"));
        } else {
            pLine.setColor(Color.parseColor("#666666"));
        }*/
        for (int i = 0; i < maxCount - 1; i++) {
            canvas.drawLine((i + 1) * divideLineWStartX,
                    0,
                    (i + 1) * divideLineWStartX,
                    height,
                    pLine);
        }

    }

    private void drawCircle(Canvas canvas) {
        for (int i = 0; i < inputLength; i++) {
            canvas.drawCircle(firstCircleW + i * 2 * firstCircleW,
                    height / 2,
                    dp2px(context, 5),
                    pCircle);
        }
    }

    private void drawRect(Canvas canvas) {
        canvas.drawRoundRect(new RectF(0, 0, width, height), dp2px(context, 2), dp2px(context, 2), pLine);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        inputLength = text.length();
        if (inputLength >= 6) {
            //this.setEnabled(false);
            if (backData != null)
                backData.onDataBack(text.toString());
            hideSoftInput();
        }
        invalidate();
    }


    public void setBackData(OnBackData backData) {
        this.backData = backData;
    }

    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(this, 0);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private OnBackData backData;

    public interface OnBackData {
        /**
         * 6位数字密码
         *
         * @param pwd
         */
        void onDataBack(String pwd);
    }
}