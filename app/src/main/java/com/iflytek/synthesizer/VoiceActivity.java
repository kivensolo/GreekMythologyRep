package com.iflytek.synthesizer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.iflytek.cloud.*;
import com.kingz.customdemo.R;
import com.kingz.utils.ToastTools;

public class VoiceActivity extends Activity implements OnClickListener {

	private static final String TAG = "Iat_TtsDemo";
	private EditText mResultText;	// 查找结果内容
	private SpeechSynthesizer mTts;	// 语音合成对象

	private SharedPreferences mSharedPreferences;
	private Context _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_to_speech);
		initLayout();//界面初始化，绑定对象函数

		SpeechUtility.createUtility(this, "appid="+"54fd6a79");
		// 初始化 合成 对象
		mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
		mSharedPreferences = getSharedPreferences("com.iflytek.setting", Activity.MODE_PRIVATE);
	}

	private void initLayout() {
		findViewById(R.id.tts_play).setOnClickListener(this);;
		mResultText=(EditText) findViewById(R.id.mResultText);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.tts_play:
				setParam();	// 语音合成
				break;
		}
	}

	/*********** 初期化 TTS 监听器 ******* 语音+初始化完成   回调接口  ***********/
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		//初始化完成  回调
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败,错误码："+code);
        	}
		}
	};
	private void setParam() {
		//设置合成
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
		//设置发音人 voicer为空默认通过语音+界面指定发音人。
		mTts.setParameter(SpeechConstant.VOICE_NAME,"xiaofeng");
		mTts.setParameter(SpeechConstant.SPEED,"50");
		mTts.setParameter(SpeechConstant.VOLUME,"50");
		//设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE,mSharedPreferences.getString("stream_preference", "1"));//0则为听筒

		int code = mTts.startSpeaking(mResultText.getText().toString(), mTtsListener);
		if (code != ErrorCode.SUCCESS) {
			showTip("未安装离线包");
		}

	}

	private void showTip(String info) {
		ToastTools.getInstance().showMgtvWaringToast(this,info);
	}

	/**********合成监听器** 回调都发生在主线程（UI线程）中******** 合成回调接口 *****************/
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		//	缓冲进度回调报告
		public void onBufferProgress(int percent,int beginPos,
				int endPos,String info) {
//			mPercentForBuffering = percent;	//百分比
//			showTip(String.format(getString(R.string.tts_toast_format),
//					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if(error == null){
				showTip("播放完成");
			}else{
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
//			mPercentForPlaying = percent;
//			showTip(String.format(getString(R.string.tts_toast_format),
//					mPercentForBuffering, mPercentForPlaying));
		}
		//	合成会话事件 扩展用接口，由具体业务进行约定
		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {}
		//  开始播放
		@Override
		public void onSpeakBegin() {}
		//	暂停播放
		@Override
		public void onSpeakPaused() {}
		//	继续播放
		@Override
		public void onSpeakResumed() {}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mTts.stopSpeaking();
		mTts.destroy();
	}
}




