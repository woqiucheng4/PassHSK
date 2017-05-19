package com.qc.hsk.speech;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import static com.baidu.tts.client.SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85;
import static com.baidu.tts.client.SpeechSynthesizer.AUDIO_ENCODE_AMR;
import static com.baidu.tts.client.SpeechSynthesizer.MIX_MODE_DEFAULT;
import static com.qc.hsk.utils.FileUtils.copyFromAssetsToSdcard;
import static com.qc.hsk.utils.FileUtils.makeDir;

/**
 * <ul>
 * <li>功能职责：语音管理器</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-11-18
 */
public class SpeechManager implements SpeechSynthesizerListener {

    private static final String BAIDU_SPEECH_APPID = "8876475";
    private static final String BAIDU_SPEECH_APP_KEY = "UHDbLtGHjKc4aztGs7B9S8Xw";
    private static final String BAIDU_SPEECH_SECRET_KEY = "f3421791936c81737a9af280dc3431c9";

    private static final String SAMPLE_DIR_NAME = "hskTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";

    private static final String SPEECH_PARAM_VOLUME_DEfAUlT = "5";//中级音量，范围[ 0- 9]
    private static final String SPEECH_PARAM_SPEED_DEfAUlT = "0";// 中速，范围[ 0- 9]
    private static final String SPEECH_PARAM_PITCH_DEfAUlT = "5";//中调，范围[ 0- 9]
    private static final String SPEECH_PARAM_SPEAKER_DEfAUlT = "0"; //0 (普通女声),1 (普通男声),2 (特别男声),3 (情感男声)


    private Context mContext;

    // 语音合成客户端
    private SpeechSynthesizer mSpeechSynthesizer;

    private String mSampleDirPath;

    public SpeechManager(Context context) {
        mContext = context;
        initSpeechSynthesizer();
    }

    private void initSpeechSynthesizer() {
        initialEnv();
        startTTS();
    }

    /**
     * 复制文件到SD卡
     */
    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(mContext, false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(mContext, false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(mContext, false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
    }

    // 初始化语音合成客户端并启动
    private void startTTS() {
        // 获取语音合成对象实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        // 设置context
        mSpeechSynthesizer.setContext(mContext);
        // 设置语音合成状态监听器
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 语音开发者平台上注册应用得到的App ID (离线授权)
        mSpeechSynthesizer.setAppId(BAIDU_SPEECH_APPID);
        // 语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        mSpeechSynthesizer.setApiKey(BAIDU_SPEECH_APP_KEY, BAIDU_SPEECH_SECRET_KEY);
        // 文本模型文件路径 (离线引擎使用)
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        //中级音量，范围[ 0- 9]
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, SPEECH_PARAM_VOLUME_DEfAUlT);
        //中速，范围[ 0- 9]
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, SPEECH_PARAM_SPEED_DEfAUlT);
        //中调，范围[ 0- 9]
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, SPEECH_PARAM_PITCH_DEfAUlT);
        //0 (普通女声),1 (普通男声),2 (特别男声),3 (情感男声)
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, SPEECH_PARAM_SPEAKER_DEfAUlT);
        //合成模式
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, MIX_MODE_DEFAULT);
        //在线合成参数
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, AUDIO_ENCODE_AMR);
        //在线合成参数
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, AUDIO_BITRATE_AMR_15K85);
        //离线合成参数
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOCODER_OPTIM_LEVEL, "0");
        // 初始化语音合成器
        mSpeechSynthesizer.initTts(TtsMode.MIX);

    }

    @Override
    public void onSynthesizeStart(String s) {
        Log.i("nnn","onSynthesizeStart");
        // 监听到合成开始，在此添加相关操作
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
        // 监听到有合成数据到达，在此添加相关操作
    }

    @Override
    public void onSynthesizeFinish(String s) {
        // 监听到合成结束，在此添加相关操作
    }

    @Override
    public void onSpeechStart(String s) {
        // 监听到合成并播放开始，在此添加相关操作
        Log.i("nnn","onSpeechStart");
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
        // 监听到播放进度有变化，在此添加相关操作
    }

    @Override
    public void onSpeechFinish(String s) {
        // 监听到播放结束，在此添加相关操作
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        // 监听到出错，在此添加相关操作
        Log.i("nnn","speechError=="+speechError.description);
    }

    public int speak(String str) {
        if (mSpeechSynthesizer != null) return mSpeechSynthesizer.speak(str);
        return -1000;
    }

    public SpeechSynthesizer getSpeechSynthesizer() {
        return mSpeechSynthesizer;
    }

}
