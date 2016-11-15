package com.qc.corelibrary.okhttp.callback;

import okhttp3.Call;
import okhttp3.Response;

public abstract class Callback{
    /**
     * Thread Pool Thread
     *
     * @param progress
     */
    public void inProgress(float progress) {

    }

    public abstract void onError(Call call, Exception e, Response response);

    public abstract void onSuccess(Response response);

    public abstract boolean isRequestSuccessFul(Response response);


    public static Callback CALLBACK_DEFAULT = new Callback() {


        @Override
        public void onError(Call call, Exception e, Response response) {

        }

        @Override
        public void onSuccess(Response response) {

        }

        @Override
        public boolean isRequestSuccessFul(Response response) {
            return false;
        }
    };

}