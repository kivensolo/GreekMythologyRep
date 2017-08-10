package com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.utils.ZLog;

import java.io.File;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/27 18:26
 */
@SuppressLint("Registered")
public class BaseActivity extends Activity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    /**
     * 全局Context
     */
    public static Context mContext;

    /**
     * 数据提供者
     */
    public ContentResolver baseResolver;

    private Toast mToast;

    private final String[][] MIME_MapTable = {
            // {后缀名， MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    public static Context getAppContext() {
        return mContext;
    }

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


    /**
     * 打开选中的文件
     *
     * @param file 所选文件
     */
    public void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMIMEType(file);
        Log.d(TAG, "Clicked File Type is :" + type);
        intent.setDataAndType(Uri.fromFile(file), type);   //打开设置打开文件的类型
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "未知类型，不能打开", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取文件类型
     * @param file 目标文件
     * @return MIME类型值
     */
    public String getMIMEType(File file) {
        String type = "";
        String fileName = file.getName();
        int dotIndex = fileName.indexOf('.');
        if (dotIndex < 0) {
            return type;
        }
        String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
        if (TextUtils.equals("", end)) {
            return type;
        }
//        for(int i=0; i<MIME_MapTable.length; i++) {
//            if(end == MIME_MapTable[i][0]) {
//                type = MIME_MapTable[i][1] ;
//            }
//        }
        for (String[] aMIME_MapTable : MIME_MapTable) {
            if (TextUtils.equals(aMIME_MapTable[0], end)) {
                type = aMIME_MapTable[1];
            }
        }
        return type;
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
