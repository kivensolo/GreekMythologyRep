package com.kingz.four_components;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by KingZ.
 * Data: 2016 2016/3/2
 * Discription:
 */
public class BoundServiceDemo extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 返回了一个IBiner
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return new AnimalImpl();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
