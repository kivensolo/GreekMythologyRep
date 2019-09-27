package com;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import com.core.logic.GlobalCacheCenter;
import com.kingz.customdemo.BuildConfig;
import com.kingz.utils.ZLog;
import com.takt.FpsTools;

/**
 * author: King.Z
 * date:  2016/5/10 23:25
 */
public class App extends Application {

    public final static String TAG = "Application";
    private static App application;
    private Handler _appMainHandler;
    private static final boolean STRICT_MODE = false;
    public static final String BDApiKey = "467e6d8f8b06b8811b7a6fb939c8ad5e";

    /** ------------------------- 屏幕适配参数  ----- Start -------- */
    public static int SCREEN_WIDTH = 1280;
	public static int SCREEN_HEIGHT = 720;
	public static final int DESIGN_WIDTH = 1280;
	public static final int DESIGN_HEIGHT = 720;
	public static float mainScale = 1.0f;
    /** ------------------------- 屏幕适配参数  ----- End -------- */

    public static App getAppInstance() {
		return application;
	}

    @Override
    public void onCreate() {
        super.onCreate();
        _appMainHandler = new Handler(getMainLooper());
        application = this;
        ZLog.d("App", "app name is:" + BuildConfig.APP_NAME);
        initLog();
        initAPPScreenParms();
        initCacheCenter();
        //initFpsDebugView();
        initStrictListenner();
        //Bmob.initialize(this, "fea19b87f0795833b30de91f46f1465c");
    }

    private void initFpsDebugView() {
        //Takt.stock(this).size(20f).play();
        FpsTools.init(this)
                .size(30f)
                .color(Color.WHITE)
                .play();
    }

    private void initStrictListenner() {
        if(STRICT_MODE){
            //https://blog.csdn.net/meegomeego/article/details/45746721
            //https://www.jianshu.com/p/113b9c54b5d1
            //设置StrictMode监听那些潜在问题，出现问题时可以对屏幕闪红色，也可以输出错误日志
            //设置线程方面的策略
            // 主要用于发现在UI线程中是否有读写磁盘的操作，是否有网络操作，
            // 以及检查UI线程中调用的自定义代码是否执行得比较慢。
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()          //磁盘读写检查
                    .detectDiskWrites()
                    .detectCustomSlowCalls()    //帮助开发者发现UI线程调用的那些方法执行得比较慢
                    .detectNetwork()            // or .detectAll() for all detectable problems
                    .penaltyLog()               //在Logcat 中打印违规异常信息
                    .build());
            //设置VM方面的策略
            //主要用于发现内存问题，比如 Activity内存泄露， SQL 对象内存泄露，
            //资源未释放，能够限定某个类的最大对象数。
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()  //当触发违规条件时，直接Crash掉当前应用程序
                    .build());
        }
    }

    @Override
    public void onTerminate() {
        //Takt.finish();
        //FpsTools.finish();
        super.onTerminate();
    }

    private void initCacheCenter() {
        GlobalCacheCenter.getInstance().init(getAppContext());
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

    public Context getAppContext() {
        return getApplicationContext();
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
