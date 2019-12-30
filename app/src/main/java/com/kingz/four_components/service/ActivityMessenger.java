package com.kingz.four_components.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;

import com.kingz.customdemo.R;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/10/17 15:23 <br>
 * description: 使用Messenger <br>
 *     //客户端
 *     服务端没有发信息给客户端的功能——这显然是不合理的。而要实现这个其实也很简单，
 *     只要客户端里也创建一个Handler实例，让它接收来自服务端的信息，
 *     同时让服务端在客户端给它发的请求完成了之后再给客户端发送一条信息即可。
 *
 *     用Messenger来进行IPC的话整体的流程是非常清晰的，Message在其中起到了一个信使的作用，
 *     通过它客户端与服务端的信息得以互通。
 */
@SuppressLint("Registered")
public class ActivityMessenger extends Activity {

    static final int MSG_SAY_HELLO = 1;

    Messenger mService = null;
    boolean mBound;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            //接收onBind()传回来的IBinder，并用它构造Messenger
            mService = new Messenger(service);
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            mBound = false;
        }
    };

    //调用此方法时会发送信息给服务端
    public void sayHello(View v) {
        if (!mBound) return;
        //发送一条信息给服务端
        Message msg = Message.obtain(null, MSG_SAY_HELLO, 0, 0);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //绑定服务端的服务，此处的action是service在Manifests文件里面声明的
        Intent intent = new Intent();
        intent.setAction("com.lypeer.messenger");
        //不要忘记了包名，不写会报错
        intent.setPackage("com.lypeer.ipcserver");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
}
