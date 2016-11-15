package com.qc.hsk.network;

import java.util.HashMap;
import java.util.Map;

public class CreditEaseMessage {
    public static final int ERROR_FAILED = 2;
    public static final int ERROR_EMPTY = 3;
    public static final int ERROR_JSON = 4;
    public static final int ERROR_UNKNOW = 5;
    public static final int ERROR_NET_POOR = 6;
    public static final int ERROR_RESPONSE = 7;
    public static final int ERROR_HTTP = 8;
    public static final int UNSUPPORTED_ENCODING = 9;
    public static final int PARSE_EXCEPTION = 10;
    public static final int IO_EXCEPTION = 11;
    public static final int ERROR_URL_FORMAT = 13;
    public static final int ERROR_NETWORK_CONNECT_TIMEOUT = 14;
    public static final int ERROR_NETWORK_HTTP_HOST_CONNECTION = 15;
    public static final int ERROR_NETWORK = 16;
    public static final int SUCCESS = 2000;
    protected static final Map<Integer, String> mMsgs = new HashMap<Integer, String>();

    /**
     * 返回值说明
     * 分类：空值类（格式：3***）、处理类（格式：4***）、系统异常类（5***）、数据库异常类（7***）、数据库查询空值类（9***）、正常类（
     * 2000）
     */
    static {
        mMsgs.put(SUCCESS, "ok");
        // 异常
        mMsgs.put(ERROR_FAILED, "操作失败");
        mMsgs.put(ERROR_EMPTY, "数据为空");
        mMsgs.put(ERROR_JSON, "数据格式错误");
        mMsgs.put(ERROR_UNKNOW, "unknow error");
        mMsgs.put(ERROR_NET_POOR, "当前无网络连接");
        mMsgs.put(ERROR_RESPONSE, "返回格式错误");
        mMsgs.put(ERROR_HTTP, "网络服务不给力~");
        mMsgs.put(UNSUPPORTED_ENCODING, "不支持的HTTP编码格式");
        mMsgs.put(PARSE_EXCEPTION, "HTTP返回参数错误");
        mMsgs.put(IO_EXCEPTION, "IO 错误");
        mMsgs.put(ERROR_URL_FORMAT, "URL格式错误");
        mMsgs.put(ERROR_NETWORK_CONNECT_TIMEOUT, "网络连接超时！请检查您的网络设置。");
        mMsgs.put(ERROR_NETWORK_HTTP_HOST_CONNECTION, "无法连接服务器！请检查您的网络设置，或与客服联系。");
        mMsgs.put(ERROR_NETWORK, "网络错误！请检查您的网络设置。");
        // 空值类：
        mMsgs.put(3001, "APPID为空");
    }

    public static void putMsg(int code, String errorMsg) {
        if (!mMsgs.containsKey(code)) {
            mMsgs.put(code, errorMsg);
        }
    }

    public static String getMessage(int code) {
        return mMsgs.containsKey(code) ? mMsgs.get(code) : "操作失败";
    }
}
