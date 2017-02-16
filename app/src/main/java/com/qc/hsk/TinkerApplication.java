package com.qc.hsk;


import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.qc.corelibrary.utils.CatchCrash;
import com.qc.corelibrary.utils.network.NetWorkChangeObserver;
import com.qc.corelibrary.utils.network.NetWorkStateReceiver;
import com.qc.corelibrary.utils.network.NetworkUtils;
import com.qc.hsk.tinker.Log.MyLogImp;
import com.qc.hsk.tinker.util.SampleApplicationContext;
import com.qc.hsk.tinker.util.TinkerManager;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * <ul>
 * <li>功能职责：</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-11-08
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.qc.hsk.HSKApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class TinkerApplication extends DefaultApplicationLike {
    private static final String TAG = "Tinker.TinkerApplication";

    public TinkerApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                             long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        SampleApplicationContext.application = getApplication();
        SampleApplicationContext.context = getApplication();
        TinkerManager.setTinkerApplicationLike(this);
        //设置崩溃删除补丁处理
        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        TinkerInstaller.setLogIml(new MyLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CatchCrash.getInstance().init(getApplication());
        NetWorkStateReceiver.registerObserver(taNetChangeObserver);
    }

    NetWorkChangeObserver taNetChangeObserver = new NetWorkChangeObserver() {
        @Override
        public void onConnect(NetworkUtils.NetType type) {
            super.onConnect(type);
            TinkerApplication.this.onConnect(type);
        }

        @Override
        public void onDisConnect() {
            super.onDisConnect();
            TinkerApplication.this.onDisConnect();

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