package com.qc.hsk.network;

/**
 * <ul>
 * <li>功能职责：网络请求接口</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-11-16
 */
public class HttpInterface {

    /**
     * 获取版本更新信息
     *
     * @return
     */
    public static String getVersionInfoURL() {
        return "https://raw.githubusercontent.com/woqiucheng4/hsk/master/version";
    }
    /**
     * 获取HSK考试一级词汇
     *
     * @return
     */
    public static String getHSKOneURL() {
        return "https://raw.githubusercontent.com/woqiucheng4/HSKData/master/hsk1";
    }

}
