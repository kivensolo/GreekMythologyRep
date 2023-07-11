package com.kingz;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.kingz.playerdemo.MainActivity;
import com.kingz.playerdemo.R;

/**
 * Copyright (c) 2022, 北京视达科科技有限责任公司 All rights reserved.
 * author：ZekeWang
 * date：2022/10/14
 * description：
 */
public class ForegoundServiceTest extends Service {
    private static final String TAG = ForegoundServiceTest.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: 将ForegoundServiceTest转为前台服务");
        startForeground(1, createNotification());
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     *
     * @return nofication通知实例
     * 可以通过NotificationManager的notify()方法将通知显示出来
     */
    private Notification createNotification(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        String notifyTitle = "话费到账通知";
        String notifyContent = "您的账户尾号0671,入账100元话费!";

        //点击通知时可进入的Activity
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pit = PendingIntent.getActivity(this, 0, notifyIntent, 0);


        // Android8.0以上的系统，需要配置消息渠道
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            String channelId = "best_notify_001"; //前台通知的id
            String channelName = "日常通知消息";    //前台通知的名称
            //通知的重要程度，此处为高，根据业务情况而定 IMPORTANCE_NOENE 就需要在系统的设置里面开启渠道，通知才能正常弹出
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId,channelName,importance);
            channel.setDescription("PlayerDemo Desc");
            //对于LED通知灯的设备进行设置
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            //通知出现时的震动设置
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});
            channel.enableVibration(true);
//            channel.setSound();

            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE); //设置锁频可见性
            notificationManager.createNotificationChannel(channel);
            notification = new NotificationCompat.Builder(this,channelId)
                    .setContentTitle(notifyTitle)
                    .setContentText(notifyContent)
                    .setSmallIcon(R.drawable.ic_launcher_background)//通知显示的图标
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.bg_btn_focused))
                    .setContentIntent(pit)
                    .setAutoCancel(true) //点击跳转后关闭通知
                    .setTicker("您有新的消息~") //收到信息后状态栏显示的文字信息
                    .build();
        }else{
            notification = new Notification.Builder(this)
                    .setContentTitle(notifyTitle)
                    .setContentText(notifyContent)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                    .setContentIntent(pit)
                    .setAutoCancel(true) //点击跳转后关闭通知
                    .setWhen(System.currentTimeMillis())
//                    .setSound()
                    //注释部分是可扩展的参数，根据自己的功能需求添加
                    //.setOngoing(true)
                    //.setPriority(NotificationCompat.PRIORITY_MAX)
                    //.setCategory(Notification.CATEGORY_TRANSPORT)
                    .build();
        }
        return notification;

        //
//        notificationManager.notify(NOTIFYID,notification);
    }
}
