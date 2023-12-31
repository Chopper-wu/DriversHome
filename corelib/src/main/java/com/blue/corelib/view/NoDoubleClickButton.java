package com.blue.corelib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Author: Chopper
 * Date: 2016/4/22 14:24
 * <p/>
 * Description: 不允许短时间内双击响应
 */
@SuppressLint("AppCompatCustomView")
public class NoDoubleClickButton extends Button {
    private long previousTime;

    public NoDoubleClickButton(Context context) {
        super(context);
    }

    public NoDoubleClickButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoDoubleClickButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param event
     *         touch事件
     *
     * @return 是否消耗点击事件
     * true - 消耗点击事件
     * false - 不消耗点击事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                long currentTime = System.currentTimeMillis();
                if (currentTime - previousTime < 1000) {
                    return true;
                }
                previousTime = currentTime;
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

}
