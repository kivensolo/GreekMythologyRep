package com;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import com.core.logic.GlobalCacheCenter;
import com.kingz.customdemo.BuildConfig;
import com.kingz.utils.ZLog;
import jp.wasabeef.takt.Takt;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/5/10 23:25
 */
//在AndroidManifest.xml中application标签下android:name中指定该类
@SuppressWarnings("JavaDoc")
public class App extends Application {

    public final static String TAG = "Application";

    private static App application;

    private static final boolean STRICT_MODE = true;
    /**
     * 全局变量
     */
    public static Context mContext;

    private static Handler _appMainHandler;

    /**
     * 屏幕适配参数
     */
    public static int SCREEN_WIDTH = 1280;
	public static int SCREEN_HEIGHT = 720;
	public static final int DESIGN_WIDTH = 1280;
	public static final int DESIGN_HEIGHT = 720;
	public static float mainScale = 1.0f;

    public static App getAppInstance() {
		return application;
	}

    @Override
    public void onCreate() {
        super.onCreate();
        _appMainHandler = new Handler(getMainLooper());
        application = this;
        mContext = getApplicationContext();
        initLog();
        initAPPScreenParms();
        initCacheCenter();
        Takt.stock(this).size(20f).play();
        initStrictListenner();
    }

    private void initStrictListenner() {
        if(STRICT_MODE){
            //设置StrictMode监听那些潜在问题，出现问题时可以对屏幕闪红色，也可以输出错误日志
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    @Override
    public void onTerminate() {
        Takt.finish();
        super.onTerminate();
    }
    private void initCacheCenter() {
        GlobalCacheCenter.getInstance().init(mContext);  //缓存初始化
    }

    private void initLog() {
        if(BuildConfig.DEBUG){
            ZLog.isDebug = true;
        }
    }


    private void initAPPScreenParms(){
        Log.i(TAG, "SCREEN_WIDTH = " + getResources().getDisplayMetrics().widthPixels
			+ "   SCREEN_HEIGHT = " + getResources().getDisplayMetrics().heightPixels);
        SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = getResources().getDisplayMetrics().heightPixels;
        mainScale = Math.min(SCREEN_HEIGHT / DESIGN_HEIGHT, SCREEN_WIDTH / DESIGN_WIDTH);
    }

    public static Context getAppContext() {
        return mContext;
    }

    /**
     * 屏幕适配
     * @param Original
     * @return
     */
    public static int ScreenAdjuest(float Original){
        return (int) (Original * mainScale + 0.5f);
        //return (int) (SCREEN_HEIGHT * (Original * 1.0f / DESIGN_HEIGHT) + 0.5f);
    }

    public void postToMainLooper(Runnable runnable) {
		_appMainHandler.post(runnable);
	}

	public void postDelayToMainLooper(Runnable runnable, long ms) {
		_appMainHandler.postDelayed(runnable, ms);
	}
}
