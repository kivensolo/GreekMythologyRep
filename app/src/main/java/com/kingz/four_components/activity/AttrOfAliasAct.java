package com.kingz.four_components.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zeke.kangaroo.utils.ZLog;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2017/1/6 16:37 <br>
 * description: 通过Acticity-Alias属性改变应用图标<br>
 */
public class AttrOfAliasAct extends Activity {
    private Button bt_1;
    //private Button bt_2;
    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout rootView = new LinearLayout(this);
        rootView.setBackgroundColor(Color.DKGRAY);
        rootView.setPadding(0, 30, 0, 0);
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bt_1 = new Button(this);
        bt_1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //bt_2 = new Button(this);
        //bt_2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootView.setLayoutParams(lps);
        rootView.addView(bt_1);
        //rootView.addView(bt_2);
        setContentView(rootView);
        bt_1.setText("Switch Component To 111111");
        //bt_2.setText("Switch Component To 12");

        pm = getPackageManager();

        final ComponentName defaultCName = new ComponentName(getBaseContext(), "com.zeke.module_login.SplashActivity");
        final ComponentName mTest11 = new ComponentName(getBaseContext(), "com.kingz.pages.Test11");
        //final ComponentName mTest12 = new ComponentName(getBaseContext(), "com.Test12");

        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZLog.d("AttrOfAliasAct", "Switch Component To 11");
                enableComponent(mTest11);
                disableComponent(defaultCName);
                //disableComponent(mTest12);
                //Intent intent = new Intent(Intent.ACTION_MAIN);
                //intent.addCategory(Intent.CATEGORY_HOME);
                //intent.addCategory(Intent.CATEGORY_DEFAULT);
                //List<ResolveInfo> resolves = pm.queryIntentActivities(intent, 0);
                //for (ResolveInfo res : resolves) {
                //    if (res.activityInfo != null) {
                //        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                //        am.killBackgroundProcesses(res.activityInfo.packageName);
                //    }
                //}
            }
        });

        //bt_2.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        ZLog.d("AttrOfAliasAct", "Switch Component To 11");
        //        enableComponent(mTest12);
        //        disableComponent(defaultCName);
        //        disableComponent(mTest11);
        //    }
        //});
    }

    private void enableComponent(ComponentName componentName) {
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void disableComponent(ComponentName componentName) {
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

}
