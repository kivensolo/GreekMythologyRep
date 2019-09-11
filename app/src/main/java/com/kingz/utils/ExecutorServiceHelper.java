package com.kingz.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * description：单例线程池,用于处理一些异步任务
 */

public class ExecutorServiceHelper {
    private static ExecutorServiceHelper sExecutorServiceHelper;
    private ExecutorService mSingleThreadExecutor;

    public static ExecutorServiceHelper getInstance() {
        if (sExecutorServiceHelper == null) {
            synchronized (ExecutorServiceHelper.class) {
                if (sExecutorServiceHelper == null){
                    sExecutorServiceHelper = new ExecutorServiceHelper();
                }
            }
        }
        return sExecutorServiceHelper;
    }

    public synchronized void execute(Runnable runnable) {
        if (mSingleThreadExecutor == null) {
            mSingleThreadExecutor = Executors.newCachedThreadPool();
        }
        mSingleThreadExecutor.execute(runnable);
    }

    public synchronized void execute(Callable callable) {
        if (mSingleThreadExecutor == null) {
            mSingleThreadExecutor = Executors.newCachedThreadPool();
        }
        Future mSubmit = mSingleThreadExecutor.submit(callable);
        showResult(mSubmit);
    }

    private void showResult(Future<?> mSubmit) {
//        try {
//            ZLog.d("ExecutorServiceHelper", mSubmit.get());
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
    }
}
