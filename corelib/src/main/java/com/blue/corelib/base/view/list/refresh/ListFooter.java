package com.blue.corelib.base.view.list.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.blue.corelib.R;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;


public class ListFooter extends RelativeLayout implements RefreshFooter {

    private TextView mTvtip;


    public ListFooter(Context context) {
        this(context, null);
    }

    public ListFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTvtip = new TextView(getContext());
        mTvtip.setCompoundDrawables(getCompoundDrawables(R.drawable.frame_loading_grey), null, null, null);
        mTvtip.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.dp_5));
        mTvtip.setText("加载更多");
        mTvtip.setTextColor(Color.parseColor("#b3b3b3"));
        mTvtip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        mTvtip.setGravity(Gravity.CENTER);
        addView(mTvtip);
        LayoutParams layoutParams = (LayoutParams) mTvtip.getLayoutParams();
        layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.dp_45);
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    }

    /**
     * Textview或者button设置Drawable
     */
    private Drawable getCompoundDrawables(int imgRsID) {
        if (imgRsID == 0) {
            return null;
        }
        Drawable drawable = ContextCompat.getDrawable(getContext(), imgRsID);
        if (drawable == null) {
            return null;
        }
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {
        setBackgroundColor(Color.TRANSPARENT);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        AnimationDrawable background = (AnimationDrawable) mTvtip.getCompoundDrawables()[0];
        if (background != null && !background.isRunning()) {
            background.start();
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        if (success) {
            // 刷新成功
        } else {
            // 刷新失败
        }
        AnimationDrawable background = (AnimationDrawable) mTvtip.getCompoundDrawables()[0];
        if (background != null && background.isRunning()) {
            background.stop();
        }
        return 0;
    }

    public void setTvtip(String tip) {
        AnimationDrawable background = (AnimationDrawable) mTvtip.getCompoundDrawables()[0];
        if (background != null && background.isRunning()) {
            background.stop();
        }
        mTvtip.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        mTvtip.setText(tip);
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }
}
