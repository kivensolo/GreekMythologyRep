package com.kingz.tools;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import com.App;
import com.zeke.kangaroo.utils.EncryptTools;
import com.zeke.kangaroo.utils.ZLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 下载工具类，提供静态方法downFile
 */
public class DownloadTools {
    private static final String TAG = "DownloadTools";

    public static final int FLAG_Start = 0;
    public static final int FLAG_DL = 1;
    public static final int FLAG_FINISH = 2;
    public static final int FLAG_ERROR = 3;
    public static final int FLAG_CAPACITY_ERROR = 4;
    /**
     * 下载文件的方法，文件下载后保存在/sdcard/DownFile目录下
     *
     * @param urlString 需要下载的url
     * @param handler 下载过程中通过handler更新状态，
     *                msg.arg1 文件总长度
     *                msg.arg2 文件已下载长度
     *                msg.what:  FLAG_Start 开始下载
     *                           FLAG_DL 下载进度更新，每下载4K更新一次
     *                           FLAG_FINISH  下载完成
     *                           FLAG_ERROR  下载出现错误
     *                           FLAG_CAPACITY_ERROR  外部储存不足
     */
    public static void downFile(String urlString, Handler handler) {
        InputStream inputStream;
        OutputStream outputStream;
        URLConnection connection;

//        if (!NetTools.isConnect()) {
//            sendNetMessage(handler,FLAG_ERROR);
//            ZLog.i(TAG, "网络未连接");
//            return;
//        }
        try {
            URL url = new URL(urlString);
            connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            if (connection.getReadTimeout() == 5) {
                ZLog.e("TAG", "当前网络有问题");
                sendNetMessage(handler,FLAG_ERROR);
                return;
            }
            inputStream = connection.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
            sendNetMessage(handler,FLAG_ERROR);
            return;
        }

        File directory = Environment.getExternalStorageDirectory();
        String savePAth = directory.getAbsolutePath() + "/DownFile";
        StatFs sf = new  StatFs(directory.getAbsolutePath());
        int  blockSize = sf.getBlockSize();
        int  blockCount = sf.getBlockCount();
        int  availCount = sf.getAvailableBlocks();

        String msg = String.format("\n block大小: %d bytes  block数目: %d个  磁盘总容量：%s \n 当前磁盘剩余block:%d个,剩余容量：%s"
                , blockSize, blockCount, Formatter.formatFileSize(App.getAppInstance().getAppContext(), blockCount * blockSize)
                , availCount, Formatter.formatFileSize(App.getAppInstance().getAppContext(), availCount * blockSize));
        Log.d(TAG ,msg);

        int downedFileLength = 0;
        File file1 = new File(savePAth);
        if (!file1.exists()) {
            file1.mkdir();
        }
        //清空文件夹
        File[] files = file1.listFiles();
        for(File item:files){
            item.delete();
        }

        String apkName = EncryptTools.MD5(urlString) + ".apk";
        String savePathString = savePAth + File.separator + apkName;
        File file = new File(savePathString);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            outputStream = new FileOutputStream(file);
            int fileLength = connection.getContentLength();
            if (fileLength == -1) {
                sendNetMessage(handler, FLAG_ERROR);
                return;
            }
            ZLog.d(TAG,"Current file size is " + fileLength + " bytes");
            int limitCapacity = 20 * 1024 * 1024; //20M的缓冲容量
            if(availCount * blockSize - limitCapacity <= fileLength){
                ZLog.w(TAG,"The local storage capacity is less than the file，giving up downloading.");
                sendNetMessage(handler, FLAG_CAPACITY_ERROR);
                return;
            }else{
                ZLog.d(TAG,"The local storage capacity is large enough to allow download.");
            }

            sendNetMessage(handler, FLAG_Start, fileLength, 0, null);
            while (downedFileLength < fileLength) {
                if (inputStream == null) {
                    ZLog.e(TAG, "apk下载地址数据有误");
                    sendNetMessage(handler, FLAG_ERROR);
                    return;
                }
                byte[] buffer = new byte[1024 * 4];
                int data = inputStream.read(buffer);
                downedFileLength += data;
                outputStream.write(buffer, 0, data);
                sendNetMessage(handler, FLAG_DL, fileLength, downedFileLength, null);
            }
            sendNetMessage(handler, FLAG_FINISH, fileLength, downedFileLength, savePathString);
        } catch (IOException e) {
            e.printStackTrace();
            sendNetMessage(handler, FLAG_ERROR);
        }
    }

    private static void sendNetMessage(Handler handler, int what) {
        Message message = new Message();
        message.what = what;
        handler.sendMessage(message);
    }

    private static void sendNetMessage(Handler handler, int what, int arg1, int arg2, Object obj) {
        Message message = new Message();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        handler.sendMessage(message);
    }
}
