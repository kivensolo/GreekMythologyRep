package com.kingz.four_components;

import android.os.RemoteException;

/**
 * Created by KingZ.
 * Data: 2016 2016/3/2
 * Discription: 业务实现类(必须是一个IBinder)
 * 前提需要一个AIDL
 * Stub是一个Binder，Binder就是一个IBinder,同时还是一个Animal
 */
public class AnimalImpl extends IAnimal.Stub{
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
