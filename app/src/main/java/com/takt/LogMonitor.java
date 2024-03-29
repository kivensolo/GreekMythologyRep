package com.takt;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.MessageQueue;

import com.zeke.kangaroo.zlog.ZLog;

/**
 * author: King.Z <br>
 * date:  2018/2/4 13:01 <br>
 * description: LogMonitor <br>
 *     记录卡顿时候主线程的堆栈信息
 */
public class LogMonitor {
    private static LogMonitor sInstance = new LogMonitor();
    private HandlerThread mLogThread = new HandlerThread("log");
    private Handler mIoHandler;
    private static final long TIME_BLOCK = 200L;

    public static LogMonitor getInstance() {
        if(sInstance == null){
            sInstance = new LogMonitor();
        }
        return sInstance;
    }

    private LogMonitor() {
        mLogThread.start();
        mIoHandler = new Handler(mLogThread.getLooper());
    }

    private static Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString()).append("\n");
            }
            ZLog.e("Droped_Frame", sb.toString());
        }
    };

    public boolean isMonitor() {
        MessageQueue myQueue = Looper.myQueue();
        return false;
    }

    public void startMonitor() {
        //startMonitorDelay(mLogRunnable, TIME_BLOCK);
        startMonitorDelay(mLogRunnable, 0);
    }

    public void startMonitorDelay(Runnable r, long delayMillis) {
        mIoHandler.postDelayed(r, delayMillis);
    }

    public void removeMonitor() {
        mIoHandler.removeCallbacks(mLogRunnable);
    }
}
