package com.qc.corelibrary.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.qc.corelibrary.R;
import com.qc.corelibrary.utils.ActivitUtils;
import com.qc.corelibrary.utils.PermissionUtils;
import com.qc.corelibrary.utils.network.NetWorkStateReceiver;
import com.qc.corelibrary.utils.network.NetworkUtils;
import com.qc.corelibrary.view.manager.ToolBarManager;
import com.qc.corelibrary.view.widget.ExceptionView;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, PermissionUtils.PermissionGrant,ExceptionView.OnExceptionViewClickListener {

    /**
     * 标题栏布局Tag
     */
    private static final String TAG_TOOLBAR = "toolbar";
    /**
     * 抽屉布局Tag
     */
    private static final String TAG_DRAWER_LAYOUT = "drawerLayout";
    /**
     * 抽屉内容Tag
     */
    private static final String TAG_NAVIGATIONVIEW = "navigationView";
    /**
     * 内容布局Tag
     */
    private static final String TAG_CONTENT_LAYOUT = "contentLayout";

    /**
     * 标题栏Toolbar
     */
    private Toolbar mToolbar;
    /**
     * 抽屉栏DrawerLayout
     */
    private DrawerLayout mDrawerLayout;
    /**
     * 抽屉栏内容
     */
    private NavigationView mNavigationView;
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
    /**
     * 是否显示DrawerLayout
     */
    private boolean isShowDrawerLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        mToolbar = (Toolbar) rootView.findViewWithTag(TAG_TOOLBAR);
        mDrawerLayout = (DrawerLayout) rootView.findViewWithTag(TAG_DRAWER_LAYOUT);
        mNavigationView = (NavigationView) rootView.findViewWithTag(TAG_NAVIGATIONVIEW);
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

    /**
     * 初始化抽屉header
     */
    public View setNavigationViewHeader(int headerLayoutID) {
        if (mNavigationView != null) {
            return mNavigationView.inflateHeaderView(headerLayoutID);
        }
        return null;
    }

    /**
     * 初始化抽屉content
     */
    public void setNavigationViewMenu(int menuID) {
        if (mNavigationView != null) {
            mNavigationView.inflateMenu(menuID);
        }
    }

    /**
     * 设置抽屉内容点击事件
     */
    public void setOnNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener listener) {
        mNavigationView.setNavigationItemSelectedListener(listener);
    }

    /**
     * 初始化异常view
     */
    private void initExceptionView() {
        mExceptionView = new ExceptionView(this);
        mContentLayout.addView(mExceptionView.getExceptionView());
    }


    private void initToolbarView() {
        mToolbarManager = new ToolBarManager(this, mToolbar);
    }

    /**
     * 自定义页面
     */
    protected abstract void initCustomView();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isShowDrawerLayout()) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                }
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
     * 设置
     */
    public void setHomeAsUpIndicator(int drawableID) {
        mToolbarManager.setHomeAsUpIndicator(drawableID);
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

    /**
     * 关闭侧边栏
     */
    public void closeDrawerLayout() {
        mDrawerLayout.closeDrawers();
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    /**
     * 是否显示侧边栏
     *
     * @return
     */
    public boolean isShowDrawerLayout() {
        return isShowDrawerLayout;
    }

    /**
     * 控制是否显示侧边栏
     */
    public void setShowDrawerLayout(boolean showDrawerLayout) {
        isShowDrawerLayout = showDrawerLayout;
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        switch (requestCode) {
            case PermissionUtils.CODE_RECORD_AUDIO:
                Toast.makeText(BaseActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_READ_CONTACTS:
                Toast.makeText(BaseActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_READ_PHONE_STATE:
                Toast.makeText(BaseActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_CALL_PHONE:
                Toast.makeText(BaseActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_SEND_SMS:
                Toast.makeText(BaseActivity.this, "Result Permission Grant CODE_SEND_SMS", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_CAMERA:
                Toast.makeText(BaseActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                Toast.makeText(BaseActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                Toast.makeText(BaseActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                Toast.makeText(BaseActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                break;
            case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                Toast.makeText(BaseActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


}
