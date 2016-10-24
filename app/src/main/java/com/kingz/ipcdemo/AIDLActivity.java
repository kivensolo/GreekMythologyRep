package com.kingz.ipcdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.kingz.customdemo.R;
import com.utils.ZLog;

import java.util.List;

/**
 * author: King.Z <br>
 * date:  2016/10/17 17:49 <br>
 * description: AIDL的客户端 <br>
 */
public class AIDLActivity extends Activity {
    public static final String TAG = AIDLActivity.class.getSimpleName();

    //由AIDL文件生成的Java类
    private IBookManager mBookManager = null;
    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;
    //包含Book对象的list
    private List<Book> mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
    }
    /**
     * 添加书籍
     * 按钮的点击事件，点击之后调用服务端的addBookIn方法
     * @param view
     */
    public void addBook(View view) {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBookManager == null) {
            return;
        }

        Book book = new Book();
        book.setName("APP研发录In");
        book.setPrice(30);
        try {
            mBookManager.addBook(book);
            ZLog.d(TAG, "addBook() "+mBooks.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getBooks(View view) {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBookManager == null) {
            return;
        }
        try {
            mBooks = mBookManager.getBooks();
            ZLog.d(TAG, "getBooks() "+mBooks.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试与服务端建立连接
     */
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
            mBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ZLog.d(getLocalClassName(), "service connected");
            //获取服务器对象
            mBookManager = IBookManager.Stub.asInterface(service);
            mBound = true;
            Toast.makeText(AIDLActivity.this, "服务器已连接！", Toast.LENGTH_SHORT).show();
            if (mBookManager != null) {
                try {
                    mBooks = mBookManager.getBooks();
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
