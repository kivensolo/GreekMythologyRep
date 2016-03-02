package com.kingz.four_components;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by KingZ.
 * Data: 2016 2016/2/13
 * Discription:
 *  IntentService是继承于Service并处理异步请求的一个类，
 *  1.内部有一个工作线线处理耗时的操作，启动方式和启动服务方式一样，
 *  2.任务执行完成后，intentSerivce会-----自动停止-----。
 *  3.可以启动多次，以工作队列的形式在onHandleIntent
 *    回调方法中执行，且每次只执行一个。
 */
public class IntentServicDemo extends IntentService{

    public static final String TAG = "IntentServicDemo";

    public IntentServicDemo() {
        super(TAG); //设置一个线程的名字
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){
            Log.d(TAG, "info = " +  intent.getStringExtra("info"));
        }
        for (int i = 0; i < 30; i++) {
            System.out.println("onHandleIntent:"+i+"-"+Thread.currentThread().getName());
            try {
                Thread.sleep(300);
            } catch (InterruptedException e)  {
                e.printStackTrace();
            }
        }
    }
}
