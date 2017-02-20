package com.qc.hsk.view.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.qc.corelibrary.utils.PermissionUtils;
import com.qc.corelibrary.view.BaseActivity;
import com.qc.corelibrary.view.widget.SwipeRecyclerView;
import com.qc.hsk.R;
import com.qc.hsk.network.bean.CharacterListBean;
import com.qc.hsk.network.value.Character;
import com.qc.hsk.speech.SpeechManager;
import com.qc.hsk.utils.FileUtils;
import com.qc.hsk.view.activity.about.AboutUsActivity;
import com.qc.hsk.view.activity.about.HSKInfoActivity;
import com.qc.hsk.view.adapter.WordAdapter;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements SwipeRecyclerView.RefreshLoadMoreListener, WordAdapter.OnSpeekListener {

    private static final String WORD_LIST_NAME = "word_json.txt";

    private String sdCardWordPath;

    private SpeechManager speechManager;

    private SwipeRecyclerView mSwipeRecyclerView;

    private List<Character> list = new ArrayList<>();

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
        //        mSwipeRecyclerView.setDistanceToTriggerSync(500);
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
                    case R.id.about:
                        gotoAboutUsActivity();
                        break;
                }
                menuItem.setChecked(true);
                closeDrawerLayout();
                return true;
            }
        });
    }

    private void gotoHSKInfoActivity() {
        startActivity(new Intent(this, HSKInfoActivity.class));
    }

    private void gotoAboutUsActivity() {
        startActivity(new Intent(this, AboutUsActivity.class));
    }

    private void initDrawerHeaderView() {
        View headerView = setNavigationViewHeader(R.layout.navigation_header);
        headerView.findViewById(R.id.portrait_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),//
                        Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "patch_signed_7zip.apk");
                //                Toast.makeText(MainActivity.this, "头像", Toast.LENGTH_LONG).show();
            }
        });
        headerView.findViewById(R.id.portrait_name_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Toast.makeText(MainActivity.this, "姓名", Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "Tinker", Toast.LENGTH_LONG).show();
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
        CharacterListBean characterListBean = JSON.parseObject(jsonWord, CharacterListBean.class);
        if (characterListBean != null && characterListBean.getValue() != null)
            list.addAll(characterListBean.getValue().getCharacterList());
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onRefresh() {
        mSwipeRecyclerView.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void onSpeek(View view) {
        String context = ((TextView) view).getText().toString();
        speechManager.speak(context);
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        switch (requestCode) {
            case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                sdCardWordPath = Environment.getExternalStorageDirectory() + File.separator + WORD_LIST_NAME;
                FileUtils.copyFromAssetsToSdcard(this, true, WORD_LIST_NAME, sdCardWordPath);
                speechManager = new SpeechManager(this);
                requestHSKCharacters();
                break;
            default:
                break;
        }
    }
}