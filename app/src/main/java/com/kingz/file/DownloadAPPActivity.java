package com.kingz.file;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.base.BaseActivity;
import com.kingz.customdemo.R;
import com.kingz.utils.ToastTools;
import com.kingz.work.FileDownloadWorker;
import com.kingz.work.FileDownloader;
import com.module.tools.ScreenTools;
import com.module.views.progress.HorizontalProgressBarNoNumber;
import com.zeke.kangaroo.utils.NetUtils;
import com.zeke.kangaroo.utils.ToastUtils;
import com.zeke.ktx.App;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by KingZ on 2015/11/3.
 * Discription:图片/文件下载测试
 * //TODO 需要重构
 */
public class DownloadAPPActivity extends BaseActivity implements
        View.OnClickListener,LifecycleOwner {

    public static final String TAG = "DownloadAPPActivity";
    public static final int PAGE_ID = R.layout.bitmap_demo_layout;
    public static final int TIMEOUT_MILLIS = 10 * 1000;         //网络超时时间阀值
    private Button btn_downLoad;
    private FileDownloader _downloader;
    private Context context;
    private String appUrlPath = "http://gyxza3.eymlz.com/yq/yx_lm1/gat5sjb.apk";
    private HttpURLConnection urlConnection;
    private InputStream downloadIs = null;
    private File downloadFile;
    private HorizontalProgressBarNoNumber mProgressBarView;
    private float percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = App.instance.getApplicationContext();
        setContentView(PAGE_ID);
        initviews();
    }

    private void initviews() {
        initProgressView();
        btn_downLoad = (Button) findViewById(R.id.btn_down_app);
        btn_downLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_down_app:
                ToastUtils.show(context, "开始下载....");
                startDownLoadFile(appUrlPath);
                break;
            default:
                break;
        }
    }

    /**
     * 进度条初始化
     */
    private void initProgressView() {
        mProgressBarView = new HorizontalProgressBarNoNumber(context);
        mProgressBarView.setTotalWidth(ScreenTools.SCREEN_WIDTH / 2);
        mProgressBarView.setInnerPaintColor(0xFF9dbaf2);
        mProgressBarView.setOuterPaintColor(0x19ffffff);
        mProgressBarView.setProgressCompleteListener(new HorizontalProgressBarNoNumber.ProgressCompleteListener() {
            @Override
            public void onComplete() {
                ToastTools.getInstance().showMgtvWaringToast(context, "Done!!");
                mProgressBarView.setVisibility(View.INVISIBLE);
            }
        });
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(mProgressBarView, lps);
        mProgressBarView.beginDraw();
    }

    /**
     * 文件下载初始化
     */
    public void startDownLoadFile(String url) {
        url = url.trim();
        if (TextUtils.isEmpty(url)) {
            Log.e(TAG, "url is empty!");
            ToastTools.getInstance().showToast(context, "url is empty!");
            return;
        }
        if (!NetUtils.isConnect(context)) {
            Log.e(TAG, "网络未连接,无法进一步下载");
            ToastTools.getInstance().showMgtvWaringToast(context, "url is empty!");
            return;
        }
//        Java方式连接网络
//        connectByJava(url);
        downloadFile = createFileDir(); //确定文件保存路径

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiresStorageNotLow(true)
                .build();

        Data urlData = new Data.Builder()
                .putString(FileDownloadWorker.KEY_ADDR_URI,url)
                .build();

        OneTimeWorkRequest fileDownloadRequest = new OneTimeWorkRequest
                .Builder(FileDownloadWorker.class)
                .setInitialDelay(2,TimeUnit.SECONDS) // 设置执行延迟
                .setConstraints(constraints) //设置约束
                .setInputData(urlData) // 设置输入参数(最大10KB)
                .addTag("download")  // 设置Worker的Tag
                .build();
        WorkManager.getInstance().enqueue(fileDownloadRequest);

        WorkManager.getInstance().getWorkInfosByTagLiveData("download")
                .observe(this, new Observer<List<WorkInfo>>() {
                    @Override
                    public void onChanged(@Nullable List<WorkInfo> workInfos) {
                        if (workInfos == null ||  workInfos.size() == 0) {
                            return;
                        }
                        WorkInfo workInfo = workInfos.get(0);
                        Data outputData = workInfo.getOutputData();

                        if(workInfo.getState() == WorkInfo.State.SUCCEEDED){
                            ToastTools.getInstance().showMgtvWaringToast(context, outputData.getString("result"));
                        }
                    }
                    // 目前1.0.1的版本不支持观察worker工作进程中的功能。 在2.3.0-alpha01版本才支持
                });

        //TODO 替换 FileDownloader
//        _downloader = new FileDownloader();
//        _downloader.start(url, downloadFile, true, new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                Log.i(TAG, "msg.what = " + msg.what +"; obj = " + msg.obj);
//                switch (msg.what){
//                    case STARTING:
//                        break;
//                    case RECIVING:
//                        break;
//                    case PROGRESSING:
//                        percent = (float) msg.obj;
//                        if(percent >= 0.9){
//                            percent = (float) 1.0;
//                        }
//                        mProgressBarView.setWidth((int) (percent * 600));
//                        break;
//                    case ERROR:
//                        break;
//                    case FINISHED:
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        }));
    }

    private File createFileDir() {
        String filePath = Environment.getExternalStorageDirectory() + "/TempFile";
        File path = new File(filePath);
        if (!path.exists()) {
            path.mkdir();
        }
        String apkName = "132131.apk";
        String apkSavePath = filePath + "/" + apkName;
        File downloadFile = new File(apkSavePath);
        if (!downloadFile.exists()) {
            try {
                downloadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return downloadFile;
    }

    private InputStream connectByJava(String urlPath) {
        try {
            URL url = new URL(urlPath);
            if (url != null) {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(TIMEOUT_MILLIS);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("GET");
                if (urlConnection.getResponseCode() == 200) {
                    downloadIs = urlConnection.getInputStream();
                }
            }
            return downloadIs;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
