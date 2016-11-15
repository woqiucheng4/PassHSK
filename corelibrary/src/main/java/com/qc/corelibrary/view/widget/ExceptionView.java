package com.qc.corelibrary.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qc.corelibrary.R;


/**
 * Implementation of App Widget functionality.
 */
public class ExceptionView {

    private OnExceptionViewClickListener listener;


    private LinearLayout mExceptionView;

    /**
     * 异常显示的图片
     */
    private ImageView mExceptionImageView;

    /**
     * 异常显示的文字
     */
    private TextView mExceptionTextView;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public ExceptionView(Context context) {
        listener = (OnExceptionViewClickListener) context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mExceptionView = (LinearLayout) inflater.inflate(R.layout.view_exception, null);
        mExceptionImageView = (ImageView) mExceptionView.findViewById(R.id.exception_imageview);
        mExceptionTextView = (TextView) mExceptionView.findViewById(R.id.exception_textview);
        setClickListener();
        hideExceptionView();
    }

    /**
     * 设置异常显示图片id
     *
     * @param drawableId
     */
    public void setExceptionImage(int drawableId) {
        mExceptionImageView.setImageResource(drawableId);
    }

    /**
     * 设置异常显示图片drawable
     *
     * @param drawable
     */
    public void setExceptionImage(Drawable drawable) {
        mExceptionImageView.setImageDrawable(drawable);
    }

    /**
     * 设置异常显示图片bitmap
     *
     * @param bitmap
     */
    public void setExceptionImage(Bitmap bitmap) {
        mExceptionImageView.setImageBitmap(bitmap);
    }

    /**
     * 设置异常显示文字Id
     *
     * @param stringId
     */
    public void setExceptionText(int stringId) {
        mExceptionTextView.setText(stringId);
    }

    /**
     * 设置异常显示文字字段
     *
     * @param exceptionStr
     */
    public void setExceptionText(String exceptionStr) {
        mExceptionTextView.setText(exceptionStr);
    }

    /**
     * 获取异常view
     *
     * @return
     */
    public View getExceptionView() {
        return mExceptionView;
    }

    /**
     * 获取异常ExceptionTextView
     *
     * @return
     */
    public View getExceptionTextView() {
        return mExceptionTextView;
    }

    /**
     * 获取异常ExceptionImageView
     *
     * @return
     */
    public View getExceptionImageView() {
        return mExceptionImageView;
    }

    /**
     * 隐藏ExceptionImageView
     *
     * @return
     */
    public void hideExceptionImageView() {
        mExceptionImageView.setVisibility(View.GONE);
    }

    /**
     * 隐藏ExceptionImageView
     *
     * @return
     */
    public void hideExceptionTextView() {
        mExceptionTextView.setVisibility(View.GONE);
    }

    /**
     * 隐藏ExceptionView
     *
     * @return
     */
    public void hideExceptionView() {
        mExceptionView.setVisibility(View.GONE);
    }

    /**
     * 显示exceptionImageView
     *
     * @return
     */
    public void showExceptionView() {
        mExceptionView.setVisibility(View.VISIBLE);
    }

    private void setClickListener() {
        mExceptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dealExceotionView();
            }
        });
    }

    public interface OnExceptionViewClickListener {
        void dealExceotionView();
    }


}

