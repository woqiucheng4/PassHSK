package com.qc.corelibrary.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 提示对话框<br>
 * 1.单按钮 2，两按钮 1.默认提示，2，自行添加内容
 * 
 * @author CreditEase/FSO
 * 
 */
public class PromptDialog {

	public static final String TAG_DIALOG_CONTENT_VIEW = "dialogContentView";
	public static final String TAG_PROMPT_TEXT = "promptText";
	public static final String TAG_LEFT_TEXT = "leftText";
	public static final String TAG_RIGHT_TEXT = "rightText";
	public static final String TAG_LINE = "line";

	private Context mContext;

	private LinearLayout contentLayout;

	private Dialog dialog;

	private TextView mRightText, mLeftText, mPromptText;

	private View line;

	private int width;

	/** 默认按键监听 */
	private final OnClickListener mDefaultListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

	/**
	 * @param context
	 */
	public PromptDialog(Context context) {
		super();
		mContext = context;
	}
	public PromptDialog(Context context, int theme, int rootViewResId) {
		super();
		mContext = context;
		initDialog(theme, rootViewResId);
	}

	/** 初始化对话框 **/
	public void initDialog(int theme, int rootViewResId) {
		dialog = new Dialog(mContext, theme);
		View rootView = LayoutInflater.from(mContext).inflate(rootViewResId,
				null);
		initView(rootView);
		dialog.setCanceledOnTouchOutside(false);
		width = mContext.getResources().getDisplayMetrics().widthPixels;
		LayoutParams params = new LayoutParams((int) (width * 0.8),
				LayoutParams.WRAP_CONTENT);
		dialog.addContentView(rootView, params);
	}

	/**
	 * 初始化控件
	 */
	private void initView(View view) {
		contentLayout = (LinearLayout) view
				.findViewWithTag(TAG_DIALOG_CONTENT_VIEW);
		mRightText = (TextView) view.findViewWithTag(TAG_RIGHT_TEXT);
		mLeftText = (TextView) view.findViewWithTag(TAG_LEFT_TEXT);
		mPromptText = (TextView) view.findViewWithTag(TAG_PROMPT_TEXT);
		line = view.findViewWithTag(TAG_LINE);
		resetListeners();
	}

	/**
	 * 设置提示信息
	 */
	public void setPromptText(String text) {
		if (text == null) {
			return;
		}
		mPromptText.setText(text);
	}

	public void setPromptText(int resId) {
		mPromptText.setText(resId);
	}

	/**
	 * 设置左侧按钮文字
	 */
	public void setLeftBtnText(String text) {
		if (text == null) {
			return;
		}
		mLeftText.setText(text);
	}

	public void setLeftBtnText(int resId) {
		mLeftText.setText(resId);
	}

	/**
	 * 设置右侧按钮文字
	 */
	public void setRightBtnText(String text) {
		if (text == null) {
			return;
		}
		mRightText.setText(text);
	}

	public void setRightBtnText(int resId) {
		mRightText.setText(resId);
	}

	/**
	 * 设置左侧按钮点击事件
	 */
	public void setLeftClickListener(OnClickListener listener) {
		if (listener != null) {
			mLeftText.setOnClickListener(listener);
		}
	}

	/**
	 * 设置右侧按钮点击事件
	 */
	public void setRightClickListener(OnClickListener listener) {
		if (listener != null) {
			mRightText.setOnClickListener(listener);
		}
	}

	/**
	 * 添加自定义的布局
	 * 
	 * @param view
	 */
	public void addContentView(View view) {
		mPromptText.setVisibility(View.GONE);
		contentLayout.addView(view);
	}

	/**
	 * 添加自定义的布局
	 * 
	 * @param layoutId
	 */
	public void addContentView(int layoutId) {
		mPromptText.setVisibility(View.GONE);
		View view = LayoutInflater.from(mContext).inflate(layoutId, null);
		contentLayout.addView(view);
	}

	/**
	 * 设置提示按钮个数
	 */
	public void setSingleBtn(boolean isSingleBtn, int leftButtonBackgroundResId) {
		if (isSingleBtn) {
			mRightText.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			mLeftText.setText("我知道了");
			mLeftText.setBackgroundResource(leftButtonBackgroundResId);
		} else {
			mRightText.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
			mLeftText.setBackgroundResource(leftButtonBackgroundResId);
		}
	}

	/**
	 * 关闭事件监听
	 */
	public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
		if (listener == null) {
			return;
		}
		dialog.setOnDismissListener(listener);
	}

	/**
	 * 显示对话框
	 */
	public synchronized void show() {
		try {
			if (dialog != null && !dialog.isShowing()) {
				dialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 关闭对话框
	 */
	public synchronized void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * 设置是否能被back键关闭。
	 * 
	 * @param cancelable
	 */
	public synchronized void setCancelable(boolean cancelable) {
		if (dialog != null) {
			dialog.setCancelable(cancelable);
		}
	}

	/**
	 * 重置左右按钮监听
	 */
	public synchronized void resetListeners() {
		mLeftText.setOnClickListener(mDefaultListener);
		mRightText.setOnClickListener(mDefaultListener);
	}

	/**
	 * @return
	 */
	public Context getContext() {
		return mContext;
	}

}
