package com.qc.hsk.view.activity.word;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.TextView;

import com.qc.corelibrary.view.BaseActivity;
import com.qc.hsk.R;
import com.qc.hsk.constants.Constants;
import com.qc.hsk.network.value.Sentence;
import com.qc.hsk.network.value.Word;
import com.qc.hsk.network.value.WordDetail;

import butterknife.Bind;

public class WordDetailActivity extends BaseActivity {

    @Bind(R.id.in_english_name_id)
    public TextView englishName;
    @Bind(R.id.in_english_value_id)
    public TextView englishValue;
    @Bind(R.id.sentences_name_id)
    public TextView sentencesName;
    @Bind(R.id.sentences_value_id)
    public TextView sentencesValue;

    private Word mWord;

    private WordDetail wordDetail;

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
            mWord = (Word) intent.getSerializableExtra(Constants.IntentBundleKey.WOORD_DETAIL);
        }
        wordDetail = new WordDetail();
    }

    /**
     * 自定义页面
     */
    @Override
    protected void initCustomView() {


    }



    private void refreshView() {
        //
        englishName.setText(wordDetail.getCharacterName() + "in English");
        englishValue.setText(getEnglishDefinition());
        //
        sentencesName.setText("Sentences examples with " + wordDetail.getCharacterName());
        sentencesValue.setText(getSentences());
    }

    private String getEnglishDefinition() {
        StringBuilder englishSb = new StringBuilder();
        int length = wordDetail.getEnglish().size();
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                englishSb.append(wordDetail.getEnglish().get(i));
            } else {
                englishSb.append(wordDetail.getEnglish().get(i) + "\n");
            }
        }
        return englishSb.toString();
    }


    private String getSentences() {
        StringBuilder sentenceSb = new StringBuilder();
        int length = wordDetail.getSentences().size();
        for (int i = 0; i < length; i++) {
            Sentence sentence = wordDetail.getSentences().get(i);
            if (i == length - 1) {
                sentenceSb.append(sentence.getHanyu() + "\n" + sentence.getHanyupinyin());
            } else {
                sentenceSb.append(sentence.getHanyu() + "\n" + sentence.getHanyupinyin() + "\n");
            }
        }
        return sentenceSb.toString();
    }

}
