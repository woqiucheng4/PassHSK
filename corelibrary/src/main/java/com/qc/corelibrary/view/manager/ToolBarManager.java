package com.qc.corelibrary.view.manager;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * <ul>
 * <li>功能职责：Toolbar管理器</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-06-29
 */
public class ToolBarManager {

    private Toolbar mToolbar;

    private AppCompatActivity mContext;

    private TextView titleTextView;

    private int mGravity;

    public ToolBarManager(AppCompatActivity context, Toolbar toolbar) {
        mContext = context;
        mToolbar = toolbar;
        mToolbar.setTitle("");
        mContext.setSupportActionBar(mToolbar);
        mGravity = Gravity.LEFT;
        ActionBar ab = mContext.getSupportActionBar();
        ab.setHomeAsUpIndicator(android.R.drawable.ic_btn_speak_now);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void setTitleViewGravity(int gravity) {
        mGravity=gravity;
    }

    /**
     * 设置居中标题
     *
     * @param text
     */
    public void setTitleText(String text) {
        if (titleTextView != null) {
            titleTextView.setText(text);
        } else {
            titleTextView = new TextView(mContext);
            Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
            lp.gravity = mGravity;
            titleTextView.setLayoutParams(lp);
            titleTextView.setTextColor(mContext.getResources().getColor(android.R.color.white));
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            titleTextView.setText(text);
            mToolbar.addView(titleTextView);
        }
    }

    /**
     * 设置居中标题内容
     *
     * @param textID
     */
    public void setTitleText(int textID) {
        TextView titleTextView = new TextView(mContext);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = mGravity;
        titleTextView.setLayoutParams(lp);
        titleTextView.setTextColor(mContext.getResources().getColor(android.R.color.white));
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        titleTextView.setText(textID);
        mToolbar.addView(titleTextView);
    }


    /**
     * 隐藏toolBar
     */
    public void hideTitleBar() {
        mToolbar.setVisibility(View.GONE);
    }

    /**
     * 隐藏toolBar
     */
    public void showTitleBar() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    /**
     * 更改返回按钮的图标
     */
    public void setHomeAsUpIndicator(int drawableID) {
        mContext.getSupportActionBar().setHomeAsUpIndicator(drawableID);
    }

    /**
     * 设置居中标题为View
     *
     * @param view
     */
    public void setTitleView(View view) {
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = mGravity;
        view.setLayoutParams(lp);
        mToolbar.addView(view);
    }

    /**
     * Set a listener to respond to menu item click events.
     *
     * @param listener Listener to set
     */
    public void setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener listener) {
        mToolbar.setOnMenuItemClickListener(listener);
    }

    public Toolbar getTitleView() {
        return mToolbar;
    }


}
