package com.kingz.ipcdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import com.kingz.utils.ZLog;

import java.util.ArrayList;
import java.util.List;

/**
 * author: King.Z <br>
 * date:  2016/10/17 17:07 <br>
 * description: 服务端 <br>
 */
public class AIDLService extends Service {
    public static final String TAG = AIDLService.class.getSimpleName();
    private List<Book> mBooks = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("Android开发艺术探索");
        book.setPrice(28);
        mBooks.add(book);
    }

    //由AIDL文件生成的BookManager
    private final IBookManager.Stub mBookManager = new IBookManager.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getBookCount() throws RemoteException {
            return getBooks().size();
        }

        @Override
        public void setBookPrice(Book book, int price) throws RemoteException {

        }

        @Override
        public void setBookName(Book book, String name) throws RemoteException {

        }

        @Override
        public void addBookIn(Book book) throws RemoteException {

        }

        @Override
        public void addBookOut(Book book) throws RemoteException {

        }

        @Override
        public void addBookInout(Book book) throws RemoteException {

        }

        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                ZLog.d(TAG, "invoking getBooks() method , now the list is : " + mBooks.toString());
                if (mBooks != null) {
                    return mBooks;
                }
            }
            return new ArrayList<>();
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                if (book == null) {
                    Log.e(TAG, "Book is null in In");
                    book = new Book();
                }
                //尝试修改book的参数，主要是为了观察其到客户端的反馈
                book.setPrice(2333);
                if (!mBooks.contains(book)) {
                    mBooks.add(book);
                }
                //打印mBooks列表，观察客户端传过来的值
                ZLog.e(TAG, "invoking addBooks() method , now the list is : " + mBooks.toString());
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        ZLog.e(TAG, String.format("on bind,intent = %s", intent.toString()));
        return mBookManager;
    }
}
