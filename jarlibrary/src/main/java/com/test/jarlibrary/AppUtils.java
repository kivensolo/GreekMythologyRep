package com.test.jarlibrary;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.util.List;
import java.util.Set;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * author: King.Z <br>
 * date:  2016/6/17 10:54 <br>
 * description: APP相关信息工具类 <br>
 *     @see #isHomeApp(Context)
 *     @see #isTopActivity(Context)
 *     @see #isAppInstalled(Context,String)
 *     @see #getThirdAppsWithLauncher(Context)
 *     @see #getAppName(Context,String)
 *     @see #getAppIcon(Context,String)
 *     @see #getVersionName(Context,String)
 *     @see #getVersionCode(Context,String)
 *     @see #getIntentContentToStr(Intent)
 *     @see #getAppSignature(Context,String)
 *
 *     @see #getPackageInfoByName(Context, String, int)
 */
public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得全部应用
     */
    public static List getThirdAppsWithLauncher(Context context) {
        PackageManager pkgMgr = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = pkgMgr.queryIntentActivities(intent, 0); //本机所有具有Launcher属性的APK信息
        return resolveInfoList;
    }

    /**
     * 是否是顶层应用
     */
    public static boolean isHomeApp(Context pcontext) {
        PackageManager packageManager = pcontext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);

        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            if (pcontext.getPackageName().equals(ri.activityInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * [根据包名判断一个应用是否已经被安装]
     *
     * @param packageName 应用包名
     * @return
     */
    public static boolean isAppInstalled(Context context,String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * [获取应用程序名称]
     */
    public static String getAppName(Context context,String packname){
        PackageInfo packageInfo = getPackageInfoByName(context,packname,0);
        if(packageInfo != null){
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        }else{
            return "";
        }
    }

    /**
     * 获取指定应用的Icon
     * @param context  context
     * @param packname 应用包名
     * @return  Drawable
     */
    public static Drawable getAppIcon(Context context,String packname) {
        String name = packname;
        try {
            PackageManager pm = context.getPackageManager();
            if(TextUtils.isEmpty(packname)){
                name = context.getPackageName();
            }
            ApplicationInfo info  = pm.getApplicationInfo(name, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context, String packageName) {
        try {
            String name = packageName;
            if (TextUtils.isEmpty(name)) {
                name = context.getPackageName();
            }
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(name, 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本号]
     * @param context
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context, String packageName) {
        int verCode = -1;
        PackageInfo packageInfo = getPackageInfoByName(context,packageName,0);
        if(packageInfo != null){
            verCode = packageInfo.versionCode;
        }
        return verCode;
    }

    /**
     * 当前activity是否处于顶层
     *
     * @return true/false
     */
    public static boolean isTopActivity(Context context) {
        //先拿到所有的activity
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            ComponentName topActivity = tasksInfo.get(0).topActivity;
            String pkgName = topActivity.getPackageName();
            String shortClassName = topActivity.getShortClassName();
            String clsName = shortClassName.substring(1, shortClassName.length());
            if (pkgName.equals(context.getPackageName()) && clsName.equals(pkgName)) {
                return true;
            }
        }
        return false;
    }

    public static String getIntentContentToStr(Intent intent) {
        ComponentName component = intent.getComponent();
        String action = intent.getAction();
        String dataStr = intent.getDataString();
        String dataType = intent.getType();
        String packageName = intent.getPackage();
        String scheme = intent.getScheme();
        Set<String> categories = intent.getCategories();
        return "[Intent = ]: component=" + component + " ;action=" + action + " ;dataStr=" + dataStr + " ;packageName=" + packageName +
                " ;scheme=" + scheme + " ;dataType=" + dataType + " ;categories=" + categories;

    }

     /**
     * 获取程序的签名
     */
    public static String getAppSignature(Context context, String packname) {
        PackageInfo packinfo = getPackageInfoByName(context, packname, PackageManager.GET_SIGNATURES);
        if (packinfo != null) {
            return packinfo.signatures[0].toCharsString();
        } else {
            return "";
        }
     }

    /**
     * 获取指定apk的PackageInfo信息
     * @param context   context
     * @param packname  指定包名
     * @param flags     flags
     *                  Flag = GET_ACTIVITIES
     *                  Flag = GET_SERVICES
     *                  .....
     *                  .....
     * @return
     */
    public static PackageInfo getPackageInfoByName(Context context, String packname, int flags) {
        String name = packname;
        try {
            PackageManager pm = context.getPackageManager();
            if (TextUtils.isEmpty(packname)) {
                name = context.getPackageName();
            }
            return pm.getPackageInfo(name, flags);
        } catch (Exception e) {
            return null;
        }
    }
}
