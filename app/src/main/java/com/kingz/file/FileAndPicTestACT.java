package com.kingz.file;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.base.BaseActivity;
import com.kingz.customdemo.R;
import com.kingz.pages.SaveDataByFilePage;

import java.io.File;

/**
 * Created by KingZ on 2015/11/1.
 * Discription:文件或者图片等的Demo
 * //TODo 文件处理  功能待扩展
 *
 * 利用Environment类下面的几个静态方法对SDcard进行读写
 *  1:getDataDirectory() 获取到Android中的data数据目录（sd卡中的data文件夹）
 *  2:getDownloadCacheDirectory() 获取到下载的缓存目录（sd卡中的download文件夹）
 *  3:getExternalStorageDirectory() 获取到外部存储的目录 一般指SDcard（/storage/sdcard0）
 *  4:getExternalStorageState() 获取外部设置的当前状态 一般指SDcard，
 *         比较常用的应该是 MEDIA_MOUNTED（SDcard存在并且可以进行读写）还有其他的一些状态，可以在文档中进行查找。
 *  5:getRootDirectory()  获取到Android Root路径
 *
 *  root:
 *      sdcard -> /storage/emulated/legacy
 *
 *  storage--|
 *         ---|emulated
 *            ---|legacy -> /mnt/shell/emulated/0
 *         ---|external_storage
 *         ---|sdcard0 -> /storage/emulated/legacy
 *
 *  mnt--| 挂载点
 *      ---|NetShareDirs
 *      ---|asec
 *      ---|external_sd -> /storage/external_storage/sdcard1
 *      ---|extsd -> /storage/external_storage/sdcard1
 *      ---|media_rw
 *      ---|obb
 *      ---|sdcard -> /storage/emulated/legacy
 *      ---|secure
 *      ---|shell
 *        ---| 0  [真正的sdcard路径]
 *        ---|legacy
 *        ---|obb
 *      ---|smb
 *
 */
public class FileAndPicTestACT extends BaseActivity implements View.OnClickListener{

    private static final String TAG = FileAndPicTestACT.class.getSimpleName();
    private boolean isSdcardExist;
    private String sdPath;
    private EditText infoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_pictures);
        TextView tv = (TextView) findViewById(R.id.show_text);
        infoEditText = (EditText) findViewById(R.id.file_edittext);
        Button defaultFileSbtn = (Button) findViewById(R.id.default_filepath_btn);
        Button intoFileSavePageBtn = (Button) findViewById(R.id.btn_intoFileSavePage);

        defaultFileSbtn.setOnClickListener(this);
        intoFileSavePageBtn.setOnClickListener(this);
        getFileFloderPath();
    }

    private String getFileFloderPath() {
        if(isSdcardExist){
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
            sdPath = "不存在";
        }
        Log.i(TAG, "SD卡路径：" + sdPath);
        return sdPath;
    }

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
                break;
            case R.id.default_filepath_btn:
                getDefaultFilePath();
                break;
            case R.id.btn_intoFileSavePage:
                intoFileSavePage();
            default:
        }
    }
    private void intoFileSavePage(){
        Intent intent = new Intent();
        intent.setClass(this, SaveDataByFilePage.class);
        startActivity(intent);
    }

}
