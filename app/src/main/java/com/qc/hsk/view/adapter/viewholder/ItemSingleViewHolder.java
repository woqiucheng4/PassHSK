package com.qc.hsk.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qc.corelibrary.view.adapter.viewholder.BaseViewHolder;
import com.qc.hsk.R;


/**
 * <ul>
 * <li>功能职责：</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-11-03
 */
public class ItemSingleViewHolder extends BaseViewHolder {

    public TextView textView;
    public TextView definitionTv;
    public TextView pinyinTv;
    public ImageView soundImg;

    public ItemSingleViewHolder(View view) {
        super(view);
        textView = (TextView) view.findViewById(R.id.textTv);
        definitionTv = (TextView) view.findViewById(R.id.definitionTv);
        pinyinTv = (TextView) view.findViewById(R.id.pinyinTv);
        soundImg = (ImageView) view.findViewById(R.id.soundImg);
    }
}
