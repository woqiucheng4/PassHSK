package com.qc.corelibrary.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qc.corelibrary.R;
import com.qc.corelibrary.view.adapter.viewholder.BaseViewHolder;
import com.qc.corelibrary.view.adapter.viewholder.FooterViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 * <li>功能职责：</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-08-29
 */
public abstract class BaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private static final int ITEM_TYPE_FOOTER = Integer.MAX_VALUE;

    private boolean mEnableLoadMore;

    protected final Context context;

    private List<T> data;

    private OnItemClickListener mOnItemClickListener = null;

    private OnItemLongClickListener mOnItemLongClickListener = null;


    /**
     * Create a BaseAdapter.
     *
     * @param context The context.
     */
    public BaseAdapter(Context context) {
        this(context, null);
    }

    /**
     * Same as BaseAdapter#BaseAdapter(Context,int) but with
     * some initialization data.
     *
     * @param context The context.
     * @param data    A new list is created out of this one to avoid mutable list
     */
    public BaseAdapter(Context context, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : data;
        this.context = context;
    }


    // RecyclerView的count设置为数据总条数+ 1（footerView）
    @Override
    public int getItemCount() {
        return mEnableLoadMore ? data.size() + 1 : data.size();

    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (mEnableLoadMore && position + 1 == getItemCount()) {
            return ITEM_TYPE_FOOTER;
        } else {
            return getMoreItemViewType(position);
        }
    }

    public T getItem(int position) {
        if (position >= data.size()) return null;
        return data.get(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(getMoreItemLayoutResId(viewType), viewGroup, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        BaseViewHolder vh = getMoreItemViewHolder(view, viewType);
        return vh;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.itemView.setTag(position);
        T item = getItem(position);
        if (!(holder instanceof FooterViewHolder)) convert((H) holder, item);
    }

    /**
     * 获取item的多种布局，如果只有一种样式，则不用进行任何操作
     *
     * @param viewType
     * @return
     */
    private int getMoreItemLayoutResId(int viewType) {
        if (ITEM_TYPE_FOOTER == viewType) {
            return R.layout.item_recyclerview_footer_view;
        } else {
            return getItemLayoutResId(viewType);
        }
    }

    /**
     * 获取item的多种holder
     *
     * @param view
     * @param viewType
     * @return
     */
    private BaseViewHolder getMoreItemViewHolder(View view, int viewType) {
        if (ITEM_TYPE_FOOTER == viewType) {
            return new FooterViewHolder(view);
        } else {
            return getItemViewHolder(view, viewType);
        }
    }

    /**
     * 获取除footer item以外的item布局
     *
     * @return
     */
    public abstract int getItemLayoutResId(int viewType);


    /**
     * 获取除footer item以外的item的ViewHolder
     *
     * @return
     */
    public abstract BaseViewHolder getItemViewHolder(View view, int viewType);

    /**
     * 界面数据处理
     *
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    public abstract void convert(H holder, T item);

    /**
     * 获取更多item类型
     *
     * @param position
     * @return
     */
    public abstract int getMoreItemViewType(int position);

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(v, (int) v.getTag());
        }
        return false;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setEnableLoadMore(boolean enable) {
        mEnableLoadMore = enable;
    }


}