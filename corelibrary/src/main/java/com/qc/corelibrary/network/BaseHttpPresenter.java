package com.qc.corelibrary.network;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.qc.corelibrary.okhttp.OkHttpUtils;
import com.qc.corelibrary.okhttp.callback.Callback;
import com.qc.corelibrary.okhttp.request.RequestCall;
import com.qc.corelibrary.view.widget.LoadingDialog;
import com.qc.corelibrary.view.widget.PromptDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


public abstract class BaseHttpPresenter<T> {

    //    private boolean isCanceled;


    /**
     * 是否显示loading
     */
    protected boolean mShowLoadingDialog;
    /**
     * 加载中对话框
     */
    protected LoadingDialog mLoadingDialog;
    /**
     * 加载中对话框样式资源
     */
    protected int mLoadingDialogTheme;
    /**
     * 加载中对话框根布局资源
     */
    protected int mLoadingDialogRootResId;

    /**
     * 是否显示成功与失败对话提示框
     */
    protected boolean mShowPromptDialog;
    /**
     * 请求返回提示对话框
     */
    protected PromptDialog mPromptDialog;
    /**
     * 提示对话框样式资源
     */
    protected int mPromptDialogTheme;
    /**
     * 提示对话框根布局资源
     */
    protected int mPromptDialogRootResId;
    /**
     * 提示对话框单按钮背景资源
     */
    protected int mSingleButtonBackgroundResId;


    /**
     * 网络请求的tag
     */
    protected Object tag;

    public BaseHttpPresenter() {
        super();
        mShowPromptDialog = false;
        mShowLoadingDialog = false;
    }

    /**
     * 加载数据
     *
     * @param context
     * @param url      请求的地址
     * @param maker    请求参数的Maker
     * @param callBack {@link InvokerCallBack}
     * @param params   参数列表
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public void loadDatas(final Context context, final String url, RequestParamMaker maker, final InvokerCallBack<T> callBack, Object... params) throws Exception {
        if (TextUtils.isEmpty(url)) {
            System.out.println("url can not be null");
            return;
        }
        Map<String, String> realParams = new HashMap<String, String>();
        if (maker != null) realParams.putAll(maker.make(params));

        if (tag == null) {
            tag = url;
        }

        final RequestCall requestCall = OkHttpUtils.get()//
                .tag(tag)//
                .url(url)//
                .params(realParams)//
                .build();//

        if (isInvokingActivityAlive(context) && mShowLoadingDialog) {
            showLoadingDialog(context, BaseHttpPresenter.this, tag);
        }

        requestCall.execute(new Callback() {

            T result = null;


            @Override
            public void onError(Call call, Exception e, Response response) {
                if (!requestCall.getCall().isCanceled()) {
                    if (isInvokingActivityAlive(context)) {
                        dismissLoadingDialog();
                    }
                    int code;
                    String errorMessage = null;
                    if (response != null) {
                        code = getErrorCode(result);
                        errorMessage = getErrorMessage(result);
                    } else {
                        code = generateErrorCode(e);
                        errorMessage = null;
                    }
                    String promptMessage = TextUtils.isEmpty(errorMessage) ? generateMessage(code) : errorMessage;
                    if (mShowPromptDialog) showPromptDialog(context, promptMessage);
                    if (callBack != null) {
                        callBack.notifyFailed(code, promptMessage);
                    }
                }

            }

            @Override
            public void onSuccess(Response response) {
                if (!requestCall.getCall().isCanceled()) {
                    if (isInvokingActivityAlive(context)) {
                        dismissLoadingDialog();
                    }
                    if (callBack != null) {
                        callBack.notifySuccess(result);
                    }
                }
            }


            @Override
            public boolean isRequestSuccessFul(Response response) {
                try {
                    result = pretreat(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return isRequestSuccess(result);
            }

        });
    }


    /**
     * @param result
     * @return
     */
    protected abstract T pretreat(String result);

    /**
     * error.getCause().getClass() == ConnectTimeoutException.class<br>
     * error.getCause().getClass() == SocketTimeoutException.class<br>
     * error.getCause().getClass() == HttpHostConnectException.class<br>
     *
     * @param error
     * @return
     */
    protected abstract int generateErrorCode(Exception error);

    /**
     * 终止运行
     */
    public void cancel(Object tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
        //        isCanceled = true;
    }

    /**
     * 初始化加载对话框
     *
     * @param context
     * @param theme
     * @param rootViewResId
     */
    public void initLoadingDialog(Context context, int theme, int rootViewResId) {
        mLoadingDialogTheme = theme;
        mLoadingDialogRootResId = rootViewResId;
    }

    /**
     * 关闭加载对话框
     */
    private void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 显示加载对话框
     *
     * @param context
     */
    private void showLoadingDialog(Context context, BaseHttpPresenter presenter, Object tag) {
        if (!mShowLoadingDialog) {
            return;
        }
        LoadingDialog loadingDialog = getLoadingDialog(context, presenter, tag);
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    /**
     * 是否显示加载对话框
     *
     * @param isShow true:显示 false:不显示
     * @return
     */
    public BaseHttpPresenter<T> setShowLoading(boolean isShow) {
        mShowLoadingDialog = isShow;
        return this;
    }

    /**
     * @param context
     * @return
     */
    private LoadingDialog getLoadingDialog(Context context, BaseHttpPresenter presenter, Object tag) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(context);
        }
        if (mLoadingDialog.getContext() != context) {
            mLoadingDialog = new LoadingDialog(context);
        }
        if (mLoadingDialogTheme != 0 && mLoadingDialogRootResId != 0) {
            mLoadingDialog.initDialog(mLoadingDialogTheme, mLoadingDialogRootResId, presenter, tag);
            return mLoadingDialog;
        }
        return null;
    }

    /**
     * 初始化显示对话框
     */
    public void initPromptDialogStyle(int theme, int rootViewResId, int singleButtonBackgroundResId) {
        mPromptDialogTheme = theme;
        mPromptDialogRootResId = rootViewResId;
        mSingleButtonBackgroundResId = singleButtonBackgroundResId;
    }

    /**
     * 显示提示对话框。
     *
     * @param context
     * @param promptText
     */
    private void showPromptDialog(Context context, String promptText) {
        if (!mShowPromptDialog) {
            return;
        }
        PromptDialog dialog = getPromptDialog(context);
        if (dialog != null) {
            if (promptText != null) {
                dialog.setPromptText(promptText);
                setPromptDialogButtons(dialog, promptText);
                dialog.show();
            }
        }
    }

    /**
     * @param context
     * @return
     */
    private PromptDialog getPromptDialog(Context context) {
        if (mPromptDialog == null || mPromptDialog.getContext() != context) {
            mPromptDialog = new PromptDialog(context);
            if (mPromptDialogTheme != 0 && mPromptDialogRootResId != 0) {
                mPromptDialog.initDialog(mPromptDialogTheme, mPromptDialogRootResId);
                return mPromptDialog;
            } else {
                return null;
            }
        }
        return mPromptDialog;
    }

    /**
     * 是否显示提示对话框
     *
     * @param isShow true:显示 false:不显示
     * @return
     */
    public BaseHttpPresenter<T> setShowPromptDialog(boolean isShow) {
        mShowPromptDialog = isShow;
        return this;
    }


    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * 设置提示框的按钮。
     *
     * @param dialog
     * @param promptText error promptText
     */
    public abstract void setPromptDialogButtons(final PromptDialog dialog, String promptText);

    /**
     * 生成提示Dialog的内容。
     *
     * @param code
     * @return
     */
    protected abstract String generateMessage(int code);

    /**
     * 调用的Activity是否在前台。
     *
     * @return
     */
    protected boolean isInvokingActivityAlive(Context context) {
        return context != null && (context instanceof Activity) && !((Activity) context).isFinishing();
    }

    protected abstract boolean isRequestSuccess(T result);

    protected abstract int getErrorCode(T result);

    protected abstract String getErrorMessage(T result);

}
