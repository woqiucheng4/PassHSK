package com.qc.corelibrary;


import android.app.Application;

import com.qc.corelibrary.utils.network.NetWorkChangeObserver;
import com.qc.corelibrary.utils.network.NetWorkStateReceiver;
import com.qc.corelibrary.utils.network.NetworkUtils;

/**
 * <ul>
 * <li>功能职责：</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2017-03-28
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    private void initApplication() {
        NetWorkStateReceiver.getReceiver().registerObserver(taNetChangeObserver);
    }

    NetWorkChangeObserver taNetChangeObserver = new NetWorkChangeObserver() {

        @Override
        public void onConnect(NetworkUtils.NetType type) {
            super.onConnect(type);
            BaseApplication.this.onConnect(type);
        }

        @Override
        public void onDisConnect() {
            super.onDisConnect();
            BaseApplication.this.onDisConnect();

        }
    };


    /**
     * 当前没有网络连接
     */
    protected void onDisConnect() {
    }

    /**
     * 网络连接连接时调用
     */
    protected void onConnect(NetworkUtils.NetType type) {
    }

}
