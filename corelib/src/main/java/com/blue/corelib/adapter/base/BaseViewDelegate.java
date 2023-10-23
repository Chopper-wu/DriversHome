package com.blue.corelib.adapter.base;

public interface BaseViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, T lastT, int position, int itemCounts);

}
