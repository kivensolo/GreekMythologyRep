package com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.ContentResolver;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import com.kingz.utils.ZLog;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/27 18:26
 */
@SuppressLint("Registered")
public class BaseActivity extends Activity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    public ContentResolver baseResolver;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"BaseActivity onCreate()");
        super.onCreate(savedInstanceState);
        findID();
        baseResolver = getContentResolver();
    }

    /**
     * 获取资源
     */
    protected void findID() {
    }

    //protected void AddToolbar() {
    //    ListView toolbar = (ListView) findViewById(R.id.list_view);
    //
    //}

    /**
     * 初始
     */
    public void InData() {
    }

    /**
     * 监听
     */
    protected void Listener() {
    }

    /**
     * 对传递数据处
     */
    protected void initIntent() {

    }



    public boolean isLoadding =false;

    public void showLoadingDialog() {
        if (!isFinishing()) {
            ZLog.i(TAG, "showLoadingDialog");
            if (isLoadding) {
                return;
            }
            isLoadding = true;
            isLoadding = showDialog(5, null);
        }
    }

    public void dismissLoadingDialog() {
        if (!isFinishing()) {
            if (isLoadding) {
                ZLog.i(TAG, "dismissLoadingDialog");
                dismissDialog(5);
            }
            isLoadding = false;
        }
    }

    private static float mNonCompatDensity;
    private static float mNonCompatScaleDensity;

    /**
     * 今日头条屏幕适配方案，以设计图宽360dp的去适配页面
     * 在onCreate的时候调用
     * px = dp * density = dp * (dpi/160)
     * dpi = (sqre(w^2 + h^2) px / 屏幕尺寸 inch ) = 机型不同尺寸
     *
     * 1920 * 1080 机子: density == 440
     * @param act
     * @param application
     */
    private static void setCustomDensity(Activity act, final Application application){
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if(mNonCompatDensity == 0){
            mNonCompatDensity = appDisplayMetrics.density;
            mNonCompatScaleDensity = appDisplayMetrics.scaledDensity;
            //监听字体切换
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if(newConfig != null && newConfig.fontScale > 1){
                        mNonCompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {}
            });
        }
        //以宽度为基准
        final float targetDensity = appDisplayMetrics.widthPixels / 360;
        final float targetScaleDensity = targetDensity * (mNonCompatScaleDensity / mNonCompatDensity);
        final int targetDensityDPI = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaleDensity;
        appDisplayMetrics.densityDpi = targetDensityDPI;

        final DisplayMetrics activityDisplayMetries = act.getResources().getDisplayMetrics();
        activityDisplayMetries.density = targetDensity;
        activityDisplayMetries.scaledDensity = targetScaleDensity;
        activityDisplayMetries.densityDpi = targetDensityDPI;
    }

}
