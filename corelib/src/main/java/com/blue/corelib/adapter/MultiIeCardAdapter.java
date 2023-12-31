package com.blue.corelib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.blue.corelib.adapter.base.BaseViewDelegate;
import com.blue.corelib.adapter.base.BaseViewDelegateController;
import com.blue.corelib.adapter.base.ViewHolder;

import java.util.List;


public class MultiIeCardAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;

    protected BaseViewDelegateController mBaseViewDelegateController;
    protected OnItemClickListener mOnItemClickListener;


    public MultiIeCardAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mBaseViewDelegateController = new BaseViewDelegateController();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) {
            return super.getItemViewType(position);
        }
        return mBaseViewDelegateController.getItemViewType(mDatas.get(position), position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewDelegate baseViewDelegate = mBaseViewDelegateController.getItemViewDelegate(viewType);
        int layoutId = baseViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {

    }

    public void convert(ViewHolder holder, T t, T lastT) {
        mBaseViewDelegateController.convert(holder, t, lastT, holder.getAdapterPosition(), getItemCount());
    }

    public void convert(ViewHolder holder, T t, T lastT, List<Object> payloads) {
        mBaseViewDelegateController.convert(holder, t, lastT, holder.getAdapterPosition(), getItemCount(), payloads);
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) {
            return;
        }
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    // @see http://android.xsoftlab.net/reference/android/support/v7/widget/RecyclerView.ViewHolder.html#getAdapterPosition()
                    // Note that if you've called notifyDataSetChanged(), until the next layout pass,
                    // the return value of this method will be NO_POSITION.
                    if (position == RecyclerView.NO_POSITION) {
                        return;
                    }
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return position != RecyclerView.NO_POSITION && mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T lastData = position != 0 ? mDatas.get(position - 1) : null;
        convert(holder, mDatas.get(position), lastData);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        try {
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
                return;
            }
            T lastData = position != 0 ? mDatas.get(position - 1) : null;
            convert(holder, mDatas.get(position), lastData, payloads);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void refreshData(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 新增清除数据
     */
    public void clear() {
        mDatas.clear();
    }

    /**
     * 新增数据添加
     *
     * @param datas
     */
    public void addAllData(List<T> datas) {
        mDatas.addAll(datas);
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }


    public MultiIeCardAdapter addItemViewDelegate(BaseViewDelegate<T> baseViewDelegate) {
        mBaseViewDelegateController.addDelegate(baseViewDelegate);
        return this;
    }

    public MultiIeCardAdapter addItemViewDelegate(int viewType, BaseViewDelegate<T> baseViewDelegate) {
        mBaseViewDelegateController.addDelegate(viewType, baseViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mBaseViewDelegateController.getItemViewDelegateCount() > 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public Context getContext() {
        return mContext;
    }

    //添加单个在指定位置
    public void addItem(T content, int position) {
        mDatas.add(position, content);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    //添加单个数据在最后
    public void addItem(T content) {
        mDatas.add(content);
        notifyItemInserted(mDatas.size() - 1);
        notifyDataSetChanged();
    }

    //移除单个
    public void removeItem(T content) {
        int position = mDatas.indexOf(content);
        removeItem(position);
    }

    public void removeItem(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDatas.size());
    }

    //改变所有
    public void dataChange(List<T> content) {
        mDatas.clear();
        notifyDataSetChanged();

        if (content != null) {
            mDatas.addAll(content);
        }
        notifyDataSetChanged();

    }

    //清除所有
    public void dataClear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    //添加list
    public void dataAdd(List<T> content) {
        if (mDatas != null && content != null) {
            mDatas.addAll(content);
            notifyDataSetChanged();
        }
    }

    //往前添加list
    public void dataAddForward(List<T> content) {
        if (mDatas != null && content != null) {
            mDatas.addAll(0, content);
            notifyDataSetChanged();
        }
    }

    //添加不刷新
    public void justDataAdd(List<T> content) {
        if (mDatas != null && content != null) {
            mDatas.addAll(content);
        }
    }

    //改变单个
    public void updateOne(int position, T content) {
        mDatas.set(position, content);
        notifyItemChanged(position);
    }
}
