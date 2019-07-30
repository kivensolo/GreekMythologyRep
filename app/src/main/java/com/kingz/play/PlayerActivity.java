package com.kingz.play;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.App;
import com.base.BaseActivity;
import com.kingz.customdemo.R;

import java.util.concurrent.TimeUnit;

/**
 * author：KingZ
 * date：2019/7/30
 * description：播放器的顶层抽象层
 */
public abstract class PlayerActivity extends BaseActivity {

    //播放部分的layout params
    private LinearLayout.LayoutParams portraitParams;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        keepScreenOn();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_play);
        initRotation();
    }


    /**
     * 保存屏幕常亮
     */
    private void keepScreenOn() {
        int keepScreenOn = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        getWindow().setFlags(keepScreenOn, keepScreenOn);
    }


    /**
     * 初始化横竖屏
     * 由重力决定
     */
    @SuppressLint("WrongViewCast")
    private void initRotation() {
        //保存默认竖屏参数
        portraitParams = (LinearLayout.LayoutParams) findViewById(R.id.player_content).getLayoutParams();

        final int autoRotation = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        App.getAppInstance().postDelayToMainLooper(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && autoRotation == 1) {
                    PlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
            }
        }, TimeUnit.SECONDS.toMillis(2));
    }

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
                findViewById(R.id.player_content).setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                //竖屏
                if (portraitParams != null) {
                    findViewById(R.id.player_content).setLayoutParams(portraitParams);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
