package com.qc.hsk;

import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.qc.corelibrary.network.BaseHttpPresenter;
import com.qc.corelibrary.network.InvokerCallBack;
import com.qc.corelibrary.view.BaseActivity;
import com.qc.corelibrary.view.widget.SwipeRecyclerView;
import com.qc.hsk.adapter.SampleAdapter;
import com.qc.hsk.network.HttpPresenter;
import com.qc.hsk.network.bean.CharacterListBean;
import com.qc.hsk.network.bean.VersionBean;
import com.qc.hsk.network.value.Character;
import com.qc.hsk.network.value.Version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements SwipeRecyclerView.RefreshLoadMoreListener, SpeechSynthesizerListener, SampleAdapter.OnSpeekListener {
    // 语音合成客户端
    private SpeechSynthesizer mSpeechSynthesizer;

    private SwipeRecyclerView mSwipeRecyclerView;

    private List<Character> list = new ArrayList<>();

    private SampleAdapter adapter;

    private String mSampleDirPath;
    private static final String SAMPLE_DIR_NAME = "hskTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";

    @Override
    protected void initCustomView() {
        initialEnv();
        setContentView(R.layout.activity_main);
        startTTS();
        mSwipeRecyclerView = (SwipeRecyclerView) findViewById(R.id.swipeRecyclerView);
        adapter = new SampleAdapter(this, list);
        mSwipeRecyclerView.setAdapter(adapter);
        //        mSwipeRecyclerView.setDistanceToTriggerSync(500);
        mSwipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //        mSwipeRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mSwipeRecyclerView.setRefreshLoadMoreListener(this);

        try {
            requestHSKCharacters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 初始化语音合成客户端并启动
    private void startTTS() {
        // 获取语音合成对象实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        // 设置context
        mSpeechSynthesizer.setContext(this);
        // 设置语音合成状态监听器
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 语音开发者平台上注册应用得到的App ID (离线授权)
        mSpeechSynthesizer.setAppId("8876475");
        // 语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        mSpeechSynthesizer.setApiKey("UHDbLtGHjKc4aztGs7B9S8Xw", "f3421791936c81737a9af280dc3431c9");
        // 文本模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        // 初始化语音合成器
        mSpeechSynthesizer.initTts(TtsMode.MIX);
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
                "https://raw.githubusercontent.com/woqiucheng4/hsk/master/version", //
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
                "https://raw.githubusercontent.com/woqiucheng4/HSKData/master/hsk1", //
                null, //
                callback, params);
    }


    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    public void onSynthesizeStart(String s) {
        // 监听到合成开始，在此添加相关操作
        Log.i("nnn", "监听到合成开始");
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
        // 监听到有合成数据到达，在此添加相关操作
        Log.i("nnn", "监听到有合成数据到达");
    }

    @Override
    public void onSynthesizeFinish(String s) {
        // 监听到合成结束，在此添加相关操作
        Log.i("nnn", "监听到合成结束");
    }

    @Override
    public void onSpeechStart(String s) {
        // 监听到合成并播放开始，在此添加相关操作
        Log.i("nnn", "监听到合成并播放开始");
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
        // 监听到播放进度有变化，在此添加相关操作
        Log.i("nnn", "监听到播放进度有变化");
    }

    @Override
    public void onSpeechFinish(String s) {
        // 监听到播放结束，在此添加相关操作
        Log.i("nnn", "监听到播放结束");
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        // 监听到出错，在此添加相关操作
        Log.i("nnn", "监听到出错==" + speechError.code + ",," + speechError.description);
    }

    @Override
    public void onSpeek(View view) {
        mSpeechSynthesizer.speak(((TextView) view).getText().toString());
    }
}