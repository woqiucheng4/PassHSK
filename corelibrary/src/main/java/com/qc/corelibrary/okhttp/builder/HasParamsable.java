package com.qc.corelibrary.okhttp.builder;

import java.util.Map;

public interface HasParamsable
{
    public abstract OkHttpRequestBuilder params(Map<String, String> params);

    public abstract OkHttpRequestBuilder addParams(String key, String val);

}
