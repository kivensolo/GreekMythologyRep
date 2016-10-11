package com.utils;
import android.util.Log;
//Logcat统一管理类
public class ZLog {
    private ZLog() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    private static final String TAG = "Zlog";
    public static boolean isDebug = true;       // 是否需要打印bug，可以在application的onCreate函数里面初始化

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    public static void w(String msg) {
        if (isDebug)
            Log.w(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }
    public static void w(String tag, String msg) {
        if (isDebug)
            Log.w(tag, msg);
    }
    public static void i(String tag, String msg,Throwable tr) {
        if (isDebug)
            Log.i(tag, msg,tr);
    }

    public static void d(String tag, String msg,Throwable tr) {
        if (isDebug)
            Log.d(tag, msg,tr);
    }

    public static void e(String tag, String msg,Throwable tr) {
        if (isDebug)
            Log.e(tag, msg,tr);
    }

    public static void v(String tag, String msg,Throwable tr) {
        if (isDebug)
            Log.v(tag, msg,tr);
    }
    public static void w(String tag, String msg,Throwable tr) {
        if (isDebug)
            Log.w(tag, msg,tr);
    }
}