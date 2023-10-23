package com.blue.corelib.adapter.base;
import androidx.collection.SparseArrayCompat;

import java.util.List;

public class BaseViewDelegateController<T>
{
    SparseArrayCompat<BaseViewDelegate<T>> delegates = new SparseArrayCompat();

    public int getItemViewDelegateCount()
    {
        return delegates.size();
    }

    public BaseViewDelegateController<T> addDelegate(BaseViewDelegate<T> delegate)
    {
        int viewType = delegates.size();
        if (delegate != null)
        {
            delegates.put(viewType, delegate);
            viewType++;
        }
        return this;
    }

    public BaseViewDelegateController<T> addDelegate(int viewType, BaseViewDelegate<T> delegate)
    {
        if (delegates.get(viewType) != null)
        {
            throw new IllegalArgumentException(
                    "An BaseViewDelegate is already registered for the viewType = "
                            + viewType
                            + ". Already registered BaseViewDelegate is "
                            + delegates.get(viewType));
        }
        delegates.put(viewType, delegate);
        return this;
    }

    public BaseViewDelegateController<T> removeDelegate(BaseViewDelegate<T> delegate)
    {
        if (delegate == null)
        {
            throw new NullPointerException("BaseViewDelegate is null");
        }
        int indexToRemove = delegates.indexOfValue(delegate);

        if (indexToRemove >= 0)
        {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public BaseViewDelegateController<T> removeDelegate(int itemType)
    {
        int indexToRemove = delegates.indexOfKey(itemType);

        if (indexToRemove >= 0)
        {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public int getItemViewType(T item, int position)
    {
        int delegatesCount = delegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--)
        {
            BaseViewDelegate<T> delegate = delegates.valueAt(i);
            if (delegate.isForViewType( item, position))
            {
                return delegates.keyAt(i);
            }
        }
        throw new IllegalArgumentException(
                "No BaseViewDelegate added that matches position=" + position + " in data source");
    }

    public void convert(ViewHolder holder, T item, T lastItem,int position,int itemCounts)
    {
        int delegatesCount = delegates.size();
        for (int i = 0; i < delegatesCount; i++)
        {
            BaseViewDelegate<T> delegate = delegates.valueAt(i);

            if (delegate.isForViewType( item, position))
            {
                delegate.convert(holder, item,lastItem, position,itemCounts);
                return;
            }
        }
        throw new IllegalArgumentException(
                "No BaseViewDelegateController added that matches position=" + position + " in data source");
    }

    public void convert(ViewHolder holder, T item, T lastItem, int position, int itemCounts, List<Object> payloads)
    {
        int delegatesCount = delegates.size();
        for (int i = 0; i < delegatesCount; i++)
        {
            BaseViewDelegate<T> delegate = delegates.valueAt(i);

            if (delegate.isForViewType( item, position))
            {
                if (delegate instanceof BaseViewDelegateImpl){
                    ((BaseViewDelegateImpl)delegate).convert(holder, item,lastItem, position,itemCounts,payloads);
                }else{
                    delegate.convert(holder, item,lastItem, position,itemCounts);
                }
                return;
            }
        }
        throw new IllegalArgumentException(
                "No BaseViewDelegateController added that matches position=" + position + " in data source");
    }


    public BaseViewDelegate getItemViewDelegate(int viewType)
    {
        return delegates.get(viewType);
    }

    public int getItemViewLayoutId(int viewType)
    {
        return getItemViewDelegate(viewType).getItemViewLayoutId();
    }

    public int getItemViewType(BaseViewDelegate baseViewDelegate)
    {
        return delegates.indexOfValue(baseViewDelegate);
    }
}
