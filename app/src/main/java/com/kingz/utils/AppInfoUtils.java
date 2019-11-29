package com.kingz.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Set;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.kingz.utils.DevicesInfoUtils.getDeviceUsableMemory;

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
 *     @see #getPackageInfoByName(Context, String, int)
 *     @see #hexdigest(byte[])
 *     @see #gc(Context)
 *     @see #getProcessNameByPid(int)
 */
public class AppInfoUtils {
    private static final boolean DEBUG = false;
    private static final String TAG = AppInfoUtils.class.getSimpleName();

    private AppInfoUtils() {
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
     * 安装apk
     *
     * @param context 上下文
     * @param uri    APK文件uri
     */
    public static void installApk(Context context, Uri uri) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载apk
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
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

    /**
     * 将签名字符串转换成需要的32位签名
     *
     * @param paramArrayOfByte 签名byte数组
     * @return 32位签名字符串
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97,
                98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 清理后台进程与服务
     * @param context 应用上下文对象context
     * @return 被清理的数量
     */
    public static int gc(Context context) {
        long i = DevicesInfoUtils.getDeviceUsableMemory(context);
        int count = 0; // 清理掉的进程数
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的service列表
        List<RunningServiceInfo> serviceList = am.getRunningServices(100);
        if (serviceList != null){
            for (ActivityManager.RunningServiceInfo service : serviceList) {
                if (service.pid == android.os.Process.myPid())
                    continue;
                try {
                    android.os.Process.killProcess(service.pid);
                    count++;
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }

        // 获取正在运行的进程列表
        List<RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        if (processList != null){
            for (RunningAppProcessInfo process : processList) {
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (process.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    // pkgList 得到该进程下运行的包名
                    String[] pkgList = process.pkgList;
                    for (String pkgName : pkgList) {
                        if (DEBUG) {
                            ZLog.d(TAG, "======正在杀死包名：" + pkgName);
                        }
                        try {
                            am.killBackgroundProcesses(pkgName);
                            count++;
                        } catch (Exception e) { // 防止意外发生
                            e.getStackTrace();
                        }
                    }
                }
            }
        }
        if (DEBUG) {
            ZLog.d(TAG, "清理了" + (getDeviceUsableMemory(context) - i) + "M内存");
        }
        return count;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessNameByPid(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
