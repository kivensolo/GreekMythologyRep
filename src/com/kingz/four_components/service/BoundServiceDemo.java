package com.kingz.four_components.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created by KingZ.
 * Data: 2016 2016/3/2
 * Discription: 服务端代码 aidl文件 + service文件
 *    实现服务端对客户端的共享
 */
public class BoundServiceDemo extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**e
     * 当客户端绑定到这个服务的时候，回调此方法
     * @param intent
     * @return IBiner
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


    /**
     * Created by KingZ.
     * Data: 2016 2016/3/2
     * Discription: 业务实现类(必须是一个IBinder)
     *              前提需要一个AIDL
     *              Stub是一个Binder，Binder就是一个IBinder,同时还是一个Animal
     */
    class AnimalImpl extends IAnimalRemoteService.Stub {
        private String name;

        @Override
        public void setName(String name) throws RemoteException {
            this.name = name;
        }

        @Override
        public String getValue() throws RemoteException {
            return "hello AIDL，I'm :" + name;
        }

        @Override
        public void desc() throws RemoteException {

        }
    }
}
