/**
 * 
 */
package com.qc.corelibrary.network;


/**
 * @author CreditEase/FSO
 * 
 */
public interface InvokerCallBack<T> {

	/**
	 * 通知反馈成功。
	 * 
	 */
	void notifySuccess(T result);

	/**
	 * 通知反馈失败。
	 * 
	 * @param code
	 *            错误码
	 * @param errorMessage
	 *            错误信息
	 */
	void notifyFailed(int code, String errorMessage);

}
