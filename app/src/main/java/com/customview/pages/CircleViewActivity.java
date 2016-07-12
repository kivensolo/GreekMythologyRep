package com.customview.pages;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.customview.views.CircleProgressView;
import com.customview.views.ColorTrackView;
import com.kingz.uiusingListViews.R;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/1/22 16:56
 * description:
 */
public class CircleViewActivity extends Activity {

    private CircleProgressView circleProgressView;
    private ColorTrackView colorTrackView;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circleview_layout);
        initCircleProgress();
        initColorTrack();
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
                handler.sendEmptyMessage(2);
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
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    btn_start.setEnabled(false);
                    break;
                case 2:
                    btn_start.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class CircleDrawStartListener implements CircleProgressView.ICircleDrawStart {
        @Override
        public void onStart() {
            handler.sendEmptyMessage(1);
        }
    }

    @SuppressLint("NewApi")
    public void startLeftChange(View view) {
        colorTrackView.setDirection(0);
        ObjectAnimator.ofFloat(colorTrackView, "progress", 0, 1).setDuration(2000)
                .start();
    }

    @SuppressLint("NewApi")
    public void startRightChange(View view) {
        colorTrackView.setDirection(1);
        ObjectAnimator.ofFloat(colorTrackView, "progress", 0, 1).setDuration(2000)
                .start();
    }
}
