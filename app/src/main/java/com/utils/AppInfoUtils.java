package com.utils;

import android.content.Intent;
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

    /**
     * 获取第三方带Lanucher属性APP
     */
    public static List getThirdApp(){
        PackageManager pkgMgr = App.getAppContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = pkgMgr.queryIntentActivities(intent, 0); //本机所有具有Launcher属性的APK信息
        return resolveInfoList;
    }

}
