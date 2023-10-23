package com.blue.corelib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.blue.corelib.R;


public class ClearEditText extends AppCompatEditText {
    private final static int TYPE_NORMAL = -1;
    private final static int TYPE_CHINESE_NUM_LETTER = 0;
    private Drawable mDrawableRight;
    private int resIconClear;
    private int maxTextLen;//输入内容长度（正对字母、数字、中文）
    private boolean previousVisible = true;
    private boolean isHasFocus;
    private int myInputType = TYPE_NORMAL;//输入类型
    private boolean isAlwaysEmotion = false;
    private InputLengthListener inputLengthListener;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
//        this(context, attrs, android.R.attr.editTextStyle);
        super(context, attrs);
        init(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
        resIconClear = attributes.getResourceId(R.styleable.ClearEditText_icon_clear, R.mipmap.ic_et_clear_default);
        maxTextLen = attributes.getInteger(R.styleable.ClearEditText_maxTextLen, -1);
        myInputType = attributes.getInteger(R.styleable.ClearEditText_myInputType, TYPE_NORMAL);
        isAlwaysEmotion = attributes.getBoolean(R.styleable.ClearEditText_allowEmotion, false);
        //Returns drawables for the left, top, right, and bottom borders.
        Drawable[] drawables = this.getCompoundDrawables();
        // Drawable顺序左上右下，0123
        // 获取DrawRight内容
        mDrawableRight = drawables[2];
        if (mDrawableRight == null) {
            // 未设置默认DrawableRight
            setmDrawableRight();
        }
        if (TextUtils.isEmpty(getText().toString())) {
            setClearDrawableVisible(false);
        }
        //光标颜色修改
//        try {
//            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
//            f.setAccessible(true);
//            f.set(this, R.drawable.bg_et_cursor);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //设置EditText文字变化的监听
        this.setFilters(new InputFilter[]{getFilter()});
        this.addTextChangedListener(new TextWatcherImpl());
        this.setOnFocusChangeListener(new FocusChangeListenerImpl());
        attributes.recycle();
    }

    // 初始化DrawableRight
    public void setmDrawableRight() {
        int drawableId;
        // 通过状态设置DrawableRight的样式
        drawableId = resIconClear;
        // 初始化DrawableRight
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDrawableRight = getResources().getDrawable(drawableId, null);
        } else {
            mDrawableRight = getResources().getDrawable(drawableId);
        }
        // 设置Drawable大小和位置
        mDrawableRight.setBounds(0, 0, mDrawableRight.getIntrinsicWidth(), mDrawableRight.getIntrinsicHeight());
        // 将其添加到控件上
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mDrawableRight, getCompoundDrawables()[3]);
    }

    public void setTextLen(int len) {
        this.maxTextLen = len;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                boolean isClean = event.getX() > (getWidth() - getPaddingRight() - mDrawableRight.getIntrinsicWidth()) && isHasFocus;
                if (isClean) {
                    setText("");
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    private InputFilter getFilter() {
        InputFilter filter = null;
        filter = (source, start, end, dest, dstart, dend) -> {
            if (myInputType == TYPE_CHINESE_NUM_LETTER) {
                if (!DetectedDataValidation.isOnlyContainChineseNUMLetter(source.toString())) {
                    return "";
                }
                return null;
            } else {
                if (!isAlwaysEmotion && DetectedDataValidation.verifyValidStr(source.toString())) {//禁止输入表情符
                    return "";
                } else {
                    return null;
                }
            }
        };
        return filter;
    }

    //EditText 输入监听
    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            if (maxTextLen != -1) {
                setTextAndLength(s);
            }
            boolean isVisible = s.toString().length() >= 1 && isHasFocus;
            setClearDrawableVisible(isVisible);
            if (!isHasFocus) {
//                Selection.setSelection(getText(), s.length());
            }
            if (afterTextChangedListener != null) {
                afterTextChangedListener.afterTextChanged(s.toString());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(" ")) {
                String[] str = s.toString().split(" ");
                String str1 = "";
                for (int i = 0; i < str.length; i++) {
                    str1 += str[i];
                }
                setText(str1);
                setSelection(length());
            }
        }

    }

    /**
     * 设置输入框的限制
     *
     * @param s
     */
    public void setTextAndLength(Editable s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (DetectedDataValidation.isChina(String.valueOf(c))) {
                length += 2;
                if (length > maxTextLen) {//大于最大长度
                    s.delete(i, s.length());
                    length -= 2;
                }
            } else {
                length++;
                if (length > maxTextLen) {//大于最大长度
                    s.delete(i, s.length());
                    length--;
                }

            }

        }
        if (inputLengthListener != null) {
            inputLengthListener.length(length);
        }
    }


    //隐藏或者显示右边clean的图标
    protected void setClearDrawableVisible(boolean isVisible) {
        if (previousVisible != isVisible) {
            previousVisible = isVisible;
            Drawable rightDrawable;
            if (previousVisible) {
                rightDrawable = mDrawableRight;
            } else {
                rightDrawable = null;
            }
            //使用代码设置该控件left, top, right, and bottom处的图标
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                    rightDrawable, getCompoundDrawables()[3]);
        }
    }

    private class FocusChangeListenerImpl implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isHasFocus = hasFocus;
            if (isHasFocus) {
                boolean isVisible = getText().toString().length() >= 1;
                setClearDrawableVisible(isVisible);
            } else {
                setClearDrawableVisible(isHasFocus);
            }
        }
    }

    public interface InputLengthListener {
        void length(int l);
    }

    public void setInputLengthListener(InputLengthListener inputLengthListener) {
        this.inputLengthListener = inputLengthListener;
    }

    public interface AfterTextChangedListener {
        void afterTextChanged(String content);
    }

    private AfterTextChangedListener afterTextChangedListener;

    public void setAfterTextChangedListener(AfterTextChangedListener afterTextChangedListener) {
        this.afterTextChangedListener = afterTextChangedListener;
    }
}
