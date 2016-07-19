package com.kingz.uiusingActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.App;
import com.kingz.uiusingListViews.R;
import com.utils.FileDownloader;
import com.utils.NetTools;
import com.utils.ToastTools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by KingZ on 2015/11/3.
 * Discription:图片/文件下载测试
 */
public class DownloadTestActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "DownloadTestActivity";
    private Button btn_NetPic, btn_LocalPic, btn_downLoad;
    private ImageView imgNetPic, imgResPic;
    private FileDownloader _downloader;
    private Context context;
    /**
     * APP下载路径
     */
    private String appUrlPath = "http://192.168.90.115/pub_apk/update-test/V4.6.0_XJCBC_IPTV_QUANZHI_Beta_MG_22773_181.apk";
    private URLConnection urlConnection;
    private InputStream downloadIs = null;
    private OutputStream downloadOs = null;
    private File downloadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = App.getAppContext();
        setContentView(R.layout.bitmap_demo_layout);
        initviews();
    }

    private void initviews() {
        btn_NetPic = (Button) findViewById(R.id.loadpic_btn);
        btn_LocalPic = (Button) findViewById(R.id.localpic_btn);
        imgNetPic = (ImageView) findViewById(R.id.image_net);
        imgResPic = (ImageView) findViewById(R.id.image_local);
        btn_downLoad = (Button) findViewById(R.id.btn_down_app);

        btn_NetPic.setOnClickListener(this);
        btn_LocalPic.setOnClickListener(this);
        btn_downLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_down_app:
                ToastTools.getInstance().showToast(context,"开始下载");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startDownLoadFile(appUrlPath);
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    /**
     * 开始下载文件
     */
    public void startDownLoadFile(String url) {
        url = url.trim();
        if (TextUtils.isEmpty(url)) {
            Log.e(TAG, "url is empty!");
            ToastTools.getInstance().showToast(context,"url is empty!");
            return;
        }
        if (!NetTools.isNetworkConnected(context)) {
            Log.e(TAG, "网络未连接,无法进一步下载");
            return;
        }
        //Java方式连接网络
        connectByJava(url);
        //确定文件保存路径
        downloadFile = createFileDir();

        _downloader = new FileDownloader();
        _downloader.start(url, downloadFile, true, new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
//                onHttpMessage(msg);
                Log.i(TAG, "msg.msg = " + msg.what);
                return false;
            }
        }));

    }

    @NonNull
    private File createFileDir() {
        String filePath = Environment.getExternalStorageDirectory()+ "/TempFile";
        File path = new File(filePath);
        if(!path.exists()){
            path.mkdir();
        }
        String apkName = "132131.apk";
        String apkSavePath = filePath + "/" + apkName;
        File downloadFile = new File(apkSavePath);
        if(!downloadFile.exists()){
            try {
                downloadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return downloadFile;
    }

    private void connectByJava(String urlPath) {
        try {
            URL url = new URL(urlPath);
            urlConnection = url.openConnection();
            if(urlConnection.getReadTimeout() >= 5){
                Log.i(TAG,"网络连接超时  不继续下载");
                return;
            }
            downloadIs = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
