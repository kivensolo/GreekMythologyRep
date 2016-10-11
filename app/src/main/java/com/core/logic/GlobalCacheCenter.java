package com.core.logic;

import android.content.Context;
import com.utils.FileUtils;
import com.utils.ZLog;

import java.io.File;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/9/7 17:10 <br>
 * description: 全局缓存控制中心 <br>
 */
public class GlobalCacheCenter {
    private static final String TAG = "GlobalCacheCenter";
    private static final int LIMIT_TIME = 3 * 24 * 3600 * 1000; //过期时间：三天
    private static volatile GlobalCacheCenter cacheCenter = null;

	private Context appContext = null;
    private String logPath;
	private String tempPath;
	private String picCachePath;
	private String configPath;

    public GlobalCacheCenter() {

    }
    public static GlobalCacheCenter getInstance(){
        if(null == cacheCenter){
            synchronized (GlobalCacheCenter.class){
                if(null == cacheCenter){
                    cacheCenter = new GlobalCacheCenter();
                }
            }
        }
        return cacheCenter;
    }

    /**
     * 初始化
     * @param context
     * @return
     */
    public void init(Context context){
        ZLog.i(TAG,"inti appContext:"+context);
        appContext = context;

        logPath = this.appContext.getDir("log",Context.MODE_PRIVATE).toString() + File.separator;
        ZLog.i(TAG,"logCache Path is:"+logPath);
        tempPath = this.appContext.getDir("temp",Context.MODE_PRIVATE).toString() + File.separator;
		ZLog.i(TAG, "tempPath:" + tempPath);
        picCachePath = this.appContext.getDir("pic",Context.MODE_PRIVATE).toString() + File.separator;
		ZLog.i(TAG, "picCachePath:" + picCachePath);
        configPath = this.appContext.getDir("config",Context.MODE_PRIVATE).toString() + File.separator;
		ZLog.i(TAG, "configPath:" + configPath);
        clearPicOldCache(LIMIT_TIME);
        return;
    }

    /** 清除旧的图片缓存 */
    public void clearPicOldCache(long limitTime) {
        FileUtils.dealPathFilesWithOldDate(picCachePath,System.currentTimeMillis() - limitTime);
    }
}
