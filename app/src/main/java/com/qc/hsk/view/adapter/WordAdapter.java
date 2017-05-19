package com.qc.hsk.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.qc.corelibrary.view.adapter.BaseAdapter;
import com.qc.corelibrary.view.adapter.OnItemClickListener;
import com.qc.corelibrary.view.adapter.viewholder.BaseViewHolder;
import com.qc.hsk.R;
import com.qc.hsk.constants.Constants;
import com.qc.hsk.network.value.Word;
import com.qc.hsk.view.activity.word.WordDetailActivity;
import com.qc.hsk.view.adapter.viewholder.ItemSingleViewHolder;

import java.util.List;


/**
 * <ul>
 * <li>功能职责：单词adapter</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-08-26
 */
public class WordAdapter extends BaseAdapter<Word, ItemSingleViewHolder> implements OnItemClickListener {

    private OnSpeekListener speekListener;


    private Context mContext;

    public WordAdapter(Context context, List<Word> list) {
        super(context, list);
        mContext = context;
        speekListener = (OnSpeekListener) context;
    }

    /**
     * 获取item布局
     *
     * @param viewType
     * @return
     */
    @Override
    public int getItemLayoutResId(int viewType) {
        return R.layout.item_recyclerview_single;
    }

    /**
     * 获取Item的ViewHolder
     *
     * @param view
     * @param viewType
     * @return
     */
    @Override
    public BaseViewHolder getItemViewHolder(View view, int viewType) {
        return new ItemSingleViewHolder(view);
    }

    /**
     * 界面数据处理
     *
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    public void convert(ItemSingleViewHolder holder, Word item) {
        final ItemSingleViewHolder itemHolder = holder;
        itemHolder.textView.setText(item.getCharacterName());
        itemHolder.pinyinTv.setText(item.getPinyin());
        itemHolder.definitionTv.setText(item.getDefinition());
        itemHolder.soundImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speekListener.onSpeek(itemHolder);
            }
        });
        setOnItemClickListener(this);
    }

    /**
     * 获取更多item类型
     *
     * @param position
     * @return
     */
    @Override
    public int getMoreItemViewType(int position) {
        return 0;
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(mContext, WordDetailActivity.class);
        intent.putExtra(Constants.IntentBundleKey.WOORD_DETAIL, getItem(position));
        mContext.startActivity(intent);
    }

    /**
     * RefreshView item 单击事件接口
     */
    public interface OnSpeekListener {

        void onSpeek(ItemSingleViewHolder itemHolder);

    }


}

