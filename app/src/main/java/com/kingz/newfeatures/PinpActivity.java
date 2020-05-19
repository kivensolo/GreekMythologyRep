package com.kingz.newfeatures;

import android.app.PictureInPictureParams;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kingz.customdemo.R;

/**
 * 画中画效果页面
 */
public class PinpActivity extends AppCompatActivity {
//    public static final String mVideoUrl = "http://182.138.101.48:38888/file/31a4d30c03894545b5d786e0fe40a6da/20190614/b90746daf5c96db74dd72224d48cdd62.mp4";
    private VideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinp);
        init();
        prepareVideo();
    }

    private void init() {
        findViewById(R.id.btn_enter_pip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
                    // 设置画中画窗口宽高比例
                    Rational aspectRatio = new Rational(16, 9);
                    builder.setAspectRatio(aspectRatio);
                    // 进入画中画模式，注意enterPictureInPictureMode是Android8.0之后新增的方法
                    enterPictureInPictureMode(builder.build());
                }

            }
        });
        /*findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.pause();
            }
        });*/
    }

    private void prepareVideo() {
        String mVideoUrl = "http://220.194.238.114/11/f/l/g/i/flgizavtgyosxckoyitnjodtjtiutw/hc.yinyuetai.com/51C001632ED3B0F34F87BAF928CF4F0A.mp4";
        final Uri uri = Uri.parse(mVideoUrl);
        mVideoView = findViewById(R.id.player);

        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setVideoURI(uri);
        mVideoView.start();
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode) { // 进入画中画模式，
            Toast.makeText(PinpActivity.this, "进入画中画", Toast.LENGTH_SHORT).show();
        } else { // 退出画中画模式，
            Toast.makeText(PinpActivity.this, "退出画中画", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        mVideoView.stopPlayback();
        super.onDestroy();
    }
}
