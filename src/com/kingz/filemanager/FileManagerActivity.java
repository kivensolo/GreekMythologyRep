package com.kingz.filemanager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.customview.listview.CustomSliderDeleteListView;
import com.kingz.uiusingListViews.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/2/26 13:50
 * description:文件管理器
 */
public class FileManagerActivity extends Activity implements  AdapterView.OnItemClickListener,
                                                    View.OnLongClickListener{

    private static final String TAG = "FileManagerActivity";
    private CustomSliderDeleteListView sliderListView;
    private ArrayAdapter<String> adapter;
    private List<String> filePaths;  //目录路径S
    private List<String> fileNames;  //文件名S

    private static final String ROOT_PATH = "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_list);
        initViews();
        showFileDir(ROOT_PATH);
    }

    private void initViews() {
        sliderListView = (CustomSliderDeleteListView) findViewById(R.id.sliderCustomListView);
        sliderListView.setOnLongClickListener(this);
        sliderListView.setOnItemClickListener(this);
    }


    /**
     * File.renameTo(dest);
     * 获得文件或文件夹的绝对路径和相对路径。区别
     * String path = File.getPath();//相对
     * String path = File.getAbsoultePath();//绝对
     * b.获得文件或文件夹的父目录
     * String parentPath = File.getParent();
     * c.获得文件或文件夹的名称：
     * String Name = File.getName();
     *
     * (6)删除文件夹或文件
         File.delete();
     */

    /**
     * 显示文件列表
     */
    private void showFileDir(String path) {
        filePaths = new ArrayList<String>();
        fileNames = new ArrayList<String>();
        //判断SD卡是否插入,外置储存状态是否是“mounted”
        if(TextUtils.equals(android.os.Environment.MEDIA_MOUNTED,Environment.getExternalStorageState())){
            //File skRoot  = Environment.getExternalStorageDirectory();
            File file = new File(path);
            File[] files = file.listFiles();
            for(File f:files){
                fileNames.add(f.getName());
                filePaths.add(f.getPath());
                Log.i(TAG,"储存卡目录+1：" + f.getName());
            }
        }else{
            Log.d(TAG, "无外置储存卡");
        }
        //else if(){
        //
        //}
        adapter = new ArrayAdapter<String>(this, R.layout.filemanager_list_item, R.id.list_item, fileNames);
        sliderListView.setAdapter(adapter);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "OnItemClickListener()..............");
        String path = filePaths.get(position);
        Log.d(TAG, "clicked path = " + path);
        File file = new File(path);
        // 文件存在并可读
        if (file.exists() && file.canRead()) {
            if (file.isDirectory()) {
                //显示子目录及文件
                showFileDir(path);
            }
            //else{
            //    //处理文件
            //    fileHandle(file);
            //}
        } else {

        }
    }
}
