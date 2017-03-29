package com.qc.hsk;


import com.qc.corelibrary.BaseApplication;
import com.qc.corelibrary.utils.CatchCrash;

/**
 * <ul>
 * <li>功能职责：</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-11-08
 */
public class HSKApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        CatchCrash.getInstance().init(getApplicationContext());
    }

}