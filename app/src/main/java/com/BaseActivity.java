package com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
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

}
