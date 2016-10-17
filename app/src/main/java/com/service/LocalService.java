package com.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

/**
 * author: King.Z <br>
 * date:  2016/10/17 15:10 <br>
 * description: GooGle Code Demo <br>
 *     使用Binder类
 *     LocalBinder 为客户端提供 getService() 方法，以检索 LocalService 的当前实例。
 *     这样，客户端便可调用服务中的公共方法。
 *     例如，客户端可调用服务中的 getRandomNumber():
 */

public class LocalService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public int getRandomNumber() {
      return mGenerator.nextInt(100);
    }
}