package com.kingz.install;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import com.zeke.kangaroo.zlog.ZLog;
import com.zeke.ktx.App;

import java.util.List;

/**
 * author: King.Z <br>
 * date:  2017/1/17 18:19 <br>
 * description:升级后覆盖安装广播检测
 *  <receiver android:name="com.starcor.service.KorkReplacedReceiver">
 *     <intent-filter>
 *         <action  android:name="android.intent.action.MY_PACKAGE_REPLACED"/>
 *     </intent-filter>
 *  </receiver>
 */
public class InstallReplacedReceiver extends BroadcastReceiver {

    private static final String TAG = "InstallReplacedReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        ZLog.d(TAG, "onReceive()  intent=" + intent.toString());
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            //doNothing
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            //doNothing
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_CHANGED)) {
            //doNothing
        } else if (intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {
            String packageName = intent.getPackage();
            ZLog.d(TAG, "onReceive()  ACTION_MY_PACKAGE_REPLACED  packageName = " + packageName);
            if(TextUtils.equals(packageName,"com.starcor.xinjiang")){
                startApp(packageName);
            }else{
                ZLog.e(TAG, "onReceive()  不是维语版本更新  自身不启动");
            }
        }
    }

     public static  boolean startApp(String packageName) {
        Context appContext = App.instance.getApplicationContext();
        PackageInfo packageinfo = null;
        try {
            packageinfo = appContext.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        if (packageinfo == null) {
            ZLog.e("", " app not install:" + packageName);
            return false;
        }
        ZLog.i("","startApp:"+packageName);
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        ZLog.i(TAG, packageName+" is not running");
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        List<ResolveInfo> resolveInfoList = appContext.getPackageManager().queryIntentActivities(resolveIntent, 0);
        if(resolveInfoList==null){
            return false;
        }
        ResolveInfo resolveinfo = resolveInfoList.iterator().next();
        if (resolveinfo != null) {
            String name = resolveinfo.activityInfo.packageName;
            String className = resolveinfo.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(name, className);
            intent.setComponent(cn);
            appContext.startActivity(intent);
            return true;
        }
        return false;
    }
}
