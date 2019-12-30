package com.bling.pages;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.kingz.customdemo.R;
import com.kingz.customviews.text.ColorTrackView;
import com.module.views.loading.Win10LoaddingView;
import com.module.views.progress.CircleProgressView;
import com.module.views.progress.HorizontalProgressBarWithNumber;
import com.module.views.progress.RoundProgressBarWidthNumber;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/1/22 16:56
 * description: 各种进度条页面展示
 * 1.基础的环形进度条
 * 2.仿win10加载圈
 * 3.歌词跑马灯效果View
 * 3.水平/环形 带数字的加载圈
 *
 * FIXME 布局需要优化
 */
public class ProgressViewsActivity extends Activity {

    private CircleProgressView circleProgressView;
    private Win10LoaddingView win10LoaddingView;
    private ColorTrackView colorTrackView;
    private Button btn_start;
    private Button btn_Dialog;

    private HorizontalProgressBarWithNumber mProgressBar;
    private RoundProgressBarWidthNumber mRoundProgressBar;
    private RoundProgressBarWidthNumber mRoundProgressBar2;
    private static final int MSG_BUTTON_DISABLE = 0x100;
    private static final int MSG_BUTTON_ENABLED = 0x101;
    private static final int MSG_PROGRESS_UPDATE = 0x102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circleview_layout);
        initCircleProgress();
        initWin10Loadding();
        initColorTrack();
        initProgressBarWithNumber();
    }

    private void initProgressBarWithNumber() {
        mProgressBar = (HorizontalProgressBarWithNumber) findViewById(R.id.id_progressbar01);
        mRoundProgressBar = (RoundProgressBarWidthNumber) findViewById(R.id.id_progress02);
        mRoundProgressBar2 = (RoundProgressBarWidthNumber) findViewById(R.id.id_progress03);
        mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);
    }

    private void initWin10Loadding() {
        btn_Dialog = (Button) findViewById(R.id.win10loadding_btn);
        win10LoaddingView = (Win10LoaddingView) findViewById(R.id.win10loadding);
        btn_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void initColorTrack() {
        colorTrackView = (ColorTrackView) findViewById(R.id.id_changeTextColorView);
    }

    private void initCircleProgress() {
        btn_start = (Button) findViewById(R.id.start_circle_progress);
        circleProgressView = (CircleProgressView) findViewById(R.id.view_circle_progress);
        circleProgressView.setCircleDrawFinishedListner(new CircleProgressView.ICircleDrawFinished() {
            @Override
            public void onFinished() {
                mHandler.sendEmptyMessage(MSG_BUTTON_ENABLED);
            }
        });
        circleProgressView.setCircleDrawStartListner(new CircleDrawStartListener());
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleProgressView.beginDrawCircle();
            }
        });
        btn_start.requestFocus();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_BUTTON_DISABLE:
                    btn_start.setEnabled(false);
                    break;
                case MSG_BUTTON_ENABLED:
                    btn_start.setEnabled(true);
                    break;
                case MSG_PROGRESS_UPDATE:
                    int progress = mProgressBar.getProgress();
                    int roundProgress = mRoundProgressBar.getProgress();
                    mProgressBar.setProgress(++progress);
                    mRoundProgressBar.setProgress(++roundProgress);
                    mRoundProgressBar2.setProgress(++roundProgress);
                    if (progress >= 100) {
                        mHandler.removeMessages(MSG_PROGRESS_UPDATE);
                    }
                    mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);
                    break;
            }
        }
    };

    public class CircleDrawStartListener implements CircleProgressView.ICircleDrawStart {
        @Override
        public void onStart() {
            mHandler.sendEmptyMessage(MSG_BUTTON_DISABLE);
        }
    }

    @SuppressLint("NewApi")
    public void startLeftChange(View view) {
        colorTrackView.setDirection(ColorTrackView.LEFT2RIGHT);
        ObjectAnimator.ofFloat(colorTrackView, "progress", 0, 1)
                .setDuration(2000)
                .start();
    }

    @SuppressLint("NewApi")
    public void startRightChange(View view) {
        colorTrackView.setDirection(ColorTrackView.RIGHT2LEFT);
        ObjectAnimator.ofFloat(colorTrackView, "progress", 0, 1)
                .setDuration(2000)
                .start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
