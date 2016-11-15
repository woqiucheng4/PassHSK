package com.qc.corelibrary.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.qc.corelibrary.R;
import com.qc.corelibrary.utils.ActivitUtils;
import com.qc.corelibrary.utils.network.NetWorkStateReceiver;
import com.qc.corelibrary.utils.network.NetworkUtils;
import com.qc.corelibrary.view.manager.ToolBarManager;
import com.qc.corelibrary.view.widget.ExceptionView;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity implements ExceptionView.OnExceptionViewClickListener {

    /**
     * 标题栏布局Tag
     */
    private static final String TAG_TOOLBAR = "toolbar";
    /**
     * 内容布局Tag
     */
    private static final String TAG_CONTENT_LAYOUT = "contentLayout";

    /**
     * 标题栏Toolbar
     */
    private Toolbar mToolbar;
    /**
     * 根view
     */
    private ViewGroup rootView;
    /**
     * Toolbar管理者
     */
    private ToolBarManager mToolbarManager;

    /**
     * 内容布局View
     */
    private FrameLayout mContentLayout;
    /**
     * 异常显示view
     */
    private ExceptionView mExceptionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        mToolbar = (Toolbar) rootView.findViewWithTag(TAG_TOOLBAR);
        mContentLayout = (FrameLayout) rootView.findViewWithTag(TAG_CONTENT_LAYOUT);
        NetWorkStateReceiver.registerNetworkStateReceiver(this);
        ActivitUtils.getInstance().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        NetWorkStateReceiver.unRegisterNetworkStateReceiver(this);
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        View contentView = LayoutInflater.from(this).inflate(layoutResID, null);
        mContentLayout.addView(contentView);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        initToolbarView();
        initExceptionView();
        initCustomView();

    }

    private void initExceptionView() {
        mExceptionView = new ExceptionView(this);
        mContentLayout.addView(mExceptionView.getExceptionView());
    }


    private void initToolbarView() {
        mToolbarManager = new ToolBarManager(this, mToolbar);
    }

    protected abstract void initCustomView();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置居中标题
     *
     * @param text
     */
    public void setTitleText(String text) {
        mToolbarManager.setTitleText(text);
    }

    /**
     * 设置居中标题内容
     *
     * @param textID
     */
    public void setTitleText(int textID) {
        mToolbarManager.setTitleText(textID);
    }


    /**
     * 隐藏toolBar
     */
    public void hideTitleBar() {
        mToolbarManager.hideTitleBar();
    }

    /**
     * 显示toolBar
     */
    public void showTitleBar() {
        mToolbarManager.showTitleBar();
    }

    /**
     * 设置居中标题为View
     *
     * @param view
     */
    public void setTitleView(View view) {
        mToolbarManager.setTitleView(view);
    }

    /**
     * Set a listener to respond to menu item click events.
     *
     * @param listener Listener to set
     */
    public void setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener listener) {
        mToolbarManager.setOnMenuItemClickListener(listener);
    }

    public Toolbar getTitleView() {
        return mToolbarManager.getTitleView();
    }


    /**
     * 当前没有网络连接
     */
    public void onDisConnect() {
        mExceptionView.showExceptionView();
    }

    /**
     * 网络连接连接时调用
     */
    public void onConnect(NetworkUtils.NetType type) {
        mExceptionView.hideExceptionView();
    }


    @Override
    public void dealExceotionView() {
        Intent intent = null;
        intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        startActivity(intent);
    }
}
