package com;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.kingz.customdemo.BuildConfig;
import com.kingz.customdemo.R;
import com.zeke.ktx.MainActivity;
import com.zeke.ktx.base.BaseActivity;
import com.zeke.ktx.view.LogininView;

/**
 * author: King.Z <br>
 * date:  2016/9/29 19:03 <br>
 * description: 应用启动欢迎页面 <br>
 */
public class SplashActivity extends BaseActivity
        implements View.OnClickListener {

    private VideoView mVideoView;
    private InputType inputType = InputType.NONE;
    private Button buttonLeft, buttonRight;
    private LogininView formView;
    private TextView logoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusTranslucent();
        setContentView(R.layout.splash_activity);
        findView();
        initView();

        if(!BuildConfig.SPLSH_DEBUG){
            openMainPage();
        }else{
            playVideo();
            playAnim();
        }
    }

    private void findView() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        formView = (LogininView) findViewById(R.id.loginView);
        logoView = (TextView) findViewById(R.id.appName);
        formView.post(new Runnable() {
            @Override
            public void run() {
                //先移除到屏幕顶端
                int delta = formView.getTop()+formView.getHeight();
                formView.setTranslationY(-1 * delta);
            }
        });
    }

    private void initView() {
        buttonRight.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
    }

    private void playVideo() {
        String _videoUrl = "android.resource://" + getPackageName() + "/" + R.raw.welcome_video;
        mVideoView.setVideoPath(_videoUrl);
        mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });
    }

    private void playAnim() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(logoView, "alpha", 0,1);
        anim.setDuration(4000);
        anim.setRepeatCount(1);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(logoView.getVisibility() != View.INVISIBLE ){
                    logoView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void openMainPage() {
        Intent intent = new Intent(SplashActivity.this,
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }

    @Override
    public void onClick(View view) {
        int delta = formView.getTop()+formView.getHeight();
        switch (inputType) {
            case NONE:
                //回到原来的位置 translationY  相对位置
                formView.animate().translationY(0).alpha(1).setDuration(500).start();
                if (view == buttonLeft) {
                    inputType = InputType.LOGIN;
                    buttonLeft.setText(R.string.button_confirm_login);
                    buttonRight.setText(R.string.button_cancel_login);
                } else if (view == buttonRight) {
                    inputType = InputType.SIGN_UP;
                    buttonLeft.setText(R.string.button_confirm_signup);
                    buttonRight.setText(R.string.button_cancel_signup);
                }
                break;
            case LOGIN:
                formView.animate().translationY(-1 * delta).alpha(0).setDuration(500).start();
                if (view == buttonLeft) {
                    logoView.setVisibility(View.INVISIBLE);
                    openMainPage();
                    return;
                }
                inputType = InputType.NONE;
                buttonLeft.setText(R.string.button_login);
                buttonRight.setText(R.string.button_signup);
                break;
            case SIGN_UP:
                formView.animate().translationY(-1 * delta).alpha(0).setDuration(500).start();
                inputType = InputType.NONE;
                buttonLeft.setText(R.string.button_login);
                buttonRight.setText(R.string.button_signup);
                break;
        }
    }

    enum InputType {
        NONE, LOGIN, SIGN_UP
    }
}