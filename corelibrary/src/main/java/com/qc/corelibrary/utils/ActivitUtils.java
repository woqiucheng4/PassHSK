package com.qc.corelibrary.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * <ul>
 * <li>功能职责：Activity集合管理</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-08-08
 */
public class ActivitUtils {

    private static Stack<Activity> activityStack;
    private static ActivitUtils instance;

    private ActivitUtils() {

    }

    /**
     * 单例模式
     */
    public static ActivitUtils getInstance() {
        if (instance == null) {
            instance = new ActivitUtils();
        }
        return instance;
    }


    /**
     * 获得当前栈顶Activity
     */
    public Activity getCurrentActivity() {
        if (activityStack.empty()) {
            return null;
        }
        Activity activity = (Activity) activityStack.lastElement();
        return activity;
    }

    /**
     * 将当前Activity推入栈中
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 退出栈顶Activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 退出栈中所有Activity
     */
    public void popAllActivity() {
        while (true) {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 退出栈中除某个Activity之外所有activity
     */
    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

}
