package com.kingz.file;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.App;
import com.BaseActivity;
import com.core.logic.GlobalCacheCenter;
import com.kingz.FileDownloader;
import com.kingz.customdemo.R;
import com.kingz.customviews.grogress.HorizontalProgressBarView;
import com.kingz.utils.NetTools;
import com.kingz.utils.ToastTools;
import com.kingz.utils.ZLog;
import com.module.tools.ScreenTools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by KingZ on 2015/11/3.
 * Discription:图片/文件下载测试
 * //TODO 需要重构
 */
public class DownloadAPPActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "DownloadAPPActivity";
    public static final int PAGE_ID = R.layout.bitmap_demo_layout;
    public static final int TIMEOUT_MILLIS = 10 * 1000;         //网络超时时间阀值
    private Button btn_NetPic;
    private Button btn_LocalPic;
    private Button btn_downLoad;
    private ImageView imgNetPic, imgResPic;
    private FileDownloader _downloader;
    private Context context;
    /** APP下载路径 */
//    private String appUrlPath = "http://192.168.90.115/pub_apk/update-test/V4.6.0_XJCBC_IPTV_QUANZHI_Beta_MG_22773_181.apk";
    private String appUrlPath = "http://www.apk3.com/uploads/soft/guiguangbao/com.xdsdfdvcswa20160630.apk";
    private HttpURLConnection urlConnection;
    private InputStream downloadIs = null;
    private OutputStream downloadOs = null;
    private File downloadFile;
    private HorizontalProgressBarView mProgressBarView;
    private float percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = App.getAppInstance().getAppContext();
        setContentView(PAGE_ID);
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

        String configPathPrx = GlobalCacheCenter.getInstance().getAppConfigPath();
        String configPath = configPathPrx + File.separator + "kingz" + File.separator + "mytest.dat";
        ZLog.d(TAG,"获取到的configPath:" + configPath);
        File file = new File(configPath);
        if(!file.exists() && !file.isDirectory()){
            //不存在就创建多级目录及文件
            file.mkdirs();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadpic_btn:

                break;
            case R.id.btn_down_app:
                ToastTools.getInstance().showMgtvWaringToast(context, "开始下载....");
                initProgressView();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startDownLoadFile(appUrlPath);
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 进度条初始化
     */
    private void initProgressView() {
        mProgressBarView = new HorizontalProgressBarView(context);
        mProgressBarView.setTotalWidth(ScreenTools.SCREEN_WIDTH / 2);
        mProgressBarView.setInnerPaintColor(0xFF9dbaf2);
        mProgressBarView.setOuterPaintColor(0x19ffffff);
        mProgressBarView.setProgressCompleteListener(new HorizontalProgressBarView.ProgressCompleteListener() {
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
        if (!NetTools.isConnect()) {
            Log.e(TAG, "网络未连接,无法进一步下载");
            ToastTools.getInstance().showMgtvWaringToast(context, "url is empty!");
            return;
        }
//        Java方式连接网络
//        connectByJava(url);

        downloadFile = createFileDir(); //确定文件保存路径
        _downloader = new FileDownloader();
        _downloader.start(url, downloadFile, true, new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.i(TAG, "msg.msg = " + msg.what +"; obj = " + msg.obj);
                switch (msg.what){
                    case 1:

                        break;
                    case 2:
                        break;
                    case 3:
                        percent = (float) msg.obj;
                        if(percent >= 0.9){
                            percent = (float) 1.0;
                        }
                        mProgressBarView.setWidth((int) (percent * 600));
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    default:
                        break;
                }
                return false;
            }
        }));
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
