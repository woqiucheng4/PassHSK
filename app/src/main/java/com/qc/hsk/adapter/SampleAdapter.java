package com.qc.hsk.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.qc.corelibrary.view.adapter.BaseAdapter;
import com.qc.corelibrary.view.adapter.OnItemClickListener;
import com.qc.corelibrary.view.adapter.viewholder.BaseViewHolder;
import com.qc.hsk.R;
import com.qc.hsk.adapter.viewholder.ItemSingleViewHolder;
import com.qc.hsk.network.value.Character;

import java.util.List;


/**
 * <ul>
 * <li>功能职责：</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-08-26
 */
public class SampleAdapter extends BaseAdapter<Character, ItemSingleViewHolder> implements OnItemClickListener {

    private OnSpeekListener speekListener;

    public SampleAdapter(Context context, List<Character> list) {
        super(context, list);
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
    public void convert(ItemSingleViewHolder holder, Character item) {
        final ItemSingleViewHolder itemHolder = holder;
        itemHolder.textView.setText(item.getCharacterName());
        itemHolder.definitionTv.setText(item.getDefinition());
        itemHolder.soundImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speekListener.onSpeek(itemHolder.textView);
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
        Toast.makeText(context, "跳转到详情", Toast.LENGTH_SHORT).show();
    }

    /**
     * RefreshView item 单击事件接口
     */
    public interface OnSpeekListener {

        void onSpeek(View view);

    }


}

