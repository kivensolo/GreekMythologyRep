package com.zeke.play;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.kingz.module.common.base.BaseActivity;
import com.zeke.module_player.R;

/**
 * author：KingZ
 * date：2019/7/30
 * description：手机端播放页面的抽象层
 *  - 横竖屏旋转的逻辑
 */
public abstract class PlayerActivity extends BaseActivity {
    protected static final String TAG_LIVE_PLAY = "live_play";
    protected static final String TAG_VOD_PLAY = "vod_play";
    protected static final String TAG_VOD_INFO = "vod_info";
    protected static final String TAG_VOD_DETAIL = "vod_detail";
    protected static final int INVALID_LAYOUT_ID = -1;

    //播放部分的layout params
    protected LinearLayout.LayoutParams portraitParams;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keepScreenOn();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if(getLayoutId() != INVALID_LAYOUT_ID){
            setContentView(getLayoutId());
        }else{
            setContentView(getLayoutView());
        }
        initRotation();
    }

    public abstract int getLayoutId();

    protected View getLayoutView(){
        return null;
    }

    /**
     * 保存屏幕常亮
     */
    private void keepScreenOn() {
        int keepScreenOn = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        getWindow().setFlags(keepScreenOn, keepScreenOn);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public abstract void initRotation();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                //横屏
                findViewById(R.id.root_layout).setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                //竖屏
                if (portraitParams != null) {
                    findViewById(R.id.root_layout).setLayoutParams(portraitParams);
                }
                break;
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
