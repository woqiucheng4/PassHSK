package com.qc.corelibrary.utils.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;

/**
 * <ul>
 * <li>功能职责：</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-08-08
 */
public class NetWorkStateReceiver extends BroadcastReceiver {
    private  Boolean networkAvailable = false;
    private  NetworkUtils.NetType netType;
    private  ArrayList<NetWorkChangeObserver> netChangeObserverArrayList = new ArrayList<>();
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private static NetWorkStateReceiver receiver;

    public static NetWorkStateReceiver getReceiver() {
        if (receiver == null) {
            receiver = new NetWorkStateReceiver();
        }
        return receiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        receiver = NetWorkStateReceiver.this;
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
            if (!NetworkUtils.isNetworkAvailable(context)) {
                networkAvailable = false;
            } else {
                networkAvailable = true;
            }
            netType = NetworkUtils.getAPNType(context);
            notifyObserver();
        }
    }

    /**
     * 注册网络状态广播
     *
     * @param mContext
     */
    public void registerNetworkStateReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        mContext.registerReceiver(getReceiver(), filter);
    }


    /**
     * 注销网络状态广播
     *
     * @param mContext
     */
    public void unRegisterNetworkStateReceiver(Context mContext) {
        if (receiver != null) {
            try {
                mContext.unregisterReceiver(receiver);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取当前网络状态，true为网络连接成功，否则网络连接失败
     *
     * @return
     */
    public Boolean isNetworkAvailable() {
        return networkAvailable;
    }

    public NetworkUtils.NetType getAPNType() {
        return netType;
    }

    private void notifyObserver() {
        for (int i = 0; i < netChangeObserverArrayList.size(); i++) {
            NetWorkChangeObserver observer = netChangeObserverArrayList.get(i);
            if (observer != null) {
                if (isNetworkAvailable()) {
                    observer.onConnect(netType);
                } else {
                    observer.onDisConnect();
                }
            }
        }
    }

    /**
     * 注册网络连接观察者
     *
     * @param observer
     */
    public void registerObserver(NetWorkChangeObserver observer) {
        if (netChangeObserverArrayList == null) {
            netChangeObserverArrayList = new ArrayList<>();
        }
        netChangeObserverArrayList.add(observer);
    }

    /**
     * 注销网络连接观察者
     *
     * @param observer
     */
    public void removeRegisterObserver(NetWorkChangeObserver observer) {
        if (netChangeObserverArrayList != null) {
            netChangeObserverArrayList.remove(observer);
        }
    }
}
