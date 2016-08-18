package com.kingz.filemanager;

import android.app.Activity;
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

import com.adapter.FileListAdapter;
import com.kingz.customdemo.R;

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
 */
public class FileManagerActivity extends Activity implements
        AdapterView.OnItemClickListener,View.OnLongClickListener{

    private static final String  TAG= "FileManagerActivity";
    private ListView fileListView;
    private TextView titlePath;
    private TextView item_count;
    private FileListAdapter fileAdapter;
    private List<String> filePathsList;             //目录路径
    private List<String> fileNamesList;             //文件名
    private ArrayList<File> currentPageFilesList;              //文件名
    private boolean isRoot;

//    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();  //根目录
    private static final String ROOT_PATH = "sdcard";  //根目录
     //文件是否成功删除
    private boolean isDeletSuccess;

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

        String apkRoot = "chmod 777 " + getPackageCodePath();
        RootCommand(apkRoot);
    }

    /**
     * 修改Root权限
     * @param command
     * @return
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
        }
        catch (Exception e) {
            return false;
        }
        finally {
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
        currentPageFilesList = new ArrayList<File>();
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
            fileAdapter = new FileListAdapter(FileManagerActivity.this, currentPageFilesList,isRoot);
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

    /**
     * 列表单项点击
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "OnItemClickListener()..............");
        File file = (File) fileAdapter.getItem(position);
        //String path = filePathsList.get(position);
        //Log.d(TAG, "clicked path = " + path);
        //File file = new File(path);

        //文件不可读
        if(!file.canRead()){
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("权限不足")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }else if (file.exists() && file.canRead()) {
            if (file.isDirectory()) {
                showFileDir(file);
            }else{
                openFile(file);
            }
        }
    }

    /**
     * 打开选中的文件
     * @param file 所选文件
     */
     private void openFile(File file) {
         Intent intent = new Intent();
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         intent.setAction(Intent.ACTION_VIEW);
         String type = getMIMEType(file);
         Log.d(TAG, "Clicked File Type is :" + type);
         intent.setDataAndType(Uri.fromFile(file), type);   //打开设置打开文件的类型
         try {
             startActivity(intent);
         }
         catch (Exception e) {
             Toast.makeText(this, "未知类型，不能打开", Toast.LENGTH_SHORT).show();
        }
     }


   /**
   * 获取文件类型
   * @param file 目标文件
   * @return MIME类型值
   */
    private String getMIMEType(File file) {
        String type = "";
        String fileName = file.getName();
        int dotIndex = fileName.indexOf('.');
        if(dotIndex < 0) {
            return type;
        }
        String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
        if(TextUtils.equals("",end)) {
            return type;
        }
//        for(int i=0; i<MIME_MapTable.length; i++) {
//            if(end == MIME_MapTable[i][0]) {
//                type = MIME_MapTable[i][1] ;
//            }
//        }
        for (String[] aMIME_MapTable : MIME_MapTable) {
            if (TextUtils.equals(aMIME_MapTable[0],end)) {
                type = aMIME_MapTable[1];
            }
        }
        return type;
    }

    private final String[][] MIME_MapTable = {
        // {后缀名， MIME类型}
        { ".3gp", "video/3gpp" },
        { ".apk", "application/vnd.android.package-archive" },
        { ".asf", "video/x-ms-asf" },
        { ".avi", "video/x-msvideo" },
        { ".bin", "application/octet-stream" },
        { ".bmp", "image/bmp" },
        { ".c", "text/plain" },
        { ".class", "application/octet-stream" },
        { ".conf", "text/plain" },
        { ".cpp", "text/plain" },
        { ".doc", "application/msword" },
        { ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
        { ".xls", "application/vnd.ms-excel" },
        { ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
        { ".exe", "application/octet-stream" },
        { ".gif", "image/gif" },
        { ".gtar", "application/x-gtar" },
        { ".gz", "application/x-gzip" },
        { ".h", "text/plain" },
        { ".htm", "text/html" },
        { ".html", "text/html" },
        { ".jar", "application/java-archive" },
        { ".java", "text/plain" },
        { ".jpeg", "image/jpeg" },
        { ".jpg", "image/jpeg" },
        { ".js", "application/x-javascript" },
        { ".log", "text/plain" },
        { ".m3u", "audio/x-mpegurl" },
        { ".m4a", "audio/mp4a-latm" },
        { ".m4b", "audio/mp4a-latm" },
        { ".m4p", "audio/mp4a-latm" },
        { ".m4u", "video/vnd.mpegurl" },
        { ".m4v", "video/x-m4v" },
        { ".mov", "video/quicktime" },
        { ".mp2", "audio/x-mpeg" },
        { ".mp3", "audio/x-mpeg" },
        { ".mp4", "video/mp4" },
        { ".mpc", "application/vnd.mpohun.certificate" },
        { ".mpe", "video/mpeg" },
        { ".mpeg", "video/mpeg" },
        { ".mpg", "video/mpeg" },
        { ".mpg4", "video/mp4" },
        { ".mpga", "audio/mpeg" },
        { ".msg", "application/vnd.ms-outlook" },
        { ".ogg", "audio/ogg" },
        { ".pdf", "application/pdf" },
        { ".png", "image/png" },
        { ".pps", "application/vnd.ms-powerpoint" },
        { ".ppt", "application/vnd.ms-powerpoint" },
        { ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
        { ".prop", "text/plain" },
        { ".rc", "text/plain" },
        { ".rmvb", "audio/x-pn-realaudio" },
        { ".rtf", "application/rtf" },
        { ".sh", "text/plain" },
        { ".tar", "application/x-tar" },
        { ".tgz", "application/x-compressed" },
        { ".txt", "text/plain" },
        { ".wav", "audio/x-wav" },
        { ".wma", "audio/x-ms-wma" },
        { ".wmv", "audio/x-ms-wmv" },
        { ".wps", "application/vnd.ms-works" },
        { ".xml", "text/plain" },
        { ".z", "application/x-compress" },
        { ".zip", "application/x-zip-compressed" },
        { "", "*/*" }
    };


    @Override
    public void onBackPressed() {
        if(!isRoot){
            showFileDir(currentPageFilesList.get(0));
            return;
        }
        super.onBackPressed();
    }
}
