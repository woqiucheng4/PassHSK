package com.qc.corelibrary.view.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.qc.corelibrary.network.BaseHttpPresenter;
import com.qc.corelibrary.utils.CommonUtils;


/**
 * 公共加载对话框
 *
 * @author wangweiqiang
 */
public class LoadingDialog {

    public static final String TAG_HINT_TEXT = "hintText";

    private Dialog dialog;
    private View mContentView;
    private Context mContext;

    public LoadingDialog(Context context) {
        mContext = context;
    }

    /**
     * @param context
     * @param theme
     * @param rootViewResId 根View。包含如下tag：<br>
     */
    public LoadingDialog(Context context, int theme, int rootViewResId, BaseHttpPresenter presenter, Object tag) {
        super();
        initDialog(theme, rootViewResId, presenter, tag);
    }

    /**
     * @param theme
     * @param rootViewResId
     */
    @SuppressLint("InflateParams")
    public void initDialog(int theme, int rootViewResId, final BaseHttpPresenter presenter, final Object tag) {
        Context context = mContext;
        dialog = new Dialog(context, theme);
        LayoutParams params = new LayoutParams(CommonUtils.dip2px(context, 150), CommonUtils.dip2px(context, 150));
        mContentView = LayoutInflater.from(context).inflate(rootViewResId, null);
        dialog.addContentView(mContentView, params);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                boolean click = false;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    presenter.cancel(tag);
                    if (dialog != null) dialog.dismiss();
                    click = true;
                }
                return click;
            }
        });
    }

    /**
     * 显示对话框
     */

    public synchronized void show() {
        if (dialog == null || dialog.isShowing()) {
            return;
        }
        dialog.show();
    }

    /**
     * 关闭对话框
     */
    public synchronized void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 设置加载提示。
     *
     * @param hint
     */
    public void setLoadingHint(final String hint) {
        Activity activity = null;
        try {
            activity = ((Activity) mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                TextView hintView = (TextView) mContentView.findViewWithTag(TAG_HINT_TEXT);
                if (hintView == null) {
                    return;
                }
                if (hint == null) {
                    hintView.setVisibility(View.GONE);
                } else {
                    hintView.setVisibility(View.VISIBLE);
                }
                hintView.setText(hint);
            }
        });
    }

    /**
     * @return
     */
    public Context getContext() {
        return mContext;
    }

}
