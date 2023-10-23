package com.blue.corelib.adapter;

import android.content.Context;
import android.view.LayoutInflater;


import com.blue.corelib.adapter.base.BaseViewDelegate;
import com.blue.corelib.adapter.base.ViewHolder;

import java.util.List;

public abstract class CustomAdapter<T> extends MultiIeCardAdapter<T> {
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CustomAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new BaseViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, T lsatT, int position, int itmeCounts) {
                CustomAdapter.this.convert(holder, t, position);
            }


        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

}
