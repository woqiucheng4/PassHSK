package com.qc.hsk.network;

import com.alibaba.fastjson.JSON;
import com.qc.corelibrary.network.BaseHttpPresenter;
import com.qc.corelibrary.view.widget.PromptDialog;
import com.qc.hsk.R;
import com.qc.hsk.network.bean.BaseBean;
import com.qc.hsk.network.bean.ErrorBean;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;


/**
 * @author CreditEase/FSO
 */
public class HttpPresenter extends BaseHttpPresenter<String> {

    private static volatile HttpPresenter instance;

    /**
     * @return
     */
    public static HttpPresenter getInstance() {
        if (instance == null) {
            synchronized (HttpPresenter.class) {
                if (instance == null) {
                    instance = new HttpPresenter();
                }
            }
        }
        return instance;
    }

    /**
     *
     */
    private HttpPresenter() {
        super();
        mPromptDialogTheme = R.style.dialogStyle;
        mPromptDialogRootResId = R.layout.public_prompt_dialog_layout;
        mSingleButtonBackgroundResId = R.drawable.prompt_dialog_left_btn_selector;
        mLoadingDialogTheme = R.style.dialogStyle;
        mLoadingDialogRootResId = R.layout.public_loading_dialog;
    }

    @Override
    protected int generateErrorCode(Exception error) {
        if (error != null) {
            Throwable cause = error.getCause();
            if (cause != null) {
                if (cause.getClass() == ConnectTimeoutException.class || cause.getClass() == SocketTimeoutException.class) {
                    return CreditEaseMessage.ERROR_NETWORK_CONNECT_TIMEOUT;
                } else if (cause.getClass() == ConnectException.class) {
                    return CreditEaseMessage.ERROR_NETWORK_HTTP_HOST_CONNECTION;
                }
            }
        }
        return CreditEaseMessage.ERROR_NETWORK;
    }

    @Override
    public void setPromptDialogButtons(PromptDialog dialog, String promptText) {
        dialog.setSingleBtn(true, mSingleButtonBackgroundResId);
    }

    @Override
    protected String generateMessage(int code) {
        return CreditEaseMessage.getMessage(code);
    }

    @Override
    protected int getErrorCode(String result) {
        try {
            return Integer.parseInt(JSON.parseObject(result, BaseBean.class).getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CreditEaseMessage.ERROR_FAILED;
    }

    @Override
    protected String getErrorMessage(String result) {
        try {
            return JSON.parseObject(result, ErrorBean.class).getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected boolean isRequestSuccess(String result) {
        try {
            return Integer.parseInt(JSON.parseObject(result, BaseBean.class).getCode()) == CreditEaseMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected String pretreat(String result) {
        return result;
    }

}