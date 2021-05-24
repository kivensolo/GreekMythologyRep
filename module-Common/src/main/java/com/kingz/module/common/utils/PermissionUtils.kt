package com.kingz.module.common.utils

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import org.jetbrains.annotations.NotNull

/**
 * 系统权限工具类
 */
class PermissionUtils private constructor() {
    interface PermissionCallBack {
        /**
         * 权限请求成功
         */
        fun onSuccess()

        /**
         * 用户拒绝了权限请求, 权限请求失败, 但还可以继续请求该权限
         *
         * @param permissions 请求失败的权限名
         */
        fun onFailure(permissions: List<String?>?)

        /**
         * 用户拒绝了权限请求并且用户选择了以后不再询问, 权限请求失败,
         * 这时将不能继续请求该权限, 需要提示用户进入设置页面打开该权限
         *
         * @param permissions 请求失败的权限名
         */
        fun onWithAskNeverAgain(permissions: List<String?>?)
    }


    companion object {
        const val TAG = "Permission"
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        )
        fun checkPermission(context: Context, permission: String): Int {
            return ActivityCompat.checkSelfPermission(context, permission)
        }
        fun requestPermission(vararg permissions: String?) {}
        /**
         * 验证有无读写权限
         * @param activity
         * @param requestCode
         * @return
         */
        fun verifyReadAndWritePermissions(activity: Activity, requestCode: Int): Boolean {
            val readResult =
                checkPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            val writeResult =
                checkPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (readResult != PackageManager.PERMISSION_GRANTED
                || writeResult != PackageManager.PERMISSION_GRANTED
                || readPhonestate(
                    activity,
                    null
                )
            ) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, requestCode)
                return false
            }
            return true
        }

        /**
         * 请求获取手机状态的权限
         */
        fun readPhonestate(@NotNull activity: Activity, callBack: PermissionCallBack?): Boolean {
            val readPhoneState =
                checkPermission(
                    activity,
                    Manifest.permission.READ_PHONE_STATE
                )
            return if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
                callBack?.onFailure(null)
                false
            }else{
                callBack?.onSuccess()
                true
            }
        }
    }

    /**
     * 获取通知权限
     * @param context
     */
    fun isNotificationEnabled(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.importance == NotificationManager.IMPORTANCE_NONE) {
                return false
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val CHECK_OP_NO_THROW = "checkOpNoThrow"
            val OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION"
            val mAppOps =
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val appInfo = context.applicationInfo
            val pkg = context.applicationContext.packageName
            val uid = appInfo.uid
            try {
                val appOpsClass =
                    Class.forName(AppOpsManager::class.java.name)
                val checkOpNoThrowMethod = appOpsClass.getMethod(
                    CHECK_OP_NO_THROW,
                    Integer.TYPE,
                    Integer.TYPE,
                    String::class.java
                )
                val opPostNotificationValue =
                    appOpsClass.getDeclaredField(OP_POST_NOTIFICATION)
                val value = opPostNotificationValue[Int::class.java] as Int
                return checkOpNoThrowMethod.invoke(
                    mAppOps,
                    value,
                    uid,
                    pkg
                ) as Int == AppOpsManager.MODE_ALLOWED
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
        return true
    }


    init {
        throw IllegalStateException("you can't instantiate me!")
    }
}