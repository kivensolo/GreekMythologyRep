package com.apiDemo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kingz.customdemo.R;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/5/16 22:32
 * description: 此实例展示如何通过schedule一个闹钟引发一个服务的启动。
 * 当你想长时间运行一个操作的时候这是非常有用的，例如反复接收最近的电子邮件。
 */
public class AlarmServiceActivity extends Activity {

    private AlarmManager am;
    private PendingIntent mAlarmSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        am = (AlarmManager)getSystemService(ALARM_SERVICE);
        mAlarmSender = PendingIntent.getService(this,0,
                new Intent(this,AlarmClock_Service.class),0);
        setContentView(R.layout.alarm_service);

        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.start_alarm);
        button.setOnClickListener(mStartAlarmListener);
        button = (Button)findViewById(R.id.stop_alarm);
        button.setOnClickListener(mStopAlarmListener);
    }

    private View.OnClickListener mStartAlarmListener = new View.OnClickListener() {
        public void onClick(View v) {
            // We want the alarm to go off 30 seconds from now.
            long firstTime = SystemClock.elapsedRealtime();

            // Schedule the alarm!
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            firstTime, 30*1000, mAlarmSender);

            Toast.makeText(AlarmServiceActivity.this,"Repeating alarm will go off in 15 seconds and\n" +
                            " every 15 seconds after based on the elapsed realtime clock",
                    Toast.LENGTH_LONG).show();
        }
    };

    private View.OnClickListener mStopAlarmListener = new View.OnClickListener() {
        public void onClick(View v) {
            //取消闹钟服务
            am.cancel(mAlarmSender);
            Toast.makeText(AlarmServiceActivity.this,"重复闹钟已经被unscheduled",
                    Toast.LENGTH_LONG).show();

        }
    };
}
