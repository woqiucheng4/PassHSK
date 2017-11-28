/**
 *
 */
package com.qc.hsk.constants;

/**
 * 常量值
 */
public class Constants {

    // ================== 初始化参数设置开始 ==========================
    /**
     * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
     * 本demo的包名是com.baidu.tts.sample，定义在build.gradle中。
     */
    public static final String APPID = "8876475";

    public static final String APP_KEY = "UHDbLtGHjKc4aztGs7B9S8Xw";

    public static final String SECRET_KEY = "f3421791936c81737a9af280dc3431c9";

    /**
     * activity之间传值
     */
    public interface IntentBundleKey {
        //单词详情
        String WOORD_DETAIL = "WordDetail";
    }

    /**
     * startActivityForResult需要传递的RequestCode
     */
    public interface IntentRequestCode {
        //上传文件
        int UPLOAD_FILE_CODE = 1120;
    }


}
