/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apiDemo;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.Toast;
import com.kingz.customdemo.R;

/**
 * This is an example of implementing an application service that will run in
 * response to an alarm, allowing us to move long duration work out of an
 * intent receiver.
 *
 * @see AlarmServiceActivity
 * @see AlarmClock_Service
 */
@SuppressLint("Registered")
public class AlarmClock_Service extends Service {
    NotificationManager mNM;

    Runnable mTask = new Runnable() {
        public void run() {
            // Normally we would do some work here...  for our sample, we will
            // just sleep for 30 seconds.
            long endTime = System.currentTimeMillis() + 15 * 1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (mBinder) {
                    try {
                        mBinder.wait(endTime - System.currentTimeMillis());
                    } catch (Exception ignored) {
                    }
                }
            }
            // Done with our work...  stop the service!
            AlarmClock_Service.this.stopSelf();
        }
    };

    @Override
    public void onCreate() {
        //获取消息通知管理者实例
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //状态栏中显示状态
        showNotification();
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        //开启子线程运行服务，不阻塞主线程
        Thread thr = new Thread(null, mTask, "ThreadName----AlarmClock");
        thr.start();
    }

    @Override
    public void onDestroy() {
        // Cancel the notification -- we use the same ID that we had used to start it
        mNM.cancel(R.string.alarm_service_started);
        // Tell the user we stopped.
        Toast.makeText(this, R.string.alarm_service_finished, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        CharSequence text = getText(R.string.alarm_service_started);
        //设置图片，滚动文字，时间戳
        Notification notification = new Notification(R.drawable.dialog_warning, text,System.currentTimeMillis());
        //The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AlarmServiceActivity.class), 0);
        // Set the info for the views that show in the notification panel.
        //HttpClient 已废弃
        //notification.setLatestEventInfo(this, "Sample Alarm Service",text, contentIntent);
        // 发送通知
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.alarm_service_started, notification);
    }

    /**
     * This is the object that receives interactions from clients.  See RemoteService
     * for a more complete example.
     */
    private final IBinder mBinder = new Binder() {
        @Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
                                     int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };
}

