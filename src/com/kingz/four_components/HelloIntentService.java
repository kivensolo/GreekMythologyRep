package com.kingz.four_components;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by KingZ.
 * Data: 2016 2016/2/13
 * Discription:
 *  IntentService是继承于Service并处理异步请求的一个类，
 *  当中有一个工作线线=程处理耗时的操作，启动方式和启动服务方式一样，
 *  只是在任务执行完成后，intentSerivce会-----自动停止-----。
 *  并且IS可以启动多次，每一个耗时的操作会以工作队列的形式在onHandleIntent
 *  回调方法中执行，且每次只执行一个。
 */
public class HelloIntentService extends IntentService{

    public static final String TAG = "HelloIntentService";

    public HelloIntentService(String name) {
        super(TAG);
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

        }

    }
}
