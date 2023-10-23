package com.blue.corelib.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.blue.corelib.R;
import com.blue.corelib.utils.RxjavaUtilsKt;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by chopper on 2020/4/30.
 * Describe:
 */
public class LoadingDialog extends Dialog {
    private LoadingDialog mDialog;
    private boolean isShowingDialog;
    private Disposable delayShowTimer;
    private Disposable delayDismissTimer;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        setDialog(false);
    }

    public LoadingDialog(@NonNull Context context, int themeResId, boolean cancelable) {
        super(context, themeResId);
        setDialog(cancelable);
    }

    public void setDialog(boolean cancelable) {
        mDialog = this;
        mDialog.setContentView(R.layout.dialog_loading);
        mDialog.setCancelable(cancelable);
        //解决状态栏字体变色问题
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void showDialog(@StringRes int msg) {
        if (getContext()==null){
            return;
        }
        isShowingDialog = true;
        delayShowTimer = Observable.timer(0, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (!mDialog.isShowing() && isShowingDialog) {
                        ImageView imgView = mDialog.findViewById(R.id.iv_animation);
                        TextView tipTextView = mDialog.findViewById(R.id.tv_dialog_msg);
                        setAnimation(imgView);
                        tipTextView.setText(msg);
                        mDialog.show();
                        delayDismiss();
                    }
                    RxjavaUtilsKt.cancelTimer(delayShowTimer);
                });
    }

    public void showDialog(String msg) {
        if (getContext()==null){
            return;
        }
        isShowingDialog = true;
        delayShowTimer = Observable.timer(0, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (!mDialog.isShowing() && isShowingDialog) {
                        ImageView imgView = mDialog.findViewById(R.id.iv_animation);
                        TextView tipTextView = mDialog.findViewById(R.id.tv_dialog_msg);
                        setAnimation(imgView);
                        tipTextView.setText(msg);
                        mDialog.show();
                        delayDismiss();
                    }
                    RxjavaUtilsKt.cancelTimer(delayShowTimer);
                });
    }
    public void showUpdateDialog(String msg) {
        if (getContext()==null){
            return;
        }
        ImageView imgView = mDialog.findViewById(R.id.iv_animation);
        TextView tipTextView = mDialog.findViewById(R.id.tv_dialog_msg);
        tipTextView.setText(msg);
        isShowingDialog = true;
        delayShowTimer = Observable.timer(0, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (!mDialog.isShowing() && isShowingDialog) {
                        setAnimation(imgView);
                        mDialog.show();
                        delayDismiss();
                    }
                    RxjavaUtilsKt.cancelTimer(delayShowTimer);
                });
    }
    /**
     * 设置旋转的动画
     */
    public void setAnimation(View view) {
        ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0, 360);
        mObjectAnimator.setDuration(1000);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator.start();
    }

    private void delayDismiss() {
        delayDismissTimer = Observable.timer(20, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    hideDialog();
                    RxjavaUtilsKt.cancelTimer(delayDismissTimer);
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    public void hideDialog() {
        isShowingDialog = false;
        if (mDialog != null && mDialog.isShowing()) {
            try {
                RxjavaUtilsKt.cancelTimer(delayDismissTimer);
                mDialog.dismiss();
            } catch (IllegalArgumentException exception) {

            }
        }
    }

}
