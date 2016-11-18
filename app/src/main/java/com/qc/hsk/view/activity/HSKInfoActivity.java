package com.qc.hsk.view.activity;

import android.os.Bundle;

import com.qc.corelibrary.view.BaseActivity;
import com.qc.hsk.R;

public class HSKInfoActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hskinfo);
        setTitleText("About HSK");
    }

    /**
     * 自定义页面
     */
    @Override
    protected void initCustomView() {

    }
}
