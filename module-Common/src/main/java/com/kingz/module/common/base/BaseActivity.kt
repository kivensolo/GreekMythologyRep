package com.kingz.module.common.base

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.kingz.base.view.dialog.ZDialogHelper
import com.kingz.module.common.AppLifeCycle
import com.zeke.kangaroo.utils.AppInfoUtils
import com.zeke.kangaroo.utils.ZLog

/**
 * author: King.Z
 * date: 2016 2016/3/27 18:26
 * des: MVP模式的基类
 */
abstract class BaseActivity : AppCompatActivity() {
    var isActivityShow = false
        protected set

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "BaseActivity onCreate()")
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(AppLifeCycle(TAG))
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        super.setContentView(view, params)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    override fun onResume() {
        super.onResume()
        isActivityShow = true
    }

    override fun onPause() {
        super.onPause()
        isActivityShow = false
    }

    fun showLoadingDialog() {
        ZLog.i(TAG,"showLoadingDialog")
        ZDialogHelper.showLoadingDialog(this)
    }

    fun dismissLoadingDialog() {
        ZLog.i(TAG,"showLoadingDialog")
        ZDialogHelper.dismissLoadingDialog()
    }

    //避免getActivity为空,不保存fragment的实例
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    /**
     * 设置窗口状态栏半透明
     * 需在setContentView方法之前调用
     */
    fun setStatusTranslucent() {
        setWindows(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    /**
     * 设置导航栏半透明
     * 需在setContentView方法之前调用
     */
    fun setNavigationTranslucent() {
        setWindows(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }

    /**
     * 动态修改Activity屏幕占比模式
     * @param flags 指定的布局样式Flag
     * @param mask  指定的布局样式mask
     */
    private fun setWindows(flags: Int, mask: Int) {
        window.setFlags(flags, mask)
    }

    /**
     * Android 8.0 中，Google移除掉了容易被滥用的“允许位置来源”应用的开关，
     * 在安装 Play Store 之外的第三方来源的 Android 应用的时候，没有了“允许未知来源”的检查框，
     * 如果你还是想要安装某个被自己所信任的开发者的 app，则需要在每一次都手动授予“安装未知应用”的许可。
     * @param uri 安装文件Url
     */
    protected fun installApk(uri: Uri?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //判断是否可以安装未知来源的应用 首次应该是false
            val can = packageManager.canRequestPackageInstalls()
            if (can) {
                AppInfoUtils.installApk(this, uri)
            } else { //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.REQUEST_INSTALL_PACKAGES),
                        INSTALL_PACKAGES_REQUESTCODE)
            }
        } else {
            AppInfoUtils.installApk(this, uri)
        }
    }

    protected fun onInstallPackagesPermissionOk() {}
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            INSTALL_PACKAGES_REQUESTCODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onInstallPackagesPermissionOk()
            } else { // 引导至安装未知应用界面
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GET_UNKNOWN_APP_SOURCES -> {
            }
            else -> {
            }
        }
    }

    companion object {
        private val TAG = BaseActivity::class.java.simpleName
        const val GET_UNKNOWN_APP_SOURCES = 0x10000
        const val INSTALL_PACKAGES_REQUESTCODE = 0x10001
        private var mNonCompatDensity = 0f
        private var mNonCompatScaleDensity = 0f
        /**
         * 今日头条屏幕适配方案，以设计图宽360dp的去适配页面
         * 在onCreate的时候调用
         * px = dp * density = dp * (dpi/160)
         * dpi = (sqre(w^2 + h^2) px / 屏幕尺寸 inch ) = 机型不同尺寸不同
         *
         * 以1080P 5.0寸设备为例：
         *  对角线像素为2203
         *  dpi = 2203 /5 ≈ 440
         *  density = 440 / 160 = 2.75
         *
         * @param act
         * @param application
         */
        private fun setCustomDensity(act: Activity, application: Application) {
            val appDisplayMetrics = application.resources.displayMetrics
            if (mNonCompatDensity == 0f) {
                mNonCompatDensity = appDisplayMetrics.density
                mNonCompatScaleDensity = appDisplayMetrics.scaledDensity
                //监听字体切换
                application.registerComponentCallbacks(object : ComponentCallbacks {
                    override fun onConfigurationChanged(newConfig: Configuration) {
                        if (newConfig.fontScale > 1) {
                            mNonCompatScaleDensity = application.resources.displayMetrics.scaledDensity
                        }
                    }

                    override fun onLowMemory() {}
                })
            }
            //以宽度为基准
            val targetDensity = appDisplayMetrics.widthPixels / 360.toFloat()
            val targetScaleDensity =
                    targetDensity * (mNonCompatScaleDensity / mNonCompatDensity)
            val targetDensityDPI = (160 * targetDensity).toInt()
            appDisplayMetrics.density = targetDensity
            appDisplayMetrics.scaledDensity = targetScaleDensity
            appDisplayMetrics.densityDpi = targetDensityDPI
            val activityDisplayMetries = act.resources.displayMetrics
            activityDisplayMetries.density = targetDensity
            activityDisplayMetries.scaledDensity = targetScaleDensity
            activityDisplayMetries.densityDpi = targetDensityDPI
        }
    }
}