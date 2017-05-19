package com.qc.hsk.view.activity.word;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;

import com.qc.corelibrary.view.BaseActivity;
import com.qc.hsk.R;
import com.qc.hsk.constants.Constants;
import com.qc.hsk.network.value.Word;

public class WordDetailActivity extends BaseActivity {

    private Word mWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_word_detail);
        showCollapsingTitleBarBackImageView();
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(mWord.getCharacterName());
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
        //        String gifUrl="https://raw.githubusercontent.com/woqiucheng4/HSKData/master/ai.gif";
        //        setCollapsingTitleBarBackImage(gifUrl);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mWord= (Word) intent.getSerializableExtra(Constants.IntentBundleKey.WOORD_DETAIL);
        }
    }

    /**
     * 自定义页面
     */
    @Override
    protected void initCustomView() {

    }
}
