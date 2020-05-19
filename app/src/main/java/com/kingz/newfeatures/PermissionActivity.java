package com.kingz.newfeatures;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.kingz.customdemo.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 权限申请Demo页面
 */
@TargetApi(Build.VERSION_CODES.O)
public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "PermissionActivity";
    private boolean flag;
    private ImageView mIvPhoto;
    private String mCurrentPhotoPath;
    private static final int REQUEST_CODE_TAKE_PHOTO = 0x110;
    public static final String AUTHORITY = "com.kingz.newfeatures.fileprovider";
    private String[] allpermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_TAKE_PHOTO) {
            mIvPhoto.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        }
    }

    private void init() {
        findViewById(R.id.btn_request_permission).setOnClickListener(this);
        findViewById(R.id.btn_request_muilt_permission).setOnClickListener(this);
        mIvPhoto = findViewById(R.id.iv_pic);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult: requestCode: " + requestCode);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(PermissionActivity.this, "已授权", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PermissionActivity.this, "拒绝授权", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request_permission:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.CAMERA)) {
                        //已有权限，可以操作
                        testPhoto1();
                        testPhoto2();
                    } else {
                        if (flag && !ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this, Manifest.permission.CAMERA)) {
                            Log.i("lzz", "onClick:hhhhh ");
                            Toast.makeText(PermissionActivity.this, "用户已经拒绝过了且不让显示", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.CAMERA}, 2);
                        flag = true;
                    }
                }
                break;
            case R.id.btn_request_muilt_permission:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean needapply = false;
                    for (String allpermission : allpermissions) {
                        int chechpermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                                allpermission);
                        if (chechpermission != PackageManager.PERMISSION_GRANTED) {
                            needapply = true;
                        }
                    }
                    if (needapply) {
                        ActivityCompat.requestPermissions(PermissionActivity.this, allpermissions, 1);
                    } else {
                        Toast.makeText(PermissionActivity.this, "已授权所有权限", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void testPhoto1() {
        String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                .format(new Date()) + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        mCurrentPhotoPath = file.getAbsolutePath();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        PermissionActivity.this.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    private void testPhoto2() {
        String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                .format(new Date()) + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), filename);
//        mCurrentPhotoPath = file.getAbsolutePath();
        Uri fileUri = FileProvider.getUriForFile(this, AUTHORITY, file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        PermissionActivity.this.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }
}
