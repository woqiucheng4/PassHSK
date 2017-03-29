package com.qc.hsk.utils;

import android.util.Log;

import com.qc.hsk.BuildConfig;


/**
 * 对日志进行管理 在DeBug模式开启，其它模式关闭
 */
public class LogUtils {

	public static boolean isDebug = BuildConfig.LOG_DEBUG;

	/**
	 * 错误
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.e(tag, msg + "");
		}
	}

	/**
	 * 信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (isDebug) {
			Log.i(tag, msg + "");
		}
	}

	/**
	 * 警告
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag, String msg) {
		if (isDebug) {
			Log.w(tag, msg + "");
		}
	}

	public static void v(String tag, String msg) {
		if (isDebug) {
			Log.v(tag, msg + "");
		}
	}
	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg + "");
		}
	}
}
