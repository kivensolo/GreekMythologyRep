package com;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/27 18:26
 */
public class BaseActivity extends Activity {

    /**
     * 全局变量
     */
    public static Context mContext;

    /** 数据提供者 */
    public ContentResolver baseResolver;

    private Toast mToast;

    public static Context getAppContext() {
        return mContext;
    }

    public void showLoadingDialog() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findID();
        baseResolver = getContentResolver();
    }

    /**
     * 获取资源
     */
    protected void findID() {
    }

    ///**
    // * 添加头部
    // */
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

}
