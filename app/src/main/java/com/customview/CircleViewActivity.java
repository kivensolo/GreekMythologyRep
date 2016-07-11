package com.customview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
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
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circleview_layout);

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

    public class CircleDrawStartListener implements CircleProgressView.ICircleDrawStart{
        @Override
        public void onStart() {
            handler.sendEmptyMessage(1);
        }
    }
}
