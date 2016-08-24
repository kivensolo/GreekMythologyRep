package com.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.App;

import java.util.List;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/6/17 10:54 <br>
 * description: APP相关信息工具类 <br>
 */
public class AppInfoUtils {
    private static final String TAG = AppInfoUtils.class.getSimpleName();

    private AppInfoUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 【获取第三方带Lanucher属性APP】
     */
    public static List getThirdApp() {
        PackageManager pkgMgr = App.getAppContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = pkgMgr.queryIntentActivities(intent, 0); //本机所有具有Launcher属性的APK信息
        return resolveInfoList;
    }
     /**
     * [根据包名判断一个应用是否已经被安装]
     * @param packageName 应用包名
     * @return
     */
    public static boolean isAppInstalled(String packageName){
        PackageManager pm = App.getAppContext().getPackageManager();
        boolean installed;
        try{
            pm.getPackageInfo(packageName,PackageManager.GET_ACTIVITIES);
            installed =true;
        }catch(PackageManager.NameNotFoundException e){
            installed =false;
        }
        return installed;
    }


    /**
     * 【获取应用程序名称】
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
