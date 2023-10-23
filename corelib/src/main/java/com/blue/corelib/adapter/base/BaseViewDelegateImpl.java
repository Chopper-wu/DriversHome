package com.blue.corelib.adapter.base;


import java.util.List;

public interface BaseViewDelegateImpl<T> extends BaseViewDelegate<T> {
    void convert(ViewHolder holder, T t, T lastT, int position, int itemCounts, List<Object> payloads);

}
