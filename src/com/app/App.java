package com.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/5/10 23:25
 * description:
 */
// 请在AndroidManifest.xml中application标签下android:name中指定该类
public class App extends Application {

    public final static String TAG = "Application";

    private static App application;
    /**
     * 全局变量
     */
    public static Context mContext;


    @Override
    public void onCreate() {
        // TODO 其他初始化流程
//        ApiStoreSDK.init(this, GlobalLogic.BDApiKey);
        super.onCreate();

        application = this;
        mContext = this;
        Log.i(TAG, "app onCreate this:" + this);
    }

    public static Context getAppContext() {
        return mContext;
    }

}
