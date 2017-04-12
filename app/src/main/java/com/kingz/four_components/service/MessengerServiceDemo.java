package com.kingz.four_components.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/10/17 15:19 <br>
 * description: 使用Messenger <br>
 *     //服务端
 *     服务端主要是返给客户端一个IBinder实例，以供服务端构造Messenger，
 *     并且处理客户端发送过来的Message。当然，不要忘了要在Manifests文件里面注册：
 *     <service
            android:name=".ActivityMessenger"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="com.lypeer.messenger"></action>
                <category android:name="android.intent.category.DEFAULT"/>
            </intent-filter>
        </service>
 *
 */
public class MessengerServiceDemo extends Service {

    static final int MSG_SAY_HELLO = 1;

    class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    //当收到客户端的message时，显示hello
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    final Messenger mMessenger = new Messenger(new ServiceHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        //返回给客户端一个IBinder实例
        return mMessenger.getBinder();
    }
}
