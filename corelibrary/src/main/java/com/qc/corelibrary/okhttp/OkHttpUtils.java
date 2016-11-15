package com.qc.corelibrary.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.qc.corelibrary.okhttp.builder.GetBuilder;
import com.qc.corelibrary.okhttp.builder.PostFormBuilder;
import com.qc.corelibrary.okhttp.callback.Callback;
import com.qc.corelibrary.okhttp.cookie.CookieJarImpl;
import com.qc.corelibrary.okhttp.cookie.store.CookieStore;
import com.qc.corelibrary.okhttp.cookie.store.HasCookieStore;
import com.qc.corelibrary.okhttp.cookie.store.MemoryCookieStore;
import com.qc.corelibrary.okhttp.https.HttpsUtils;
import com.qc.corelibrary.okhttp.log.LoggerInterceptor;
import com.qc.corelibrary.okhttp.request.RequestCall;
import com.qc.corelibrary.okhttp.utils.Exceptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Call;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class OkHttpUtils {

    public static final int DEFAULT_MILLISECONDS = 30 * 1000;
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            //cookie enabled
            okHttpClientBuilder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            mOkHttpClient = okHttpClientBuilder.build();
        } else {
            mOkHttpClient = okHttpClient;
        }

        init();
    }

    private void init() {
        mDelivery = new Handler(Looper.getMainLooper());
        setConnectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        setReadTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        setWriteTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
    }


    public OkHttpUtils debug(String tag) {
        mOkHttpClient = getOkHttpClient().newBuilder().addInterceptor(new LoggerInterceptor(tag, false)).build();
        return this;
    }

    /**
     * showResponse may cause error, but you can try .
     *
     * @param tag
     * @param showResponse
     * @return
     */
    public OkHttpUtils debug(String tag, boolean showResponse) {
        mOkHttpClient = getOkHttpClient().newBuilder().addInterceptor(new LoggerInterceptor(tag, showResponse)).build();
        return this;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(null);
                }
            }
        }
        return mInstance;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null) callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (!call.isCanceled()) sendFailResultCallback(call, e, null, finalCallback);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                if (!call.isCanceled()) {
                    if (response.isSuccessful()) {
                        try {
                            if (finalCallback.isRequestSuccessFul(response)) {
                                sendSuccessResultCallback(response, finalCallback);
                            } else {
                                sendFailResultCallback(call, null, response, finalCallback);
                            }
                        } catch (Exception e) {
                            sendFailResultCallback(call, e, null, finalCallback);
                        }
                    } else {
                        sendFailResultCallback(call, new RuntimeException("网络服务不给力"), null, finalCallback);
                    }
                }
                response.body().close();
            }
        });
    }


    public CookieStore getCookieStore() {
        final CookieJar cookieJar = mOkHttpClient.cookieJar();
        if (cookieJar == null) {
            Exceptions.illegalArgument("you should invoked okHttpClientBuilder.cookieJar() to set a cookieJar.");
        }
        if (cookieJar instanceof HasCookieStore) {
            return ((HasCookieStore) cookieJar).getCookieStore();
        } else {
            return null;
        }
    }


    public void sendFailResultCallback(final Call call, final Exception e, final Response response, final Callback callback) {
        if (callback == null) return;

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, response);
            }
        });
    }

    public void sendSuccessResultCallback(final Response response, final Callback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response);
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public void cancelAll() {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            call.cancel();
        }
    }


    /**
     * for https-way authentication
     *
     * @param certificates
     */
    public void setCertificates(InputStream... certificates) {
        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslSocketFactory(certificates, null, null);

        OkHttpClient.Builder builder = getOkHttpClient().newBuilder();
        builder = builder.sslSocketFactory(sslSocketFactory);
        mOkHttpClient = builder.build();


    }

    /**
     * for https mutual authentication
     *
     * @param certificates
     * @param bksFile
     * @param password
     */
    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        mOkHttpClient = getOkHttpClient().newBuilder().sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, bksFile, password)).build();
    }

    public void setHostNameVerifier(HostnameVerifier hostNameVerifier) {
        mOkHttpClient = getOkHttpClient().newBuilder().hostnameVerifier(hostNameVerifier).build();
    }

    public void setConnectTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder().connectTimeout(timeout, units).build();
    }

    public void setReadTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder().readTimeout(timeout, units).build();
    }

    public void setWriteTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder().writeTimeout(timeout, units).build();
    }

}

