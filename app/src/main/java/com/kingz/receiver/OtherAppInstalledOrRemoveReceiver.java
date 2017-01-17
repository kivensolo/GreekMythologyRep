package com.kingz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.utils.ZLog;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2017/1/17 22:16
 * description: 其他应用安装完成的广播接受器
 */

public class OtherAppInstalledOrRemoveReceiver extends BroadcastReceiver{

    private static final String TAG = "OtherAppInstalledOrRemoveReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        ZLog.d(TAG,"onReceive() " + intent.toString());
        if(TextUtils.equals(intent.getAction(),Intent.ACTION_PACKAGE_ADDED)){
            //doInstall
        }else if(TextUtils.equals(intent.getAction(),Intent.ACTION_PACKAGE_REMOVED)){
            //doRemove
        }
//        ComponentName component = intent.getComponent();
//        String action = intent.getAction();
//        String dataStr = intent.getDataString();
//        String dataType = intent.getType();
//        String packageName = intent.getPackage();
//        String scheme = intent.getScheme();
//        Set<String> categories = intent.getCategories();
//        ZLog.d(TAG,"[Intent = ]: component=" + component + " ;action=" + action + " ;dataStr=" + dataStr + " ;packageName=" + packageName +
//                " ;scheme=" + scheme + " ;dataType=" + dataType + " ;categories=" + categories);
    }

}
