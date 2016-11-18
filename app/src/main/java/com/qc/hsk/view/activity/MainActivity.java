package com.qc.hsk.view.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.qc.corelibrary.network.BaseHttpPresenter;
import com.qc.corelibrary.network.InvokerCallBack;
import com.qc.corelibrary.utils.PermissionUtils;
import com.qc.corelibrary.view.BaseActivity;
import com.qc.corelibrary.view.widget.SwipeRecyclerView;
import com.qc.hsk.R;
import com.qc.hsk.network.HttpInterface;
import com.qc.hsk.network.HttpPresenter;
import com.qc.hsk.network.bean.CharacterListBean;
import com.qc.hsk.network.bean.VersionBean;
import com.qc.hsk.network.value.Character;
import com.qc.hsk.network.value.Version;
import com.qc.hsk.speech.SpeechManager;
import com.qc.hsk.view.adapter.SampleAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements SwipeRecyclerView.RefreshLoadMoreListener, SampleAdapter.OnSpeekListener {

    private SpeechManager speechManager;

    private SwipeRecyclerView mSwipeRecyclerView;

    private List<Character> list = new ArrayList<>();

    private SampleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitleText(R.string.app_name);
    }

    @Override
    protected void initCustomView() {
        PermissionUtils.requestPermission(MainActivity.this,//
                PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, MainActivity.this);
        mSwipeRecyclerView = (SwipeRecyclerView) findViewById(R.id.swipeRecyclerView);
        adapter = new SampleAdapter(this, list);
        mSwipeRecyclerView.setAdapter(adapter);
        //        mSwipeRecyclerView.setDistanceToTriggerSync(500);
        mSwipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //        mSwipeRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mSwipeRecyclerView.setRefreshLoadMoreListener(this);
        setHomeAsUpIndicator(R.drawable.ic_menu);
        setShowDrawerLayout(true);
        initNavigationView();

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
                        Toast.makeText(MainActivity.this, "about", Toast.LENGTH_LONG).show();
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
     * 请求强制更新参数
     *
     * @throws Exception
     */
    private void requestVersionUpdateInfo() throws Exception {
        final BaseHttpPresenter<String> presenter = HttpPresenter.getInstance().setShowLoading(false).setShowPromptDialog(false);
        Object[] params = new Object[]{};
        InvokerCallBack<String> callback = new InvokerCallBack<String>() {
            @Override
            public void notifySuccess(String result) {
                VersionBean versionBean = JSON.parseObject(result, VersionBean.class);
                if (versionBean != null && versionBean.getValue() != null) {
                    Version mVersion = versionBean.getValue();
                }
            }

            @Override
            public void notifyFailed(int code, String errorMessage) {
            }
        };
        presenter.setShowLoading(true).setShowPromptDialog(true).loadDatas(this,//
                HttpInterface.getVersionInfoURL(), //
                null, //
                callback, params);
    }

    /**
     * 请求强制更新参数
     *
     * @throws Exception
     */
    private void requestHSKCharacters() throws Exception {
        final BaseHttpPresenter<String> presenter = HttpPresenter.getInstance().setShowLoading(false).setShowPromptDialog(false);
        Object[] params = new Object[]{};
        InvokerCallBack<String> callback = new InvokerCallBack<String>() {
            @Override
            public void notifySuccess(String result) {
                CharacterListBean characterListBean = JSON.parseObject(result, CharacterListBean.class);
                if (characterListBean != null && characterListBean.getValue() != null)
                    list.addAll(characterListBean.getValue().getCharacterList());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void notifyFailed(int code, String errorMessage) {
            }
        };
        presenter.setShowLoading(true).setShowPromptDialog(true).loadDatas(this,//
                HttpInterface.getHSKOneURL(), //
                null, //
                callback, params);
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
                speechManager = new SpeechManager(this);
                try {
                    requestHSKCharacters();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}