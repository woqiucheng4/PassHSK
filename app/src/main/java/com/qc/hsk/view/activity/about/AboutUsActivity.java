package com.qc.hsk.view.activity.about;

import android.os.Bundle;

import com.qc.corelibrary.view.BaseActivity;
import com.qc.hsk.R;

public class AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setTitleText("About Us");
    }

    /**
     * 自定义页面
     */
    @Override
    protected void initCustomView() {

    }
}
