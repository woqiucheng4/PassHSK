package com.qc.hsk.view.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.qc.corelibrary.utils.PermissionUtils;
import com.qc.corelibrary.view.BaseActivity;
import com.qc.corelibrary.view.widget.SwipeRecyclerView;
import com.qc.hsk.R;
import com.qc.hsk.network.bean.WordListBean;
import com.qc.hsk.network.value.Sentence;
import com.qc.hsk.network.value.Word;
import com.qc.hsk.network.value.WordDetail;
import com.qc.hsk.speech.SpeechManager;
import com.qc.hsk.utils.FileUtils;
import com.qc.hsk.view.activity.about.AboutUsActivity;
import com.qc.hsk.view.activity.about.HSKInfoActivity;
import com.qc.hsk.view.activity.setting.SettingActivity;
import com.qc.hsk.view.adapter.WordAdapter;
import com.qc.hsk.view.adapter.viewholder.ItemSingleViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements SwipeRecyclerView.RefreshLoadMoreListener, WordAdapter.OnSpeekListener {

    private static final String WORD_LIST_NAME = "word_json.txt";
    private static final String WORD_DETAIL_LIST_NAME = "word_detail.txt";

    private String sdCardWordPath;
    private String sdCardWordDetailPath;

    private SpeechManager speechManager;

    private SwipeRecyclerView mSwipeRecyclerView;

    private List<Word> list = new ArrayList<>();

    private WordAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitleText(R.string.app_name);
    }

    @Override
    protected void initCustomView() {
        mSwipeRecyclerView = (SwipeRecyclerView) findViewById(R.id.swipeRecyclerView);
        adapter = new WordAdapter(this, list);
        mSwipeRecyclerView.setAdapter(adapter);
        mSwipeRecyclerView.setDistanceToTriggerSync(500);
        mSwipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //        mSwipeRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mSwipeRecyclerView.setRefreshLoadMoreListener(this);
        setHomeAsUpIndicator(R.drawable.ic_menu);
        setShowDrawerLayout(true);
        initNavigationView();
        PermissionUtils.requestPermission(MainActivity.this,//
                PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, MainActivity.this);
    }

    private void initNavigationView() {
        //初始化侧边栏的headerview
        initDrawerHeaderView();
        //设置侧边栏menu布局
        setNavigationViewMenu(R.menu.drawer_view);
        //        设置侧边栏item文字颜色
        NavigationView navigationView = getNavigationView();
        ColorStateList csl = getResources().getColorStateList(R.color.navigation_menu_item_color);
        navigationView.setItemTextColor(csl);
        //MenuItem点击事件
        setOnNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        gotoHSKInfoActivity();
                        break;
                    case R.id.nav_messages:
                        gotoHSKInfoActivity();
                        break;
                    case R.id.about:
                        gotoAboutUsActivity();
                        break;
                    case R.id.setting:
                        gotoSettingActivity();
                        break;
                }
                //                menuItem.setChecked(true);
                closeDrawerLayout();
                return true;
            }
        });
    }

    private void gotoHSKInfoActivity() {
        startActivity(new Intent(this, HSKInfoActivity.class));
    }

    private void gotoSettingActivity() {
        startActivity(new Intent(this, SettingActivity.class));
    }

    private void gotoAboutUsActivity() {
        startActivity(new Intent(this, AboutUsActivity.class));
    }

    private void initDrawerHeaderView() {
        View headerView = setNavigationViewHeader(R.layout.navigation_header);
        headerView.findViewById(R.id.portrait_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "头像", Toast.LENGTH_LONG).show();
            }
        });
        headerView.findViewById(R.id.portrait_name_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "姓名", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 请求单词列表
     *
     * @throws Exception
     */
    private void requestHSKCharacters() {
        String jsonWord = FileUtils.readFile(sdCardWordPath, "utf-8").toString();
        WordListBean characterListBean = JSON.parseObject(jsonWord, WordListBean.class);
        if (characterListBean != null && characterListBean.getValue() != null)
            list.addAll(characterListBean.getValue().getCharacterList());
        adapter.notifyDataSetChanged();
    }

    /**
     * 请求单词详情列表
     *
     * @throws Exception
     */
    private void requestHSKCharactersDetail() {
        String jsonWordDetail = FileUtils.readFile(sdCardWordDetailPath, "utf-8").toString();
        String[] arrays = jsonWordDetail.split("#");
        int componentsIndex = 0;
        int sentencesIndex = 0;
        for (int i = 0; i < arrays.length; i++) {
            String word = arrays[i];
            if (i == 0) {
                WordDetail wordDetail = new WordDetail();
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
                Sentence sentence=null;
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
                        if((k-sentencesIndex)%2!=0){
                            sentence=new Sentence();
                            sentence.setHanyu(wordArray[k]);
                        }else{
                            sentence.setHanyupinyin(wordArray[k]);
                        }
                        sentencesList.add(sentence);
                    }
                }
                Log.i("nnn","detail=="+wordDetail.toString());
            }
        }
    }


    @Override
    public void onRefresh() {
        mSwipeRecyclerView.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void onSpeek(ItemSingleViewHolder itemHolder) {
        String context = itemHolder.textView.getText().toString();

        int result = speechManager.speak(context);
        if (result < 0) {
            Log.i("nnn", "error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
        } else {
            Log.i("nnn", "result==" + result);
        }
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        switch (requestCode) {
            case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                sdCardWordPath = Environment.getExternalStorageDirectory() + File.separator + WORD_LIST_NAME;
                sdCardWordDetailPath = Environment.getExternalStorageDirectory() + File.separator + WORD_DETAIL_LIST_NAME;
                FileUtils.copyFromAssetsToSdcard(this, true, WORD_LIST_NAME, sdCardWordPath);
                FileUtils.copyFromAssetsToSdcard(this, true, WORD_DETAIL_LIST_NAME, sdCardWordDetailPath);
                speechManager = new SpeechManager(this);
                requestHSKCharacters();
                requestHSKCharactersDetail();
                break;
            default:
                break;
        }
    }
}