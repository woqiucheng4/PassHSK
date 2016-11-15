package com.qc.corelibrary.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.qc.corelibrary.R;
import com.qc.corelibrary.bean.Page;
import com.qc.corelibrary.view.adapter.BaseAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * <ul>
 * <li>功能职责：可上拉加载，下拉刷新的RecyclerView</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-10-17
 */
public class SwipeRecyclerView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 旋转圆圈颜色值
     */
    private int[] colorSchemeResources = new int[]{android.R.color.holo_blue_dark,//
            android.R.color.holo_blue_light,//
            android.R.color.holo_green_dark,//
            android.R.color.holo_green_light};

    /**
     * 显示数据的RecyclerView
     */
    private RecyclerView mRecyclerView;
    /**
     * 显示数据的Adapter
     */
    private BaseAdapter mAdapter;

    /**
     * 线性布局LayoutManager
     */
    private LinearLayoutManager mLayoutManager;
    /**
     * 刷新加载更多Listener
     */
    private RefreshLoadMoreListener refreshLoadMoreListener;
    /**
     * 每一项是否为固定size
     */
    private boolean mHasFixedSize;
    /**
     * 最后一个可见的item
     */
    private int lastVisibleItem;
    /**
     * 是否可加载更多
     */
    private boolean enableLoadMore;
    /**
     * 是否可刷新
     */
    private boolean enableRefresh;
    /**
     * 是否在进行刷新
     */
    private boolean isRefreshing;
    /**
     * 是否在加载更多
     */
    private boolean isLoading;

    private Page mPage;

    private List<Integer> mDatas;


    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRecyclerView = new RecyclerView(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeRecyclerViewStyle);
        mHasFixedSize = a.getBoolean(R.styleable.SwipeRecyclerViewStyle_hasFixedSize, false);
        enableLoadMore = a.getBoolean(R.styleable.SwipeRecyclerViewStyle_enableLoadMore, false);
        enableRefresh = a.getBoolean(R.styleable.SwipeRecyclerViewStyle_enableRefresh, true);
        a.recycle();
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        isRefreshing = false;
        isLoading = false;
        setColorSchemeResources(colorSchemeResources);
        setProgressViewOffset(false, 0, //
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,//
                        24,//
                        getResources().getDisplayMetrics()));
        setOnRefreshListener(this);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        mRecyclerView.setHasFixedSize(mHasFixedSize);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final Context finalContext = context;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                final Picasso picasso = Picasso.with(finalContext);
                //滑动不加载图片，停止滑动加载图片
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    picasso.resumeTag(finalContext);
                } else {
                    picasso.pauseTag(finalContext);
                }
                if (enableLoadMore && !isLoading && !isRefreshing && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == recyclerView.getAdapter().getItemCount()) {
                    isLoading = true;
                    setViewEnabled(false);
                    refreshLoadMoreListener.onLoadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }

        });
        addView(mRecyclerView);
    }

    /**
     * 设置控件是否可以使用
     *
     * @param enabled
     */
    private void setViewEnabled(boolean enabled) {
        mRecyclerView.setEnabled(enabled);
        setEnabled(enabled);
    }

    /**
     * 控件刷新操作
     */
    @Override
    public void onRefresh() {
        isRefreshing = true;
        setViewEnabled(false);
        refreshLoadMoreListener.onRefresh();
    }

    /**
     * 设置控件item分割线
     *
     * @param dividerItemDecoration
     */
    public void addItemDecoration(DividerItemDecoration dividerItemDecoration) {
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    /**
     * 设置控件item动画
     *
     * @param animator
     */
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    /**
     * 设置控件刷新加载监听
     *
     * @param refreshLoadMoreListener
     */
    public void setRefreshLoadMoreListener(RefreshLoadMoreListener refreshLoadMoreListener) {
        this.refreshLoadMoreListener = refreshLoadMoreListener;
    }

    /**
     * 判断控件是否可以加载
     *
     * @return
     */
    public boolean isEnableLoadMore() {
        return enableLoadMore;
    }

    /**
     * 设置控件是否可以加载
     *
     * @param enableLoadMore
     */
    public void setEnableLoadMore(boolean enableLoadMore) {
        this.enableLoadMore = enableLoadMore;
        if (mAdapter != null) {
            mAdapter.setEnableLoadMore(enableLoadMore);
        }
    }

    /**
     * 判断控件是否可以刷新
     *
     * @return
     */
    public boolean isEnableRefresh() {
        return enableRefresh;
    }

    /**
     * 设置控件是否可以刷新
     *
     * @param enableRefresh
     */
    public void setEnableRefresh(boolean enableRefresh) {
        this.enableRefresh = enableRefresh;
    }

    /**
     * 获取Adpter
     *
     * @return
     */
    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 设置Adapter
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 停止刷新和加载
     */
    public void stopRefreshLoad() {
        isRefreshing = false;
        isLoading = false;
        setRefreshing(false);
        setViewEnabled(true);
    }

    /**
     * 刷新数据
     *
     * @param page
     * @param data
     */
    public void updateRecyclerViewData(Page page, List<Integer> data) {
        updateListData(page, data);
        resetEnabeLoadMore(page);
        stopRefreshLoad();
    }

    private void resetEnabeLoadMore(Page page) {
        mPage = page;
        if (page == null) {
            setEnableLoadMore(false);
            return;
        }
        int now = page.getNowPage();
        int total = page.getTotalPage();
        if (now >= total) {
            setEnableLoadMore(false);
        } else {
            setEnableLoadMore(true);
        }

    }

    private void updateListData(Page page, List<Integer> datas) {
        if (page == null || (page != null && 1 == page.getNowPage())) {
            getAdapter().setData(null);
        }
        int position = 0;
        if (getAdapter().getData() == null) {
            position = 0;
        } else {
            position = getAdapter().getData().size();
        }

        if (getAdapter().getData() == null) {
            getAdapter().setData(datas);
        } else {
            getAdapter().getData().addAll(datas);
        }
        getAdapter().notifyItemInserted(position);
        getAdapter().notifyItemRangeChanged(position, datas.size() + 1);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 插入数据到某个位置
     *
     * @param position
     * @param data
     */
    public void insertedRecyclerViewData(int position, Integer data) {
        getAdapter().getData().add(position, data);
        getAdapter().notifyItemInserted(position);
        getAdapter().notifyDataSetChanged();
    }

    /**
     * 删除某个位置数据
     *
     * @param position
     */
    public void deleteRecyclerViewData(int position) {
        getAdapter().notifyItemRemoved(position);
        getAdapter().notifyDataSetChanged();
    }


    /**
     * 刷新和加载更多监听事件
     */
    public interface RefreshLoadMoreListener {

        void onRefresh();

        void onLoadMore();
    }


}
