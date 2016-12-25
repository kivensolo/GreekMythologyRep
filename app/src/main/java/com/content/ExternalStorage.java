package com.content;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.utils.ToastTools;
import com.utils.ZLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * description: Demonstration of styled text resources. <br>
 */
public class ExternalStorage extends Activity {

    public static final String PUBLIC_PICTURE_NAME = "DemoPicture.jpg";

    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWriteable = false;
    ViewGroup mLayout;

    static class Item {
        View mRoot;
        Button mCreate;
        Button mDelete;
    }

    Item mExternalStoragePublicPicture;
    Item mExternalStoragePrivatePicture;
    Item mExternalStoragePrivateFile;

    Item createStorageControls(CharSequence label, File path,
                               View.OnClickListener createClick, View.OnClickListener deleteClick) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        Item item = new Item();
        item.mRoot = inflater.inflate(R.layout.external_storage_item, null);
        TextView tv = (TextView) item.mRoot.findViewById(R.id.label);
        tv.setText(label);
        if (path != null) {
            tv = (TextView) item.mRoot.findViewById(R.id.path);
            tv.setText(path.toString());
        }
        item.mCreate = (Button) item.mRoot.findViewById(R.id.create);
        item.mCreate.setOnClickListener(createClick);
        item.mDelete = (Button) item.mRoot.findViewById(R.id.delete);
        item.mDelete.setOnClickListener(deleteClick);
        return item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.external_storage);
        mLayout = (ViewGroup) findViewById(R.id.layout);
        initPublicPictureBtn();
        initPrivatePictureBtn();
        initPrivateFileBtn();
        startWatchingExternalStorage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopWatchingExternalStorage();
    }

    BroadcastReceiver mExternalStorageReceiver;

    void startWatchingExternalStorage() {
        mExternalStorageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("test", "Storage: " + intent.getData());
                updateExternalStorageState();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        registerReceiver(mExternalStorageReceiver, filter);
        updateExternalStorageState();
    }

    void stopWatchingExternalStorage() {
        unregisterReceiver(mExternalStorageReceiver);
    }


    private void initPublicPictureBtn() {
        mExternalStoragePublicPicture = createStorageControls("Picture: getExternalStoragePublicDirectory",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createExternalStoragePublicPicture();
                        updateExternalStorageState();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteExternalStoragePublicPicture();
                        updateExternalStorageState();
                    }
                });
        mLayout.addView(mExternalStoragePublicPicture.mRoot);
    }

    private void initPrivatePictureBtn() {
        mExternalStoragePrivatePicture = createStorageControls("Picture getExternalFilesDir",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                new View.OnClickListener() {
                    public void onClick(View v) {
                        createExternalStoragePrivatePicture();
                        updateExternalStorageState();
                    }
                },
                new View.OnClickListener() {
                    public void onClick(View v) {
                        deleteExternalStoragePrivatePicture();
                        updateExternalStorageState();
                    }
                });
        mLayout.addView(mExternalStoragePrivatePicture.mRoot);
    }

    private void initPrivateFileBtn() {
        mExternalStoragePrivateFile = createStorageControls("File getExternalFilesDir",
                getExternalFilesDir(null),
                new View.OnClickListener() {
                    public void onClick(View v) {
                        createExternalStoragePrivateFile();
                        updateExternalStorageState();
                    }
                },
                new View.OnClickListener() {
                    public void onClick(View v) {
                        deleteExternalStoragePrivateFile();
                        updateExternalStorageState();
                    }
                });
        mLayout.addView(mExternalStoragePrivateFile.mRoot);
    }

    /**
     * 更新外部储存状态
     */
    void updateExternalStorageState() {
        //获取主“外部”存储设备的当前状态。
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        handleExternalStorageState(mExternalStorageAvailable, mExternalStorageWriteable);
    }

    /**
     * 通过外部储存状态改变按钮状态
     *
     * @param available
     * @param writeable
     */
    void handleExternalStorageState(boolean available, boolean writeable) {
        boolean has = hasExternalStoragePublicPicture();
        mExternalStoragePublicPicture.mCreate.setEnabled(writeable && !has);
        mExternalStoragePublicPicture.mDelete.setEnabled(writeable && has);
        has = hasExternalStoragePrivatePicture();
        mExternalStoragePrivatePicture.mCreate.setEnabled(writeable && !has);
        mExternalStoragePrivatePicture.mDelete.setEnabled(writeable && has);
        has = hasExternalStoragePrivateFile();
        mExternalStoragePrivateFile.mCreate.setEnabled(writeable && !has);
        mExternalStoragePrivateFile.mDelete.setEnabled(writeable && has);
    }

    //---------------判断图片/文件是否存在  start---------------//
    boolean hasExternalStoragePublicPicture() {
        File publicPicFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(publicPicFile, PUBLIC_PICTURE_NAME);
        return file.exists();
    }

    boolean hasExternalStoragePrivatePicture() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), PUBLIC_PICTURE_NAME);
        return file.exists();
    }

    boolean hasExternalStoragePrivateFile() {
        File file = new File(getExternalFilesDir(null), PUBLIC_PICTURE_NAME);
        return file.exists();
    }
    //---------------判断图片/文件是否存在  End---------------//


    //---------------创建图片/文件  start---------------//
    void createExternalStoragePublicPicture() {
        //Environment.getExternalStoragePublicDirectory：提供一个媒资分享目录给所有应用使用
        File publicPicFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File file = new File(publicPicFile, PUBLIC_PICTURE_NAME);
        try {
            //创建此抽象路径名指定的目录，包括所有必需但不存在的父目录,注意，此操作失败时也可能已经成功地创建了一部分必需的父目录。
            publicPicFile.mkdirs();
            InputStream ins = getResources().openRawResource(R.raw.bg4);
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            //------将数据放入文件中-------
            OutputStream os = new FileOutputStream(file);
            os.write(buffer);
            ins.close();
            os.close();

            //告诉新文件的媒体扫描器，以便它立即提供给用户使用。
            mediaScanFile(file);
        } catch (IOException e) {
            //无法创建文件，很可能是因为外部存储目前还没有挂载
            e.printStackTrace();
            ZLog.w("ExternalStorage", "Error writing " + file, e);
        }
    }

    void createExternalStoragePrivatePicture() {
        //getExternalFilesDir：提供一个媒资分享目录仅供本应用使用，当应用被卸载的时候,此目录会被删除
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File file = new File(path, PUBLIC_PICTURE_NAME);
        try {
            InputStream ins = getResources().openRawResource(R.raw.bg1);
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[ins.available()];
            ins.read(data);
            os.write(data);
            ins.close();
            os.close();

            mediaScanFile(file);
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }

    void createExternalStoragePrivateFile() {
        //为空就保存在文件目录的根目录
        File file = new File(getExternalFilesDir(null), PUBLIC_PICTURE_NAME);
        try {
            InputStream ins = getResources().openRawResource(R.raw.bg3);
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[ins.available()];
            ins.read(data);
            os.write(data);
            ins.close();
            os.close();

            mediaScanFile(file);
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }

    //---------------创建图片/文件  End---------------//


    //---------------删除图片/文件  start---------------//
    void deleteExternalStoragePublicPicture() {
        File publicPicFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(publicPicFile, PUBLIC_PICTURE_NAME);
        file.delete();
    }

    void deleteExternalStoragePrivatePicture() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), PUBLIC_PICTURE_NAME);
        file.delete();
    }

    void deleteExternalStoragePrivateFile() {
        File file = new File(getExternalFilesDir(null), PUBLIC_PICTURE_NAME);
        file.delete();
    }
    //---------------删除图片/文件  End---------------//


    private void mediaScanFile(File file) {
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        ZLog.i("ExternalStorage", "Scanned " + path + ":");
                        ZLog.i("ExternalStorage", "-> uri=" + uri);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastTools.getInstance().showMgtvWaringToast(ExternalStorage.this, "onScanCompleted!");
                            }
                        });
                    }
                });
    }
}
