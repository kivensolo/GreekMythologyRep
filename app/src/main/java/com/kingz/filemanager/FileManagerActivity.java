package com.kingz.filemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.BaseActivity;
import com.kingz.customdemo.R;
import com.kingz.filemanager.adapter.FileListAdapter;
import com.kingz.utils.FileUtils;
import com.kingz.utils.ZLog;

import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/2/26 13:50
 * description:文件管理器
 * //TODo 增加文件删除工鞥
 * //TODo 完善文件图标
 */
public class FileManagerActivity extends BaseActivity implements AdapterView.OnItemClickListener,View.OnLongClickListener{

    private static final String  TAG= "FileManagerActivity";
    //TODO View要抽离
    private ListView fileListView;
    private TextView titlePath;
    private TextView item_count;
    private FileListAdapter fileAdapter;
    private List<String> filePathsList;             //目录路径
    private List<String> fileNamesList;             //文件名
    private ArrayList<File> currentPageFilesList;   //文件名
    private boolean isRoot;

    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();  //  /mnt/shell/emulated/0   一般就是SD卡
//    private static final String ROOT_PATH = "sdcard";  //根目录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "FileManagerActivity --- onCreate()");
        setContentView(R.layout.slider_list);
        initViews();
        File floder = new File(ROOT_PATH);
        showFileDir(floder);
    }

    private void initViews() {
        titlePath = (TextView) findViewById(R.id.dir_title_id);
        item_count = (TextView) findViewById(R.id.item_count);
        fileListView = (ListView) findViewById(R.id.sliderCustomListView);
        fileListView.setOnLongClickListener(this);
        fileListView.setOnItemClickListener(this);
        //获取root权限
        String apkRoot = "chmod 777 " + getPackageCodePath();
        if(RootCommand(apkRoot)){
            ZLog.d(TAG,"root权限获取成功");
        }else{
            ZLog.w(TAG,"root权限获取失败");
        }
    }

    /**
     * 获取root权限
     * @param command
     * @return command result.
     */
    public static boolean RootCommand (String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        }catch (Exception e) {
            return false;
        }finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
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
    private void showFileDir(File folder) {
        isRoot = TextUtils.isEmpty(folder.getParent()); //是否是根目录
        Log.d(TAG, "showFileDir isRoot = " + isRoot);
        currentPageFilesList = new ArrayList<>();
        filePathsList = new ArrayList<>();
        fileNamesList = new ArrayList<>();
        if(!isRoot){
            currentPageFilesList.add(folder.getParentFile());
            Log.d(TAG,"非根目录，添加上页目录 = " + currentPageFilesList.toString());
        }

        //判断SD卡是否插入,外置储存状态是否是“mounted”
        if(TextUtils.equals(Environment.MEDIA_MOUNTED,Environment.getExternalStorageState())){
            //File skRoot  = Environment.getExternalStorageDirectory();
            File[] files = folder.listFiles();
            titlePath.setText(folder.getAbsolutePath());
            Log.i(TAG,"currentPage files = " + files.length);
            item_count.setText("共" + files.length + "项");
            if (files.length > 0) {
                for (File f : files) {
                    currentPageFilesList.add(f);
                    fileNamesList.add(f.getName());
                    filePathsList.add(f.getPath());
                }
            }
            fileAdapter = new FileListAdapter(this, currentPageFilesList,isRoot);
            fileListView.setAdapter(fileAdapter);
        }else{
            Log.d(TAG, "无外置储存卡");
        }
//        fileAdapter = new FileListAdapter(FileManagerActivity.this,currentPageFilesList,isRoot);
//        fileListView.setAdapter(fileAdapter);
    }

    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"rename file", "delete file"};
        builder.setItems(items, null);
        builder.create().show();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "OnItemClickListener()..............");
        File file = (File) fileAdapter.getItem(position);
        if(!file.canRead()){
            new AlertDialog.Builder(this).setTitle("提示").setMessage("权限不足")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {}
                                        }).show();
        }else if (file.exists() && file.canRead()) {
            if (file.isDirectory()) {
                showFileDir(file);
            }else{
                openFile(file);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(!isRoot){
            showFileDir(currentPageFilesList.get(0));
            return;
        }
        super.onBackPressed();
    }

    public void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = FileUtils.getMIMEType(file);
        Log.d(TAG, "Clicked File Type is :" + type);
        intent.setDataAndType(Uri.fromFile(file), type);   //打开设置打开文件的类型
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "未知类型，不能打开", Toast.LENGTH_SHORT).show();
        }
    }
}
