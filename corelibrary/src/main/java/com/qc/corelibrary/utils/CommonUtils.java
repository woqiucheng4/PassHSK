package com.qc.corelibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 常用工具类
 */
public class CommonUtils {

    /**
     * 根据手机的分辨率dp转成px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率px转成dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @param value
     * @return 将dip或者dp转为float
     */
    public static float dipOrDpToFloat(String value) {
        if (value.indexOf("dp") != -1) {
            value = value.replace("dp", "");
        } else {
            value = value.replace("dip", "");
        }
        return Float.parseFloat(value);
    }

    /**
     * 判断是否有网络连接
     *
     * @param mContext
     * @return
     */
    public static boolean isNetworkConnected(Context mContext) {

        ConnectivityManager manager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }

    /**
     * 只允许字母、数字和汉字
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String p = "^[1-9][0-9]{1,8}(.[0-9]{0,2})";
        String p1 = "^[1-9]";
        String p2 = "^[1][0]{9}(.[0]{1,2})";

        Pattern pattern = null;

        if (Pattern.matches(p, str)) {
            pattern = Pattern.compile(p);

        } else if (Pattern.matches(p1, str)) {
            pattern = Pattern.compile(p1);
        } else if (Pattern.matches(p2, str)) {
            pattern = Pattern.compile(p2);
        }
        Matcher m = pattern.matcher(str);
        return m.replaceAll("").trim();
    }


}
