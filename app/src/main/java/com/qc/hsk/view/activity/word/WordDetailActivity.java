package com.qc.hsk.view.activity.word;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.TextView;

import com.qc.corelibrary.view.BaseActivity;
import com.qc.hsk.R;
import com.qc.hsk.constants.Constants;
import com.qc.hsk.network.value.Sentence;
import com.qc.hsk.network.value.Word;
import com.qc.hsk.network.value.WordDetail;
import com.qc.hsk.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class WordDetailActivity extends BaseActivity {

    private static final String WORD_DETAIL_LIST_NAME = "word_detail.txt";

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
        setContentView(R.layout.activity_word_detail);
        initData();
//        showCollapsingTitleBarBackImageView();
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(mWord.getCharacterName());
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
        //                String gifUrl="https://raw.githubusercontent.com/woqiucheng4/HSKData/master/ai.gif";
        //                setCollapsingTitleBarBackImage(gifUrl);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mWord = (Word) intent.getSerializableExtra(Constants.IntentBundleKey.WOORD_DETAIL);
        }
        requestHSKCharactersDetail();
        refreshView();
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


    /**
     * 请求单词详情列表
     *
     * @throws Exception
     */
    private void requestHSKCharactersDetail() {
        String sdCardWordDetailPath = Environment.getExternalStorageDirectory() + File.separator + WORD_DETAIL_LIST_NAME;
        String jsonWordDetail = FileUtils.readFile(sdCardWordDetailPath, "utf-8").toString();
        String[] arrays = jsonWordDetail.split("#");
        int componentsIndex = 0;
        int sentencesIndex = 0;
        for (int i = 0; i < arrays.length; i++) {
            String word = arrays[i];
            if (i == 0) {
                wordDetail = new WordDetail();
                List<String> englishList = new ArrayList<>();
                List<String> componentsList = new ArrayList<>();
                List<Sentence> sentencesList = new ArrayList<>();
                wordDetail.setEnglish(englishList);
                wordDetail.setComponents(componentsList);
                wordDetail.setSentences(sentencesList);
                String[] wordArray = word.split("\n");
                for (int j = 0; j < wordArray.length; j++) {
                    if (wordArray[j].contains("Components")) {
                        componentsIndex = j;
                    }
                    if (wordArray[j].contains("Sentences")) {
                        sentencesIndex = j;
                    }
                }
                Sentence sentence = null;
                for (int k = 0; k < wordArray.length; k++) {
                    if (k == 0) {
                        wordDetail.setCharacterName(wordArray[k]);
                    }
                    if (k == 1) {
                        wordDetail.setPinyin(wordArray[k]);
                    }
                    if (k > 1 && k < componentsIndex) {
                        englishList.add(wordArray[k]);
                    }
                    if (k > componentsIndex && k < sentencesIndex) {
                        componentsList.add(wordArray[k]);
                    }
                    if (k > sentencesIndex) {
                        if ((k - sentencesIndex) % 2 != 0) {
                            sentence = new Sentence();
                            sentence.setHanyu(wordArray[k]);
                        } else {
                            sentence.setHanyupinyin(wordArray[k]);
                        }
                        sentencesList.add(sentence);
                    }
                }
            }
        }
    }


}
