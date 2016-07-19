package com.kingz.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kingz.customDemo.R;

import java.io.File;

/**
 * Created by KingZ on 2015/11/1.
 * Discription:文件或者图片等的Demo
 */
public class FileAndPicTestACT extends Activity implements View.OnClickListener{

    private static final String TAG = FileAndPicTestACT.class.getSimpleName();
    private boolean isSdcardExist;          //是否有SD卡挂载
    private String sdPath;                  //SD卡路径
    private EditText infoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_pictures);
        TextView tv = (TextView) findViewById(R.id.show_text);
        infoEditText = (EditText) findViewById(R.id.file_edittext);
        Button scanSdbtn = (Button) findViewById(R.id.sd_button);
        Button defaultFileSbtn = (Button) findViewById(R.id.default_filepath_btn);
        Button saveDatabtn = (Button) findViewById(R.id.save_data);
        Button intoFileSavePageBtn = (Button) findViewById(R.id.btn_intoFileSavePage);

        scanSdbtn.setOnClickListener(this);
        defaultFileSbtn.setOnClickListener(this);
        saveDatabtn.setOnClickListener(this);
        intoFileSavePageBtn.setOnClickListener(this);
        getFileFloderPath();
        Log.i(TAG, "SD卡路径：" + sdPath);

    }

    /**
     * 判断SD卡是否存在
     */
    private void ifSdCardExist() {
        isSdcardExist = Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED);
        if(isSdcardExist){
            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            Toast.makeText(this,"SDcard已经挂载，路径为："+sdPath,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"SDcard未挂载",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 利用Environment类下面的几个静态方法对SDcard进行读写
     *
     *  1:getDataDirectory() 获取到Android中的data数据目录（sd卡中的data文件夹）
        2:getDownloadCacheDirectory() 获取到下载的缓存目录（sd卡中的download文件夹）
        3:getExternalStorageDirectory() 获取到外部存储的目录 一般指SDcard（/storage/sdcard0）
        4:getExternalStorageState() 获取外部设置的当前状态 一般指SDcard，
               比较常用的应该是 MEDIA_MOUNTED（SDcard存在并且可以进行读写）还有其他的一些状态，可以在文档中进行查找。
        5:getRootDirectory()  获取到Android Root路径
     */
    private String getFileFloderPath() {
        if(isSdcardExist){
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
            sdPath = "不存在";
        }

//        File datapath = Environment.getDataDirectory();//获取到Android中的data数据目录（sd卡中的data文件夹）
        return sdPath;
    }

    /**
     * 获取默认的文件路径
     *
     * @return
     */
    public String getDefaultFilePath() {
        String filepath = "";
        File file = new File(Environment.getExternalStorageDirectory(),"abc.txt");  //路径，文件名
        if (file.exists()) {
            filepath = file.getAbsolutePath();
        } else {
            filepath = "默认文件不存在";
        }
        Toast.makeText(FileAndPicTestACT.this,"默认文件:"+filepath,Toast.LENGTH_SHORT).show();
        return filepath;
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick() " + v.getId());
        switch (v.getId()){
            case R.id.sd_button:
                ifSdCardExist();
                break;
            case R.id.default_filepath_btn:
                getDefaultFilePath();
                break;
            case R.id.save_data:
                saveDataToDisk();
                break;
            case R.id.btn_intoFileSavePage:
                intoFileSavePage();
            default:
        }
    }

    /**
     * 存储数据
     */
    private void saveDataToDisk() {
        String data = infoEditText.getText().toString();
        if(data == null){
            Toast.makeText(FileAndPicTestACT.this,"请输入数据！！",Toast.LENGTH_SHORT).show();
        }else{
         //执行存储的动作


        }
    }
    private void intoFileSavePage(){
        Intent intent = new Intent();
        intent.setClass(this, SaveDataByFilePage.class);
        startActivity(intent);
    }

}
