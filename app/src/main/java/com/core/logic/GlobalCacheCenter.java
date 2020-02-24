package com.core.logic;

import android.content.Context;

import com.zeke.kangaroo.utils.FileUtils;
import com.zeke.kangaroo.utils.ZLog;

import java.io.File;

/**
 * author: King.Z <br>
 * date:  2016/9/7 17:10 <br>
 * description: 全局缓存控制中心 <br>
 *     @deprecated
 */
public class GlobalCacheCenter {
    private static final String TAG = "GlobalCacheCenter";
    private static final long LIMIT_TIME = 3 * 24 * 3600 * 1000; //过期时间：三天
    private static volatile GlobalCacheCenter cacheCenter = null;

    private Context appContext = null;
    private String logPath;
    private String tempPath;
    private String picCachePath;
    private String configPath;

    private GlobalCacheCenter() {

    }

    public static GlobalCacheCenter getInstance() {
        if (null == cacheCenter) {
            synchronized (GlobalCacheCenter.class) {
                if (null == cacheCenter) {
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
    public void init(Context context) {
        this.appContext = context;
        logPath = this.appContext.getDir("log", Context.MODE_PRIVATE).toString() + File.separator;
        ZLog.i(TAG, "init() logCache Path is:" + logPath);
        tempPath = this.appContext.getDir("temp", Context.MODE_PRIVATE).toString() + File.separator;
        ZLog.i(TAG, "init() tempPath:" + tempPath);
        picCachePath = this.appContext.getDir("pic", Context.MODE_PRIVATE).toString() + File.separator;
        ZLog.i(TAG, "init() picCachePath:" + picCachePath);
        configPath = this.appContext.getDir("config", Context.MODE_PRIVATE).toString() + File.separator;
        ZLog.i(TAG, "init() configPath:" + configPath);
        clearPicOldCache(LIMIT_TIME);
    }

    /**
     * 清除旧的图片缓存
     */
    public void clearPicOldCache(long limitTime) {
        FileUtils.dealPathFilesWithOldDate(picCachePath, System.currentTimeMillis() - limitTime);
    }

    public String getAppLogPathPath() {
        return logPath;
    }

    public String getAppTempPathPath() {
        return tempPath;
    }

    public String getAppPicCachePath() {
        ///data/data/packageName/app_pic/
        return picCachePath;
    }

    public String getAppConfigPath() {
        return configPath;
    }
}
