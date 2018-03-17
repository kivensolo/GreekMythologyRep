package com.kingz.ipcdemo;

import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;
import com.kingz.customdemo.R;
import com.kingz.utils.ZLog;

import java.io.File;
import java.util.List;

/**
 * author: King.Z <br>
 * date:  2016/10/17 17:49 <br>
 * description: AIDL的客户端 <br>
 */
public class AIDLActivity extends Activity {
    public static final String TAG = AIDLActivity.class.getSimpleName();

    private IBookManager mIBookManager = null;  //具体实现的IInterface对象（代理）
    private boolean mBound = false;
    private List<Book> mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
    }

    public void addBook(View view) {
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "绑定中服务.....请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mIBookManager == null) { return; }
        Book book = new Book();
        book.setName("APP研发录In");
        book.setPrice(30);
        try {
            mIBookManager.addBook(book);
            ZLog.d(TAG, "addBook() "+mBooks.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getBooks(View view) {
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if(mIBookManager != null){
                mBooks = mIBookManager.getBooks();
                ZLog.d(TAG, "getBooks() from service"+mBooks.toString());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void ChangeFile(View view){
        File storageDirectory = Environment.getExternalStorageDirectory(); //storage/emulated/0
        ZLog.d(TAG, "ChangeFile() path = "+storageDirectory.toString());
        //File xmlFile = new File("/data/data/com.starcor.xinjiang.dispatcher/shared_prefs/AppConfig.xml");
        try {
            Context packageContext = getBaseContext().createPackageContext("com.starcor.xinjiang.dispatcher", Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences sh = packageContext.getSharedPreferences("AppConfig", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = sh.edit();
            editor.putString("choseApp","Greek");
            editor.clear();
            ////editor.apply();//write data by QueuedWork
            editor.commit();//write data by application's main thread.
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void attemptToBindService() {
        Intent intent = new Intent(this,AIDLService.class);
//        intent.setAction("com.kingz.aidl");
//        intent.setPackage("com.kingz.ipcdemo");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = !mBound;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ZLog.d(getLocalClassName(), "service connected");
            //获取服务器代理
            mIBookManager = IBookManager.Stub.asInterface(service);
            try {
                service.linkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {

                    }
                },0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBound = true;
            Toast.makeText(AIDLActivity.this, "服务器已连接！", Toast.LENGTH_SHORT).show();
            if (mIBookManager != null) {
                try {
                    mBooks = mIBookManager.getBooks();
                    ZLog.d(getLocalClassName(), mBooks.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ZLog.e(getLocalClassName(), "service disconnected");
            mBound = false;
        }
    };
}
