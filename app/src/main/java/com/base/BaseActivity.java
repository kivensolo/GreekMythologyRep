package com.base;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.kingz.utils.ZLog;

import butterknife.ButterKnife;

/**
 * author: King.Z
 * date: 2016 2016/3/27 18:26
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    public boolean isLoadding =false;
    protected boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"BaseActivity onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        bindButterKnife();
    }


    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        bindButterKnife();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindButterKnife();
    }

    private void bindButterKnife() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShow = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        isShow = false;
    }

    public void showLoadingDialog() {
        if (!isFinishing()) {
            ZLog.i(TAG, "showLoadingDialog");
            if (isLoadding) {
                return;
            }
            isLoadding = true;
            //TODO 替换新版本的加载圈
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

    public boolean isActivityShow() {
        return isShow;
    }


    //避免getActivity为空,不保存fragment的实例
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    /**
     * 设置窗口状态栏半透明
     * 需在setContentView方法之前调用
     */
    public void setStatusTranslucent() {
        setWindows(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }


    /**
     * 设置导航栏半透明
     * 需在setContentView方法之前调用
     */
    public void setNavigationTranslucent() {
        setWindows(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    /**
     * 动态修改Activity屏幕占比模式
     * @param flags 指定的布局样式Flag
     * @param mask  指定的布局样式mask
     */
    private void setWindows(int flags, int mask){
        getWindow().setFlags(flags,mask);
    }
}
