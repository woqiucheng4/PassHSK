package com.qc.hsk.view.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.qc.corelibrary.view.BaseActivity;
import com.qc.hsk.R;

import butterknife.Bind;
import butterknife.OnClick;

public class VoiceSpeakerSettingActivity extends BaseActivity {

    @Bind(R.id.voice_settings_id)
    public RelativeLayout voiceSettingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitleText("Settings");
    }


    @OnClick({R.id.voice_settings_id})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voice_settings_id:
                break;
            default:
                break;
        }
    }

    /**
     * 自定义页面
     */
    @Override
    protected void initCustomView() {

    }
}
