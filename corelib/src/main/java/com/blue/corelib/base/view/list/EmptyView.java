package com.blue.corelib.base.view.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blue.corelib.R;
import com.jakewharton.rxbinding.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.blue.corelib.base.config.RegexConfig.SPACING_TIME;
import static com.blue.corelib.base.config.RegexConfig.SPACING_TIME_S;


/**
 * @Describe 空置页面或错误提示
 * @Author
 * @Date 2017/2/7
 * @Contact master. @gmail.com
 */
public class EmptyView extends LinearLayout {

    // view 当前状态
    /**
     * default
     */
    public static final int STATE_DEFAULT = 0;
    /**
     * net error
     */
    public static final int STATE_NETWORK_ERROR = 1;
    /**
     * loading
     */
    public static final int STATE_NETWORK_LOADING = 2;
    /**
     * no data and clickable is false
     */
    public static final int STATE_NODATA = 3;
    /**
     * no data and clickable is true
     */
    public static final int STATE_NODATA_ENABLE_CLICK = 4;
    /**
     * hide this view
     */
    public static final int STATE_HIDE_LAYOUT = 5;

    public ImageView mIvError;
    private TextView mTvError;
    private View mLlContent;
    private LinearLayout mLldata;
    private TextView mTvWaybill;


    private OnClickListener mOnClickListener;
    private int mErrorState;
    private boolean mClickEnable = true;

    private boolean mIsNeedClickLoadState = true;// 是否需要点击响应时自动 load 状态

    private Context mContext;

    public EmptyView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.view_empty, this);
        mLlContent = view.findViewById(R.id.ll_content);
        mLldata = view.findViewById(R.id.ll_data_empty);
        mTvWaybill = view.findViewById(R.id.tv_waybill_empty);
        mIvError = view.findViewById(R.id.errorIcon);
        mTvError = view.findViewById(R.id.tips);
        RxView.clicks(mLlContent)
                .throttleFirst(SPACING_TIME_S, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mClickEnable) {
                            if (mIsNeedClickLoadState) {
                                setErrorType(STATE_NETWORK_LOADING);
                            }
                            if (mOnClickListener != null) {
                                mOnClickListener.onClick(mLlContent);
                            }
                        }
                    }
                });
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            mErrorState = STATE_HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }


    /**
     * 设置错误提示信息内容
     *
     * @param msg 信息内容，not null
     */
    public void setErrorMessage(@NotNull String msg) {
        mTvError.setVisibility(VISIBLE);
        mTvError.setText(msg);
    }

    /**
     * 设置运单卡片的无任务
     *
     */
    public void setWaybillEmpty() {
        mTvWaybill.setVisibility(VISIBLE);
        mLldata.setVisibility(GONE);
    }
    /**
     * 设置运单卡片的无任务
     *
     */
    public void setWaybillEmptyTxt(@NotNull String msg) {
        mTvWaybill.setText(msg);
        setErrorMessage(msg);
    }
    /**
     * 设置提示图片
     *
     * @param resId 资源引用 id
     * @return true, 加载中
     */
    public void setErrorImag(int resId) {
        if (resId > 0) {
            mIvError.setVisibility(View.VISIBLE);
            mIvError.setImageResource(resId);
        }
    }

    /**
     * 设置当前状态
     *
     * @param type 当前状态类型
     */
    public void setErrorType(int type) {
        setVisibility(View.VISIBLE);
        switch (type) {
            case STATE_NETWORK_ERROR:
                mErrorState = STATE_NETWORK_ERROR;
                setErrorImag(R.mipmap.no_wifi_img);
                setErrorMessage("网络加载失败，请点击页面重试");
                mClickEnable = true;
                break;
            case STATE_NETWORK_LOADING:
                mErrorState = STATE_NETWORK_LOADING;
                mIvError.setVisibility(View.GONE);
               // ImageUtils.loadImageWithId(mIvError, R.drawable.roller_loading);
                setErrorMessage("");
                mClickEnable = false;
                break;
            case STATE_NODATA:
                mErrorState = STATE_NODATA;
                setErrorImag(R.mipmap.no_data_img);
                setErrorMessage("暂无内容");
                mClickEnable = true;
                break;
            case STATE_HIDE_LAYOUT:
                setVisibility(View.GONE);
                mIvError.setVisibility(View.GONE);
                break;
            case STATE_NODATA_ENABLE_CLICK:
                mErrorState = STATE_NODATA_ENABLE_CLICK;
                setErrorImag(R.mipmap.no_data_img);
                setErrorMessage("暂无内容");
                mClickEnable = true;
                break;
            default:
        }
    }


}

