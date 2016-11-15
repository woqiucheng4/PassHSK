package com.qc.corelibrary.okhttp.request;


import com.qc.corelibrary.okhttp.OkHttpUtils;
import com.qc.corelibrary.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Request;

public class RequestCall {
    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;

    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }


    private Request generateRequest(Callback callback) {
        return okHttpRequest.generateRequest(callback);
    }

    public void execute(Callback callback) {
        request = generateRequest(callback);
        call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);

        OkHttpUtils.getInstance().execute(this, callback);
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }


}
