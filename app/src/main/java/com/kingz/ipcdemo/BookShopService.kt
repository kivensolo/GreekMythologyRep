package com.kingz.ipcdemo

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.zeke.kangaroo.utils.ZLog
import java.util.*

/**
 * @author zeke.wang
 * @date 2020/3/20
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
class BookShopService : Service() {
    val TAG = BookShopService::class.java.simpleName
    private val mBooks: MutableList<Book> = ArrayList()
    private var mICallback: ICallback? = null
    override fun onCreate() {
        super.onCreate()
        initBookShop()
    }

    private fun initBookShop() {
        val book = Book()
        book.name = "Android开发艺术探索"
        book.price = 28
        mBooks.add(book)
    }

    //由AIDL文件生成的BookManager
    private val mServiceStub = object : IBookManager.Stub() {
        override fun deleteBook(book: Book?, callback: ICallback?) {
            mICallback = callback
            notifySucess("delete sucess.")
        }

        override fun addBookInout(book: Book?) {}

        override fun setBookName(book: Book?, name: String?) {
        }

        override fun addBookOut(book: Book?) {}

        override fun getBookCount(): Int {
            return books.size
        }

        override fun setBookPrice(book: Book?, price: Int) {}

        override fun addBookIn(book: Book?) {}

        override fun addBook(book: Book?) {
            synchronized (this) {
                book?:Book()
                //尝试修改book的参数
                //观察其到客户端的反馈
                book?.price = 2333

                if (!mBooks.contains(book)) {
                    if (book != null) {
                        mBooks.add(book)
                    }
                }
                //打印mBooks列表，观察客户端传过来的值
                ZLog.e(TAG, "invoking addBooks() method , now the list is : $mBooks")
            }
        }

        override fun getBooks(): MutableList<Book> {
            synchronized(this) {
                ZLog.d(TAG, "invoking getBooks() method , now the list is : $mBooks")
                return mBooks
            }
        }
    }

    private fun notifySucess(msg: String) {
        mICallback?.onSuccess(msg)
    }

    /**
     * 客户端调用 bindService(serviceIntent, conn, BIND_AUTO_CREATE)
     * bind成功后触发回调.
     * @param intent 启动Service的intent
     * @return
     * Null : oBind返回值是null的情况下, 生命周期为：
     * onCreate() --> onBind()
     * Local Binder Object(本地Binder对象)， 生命周期为：
     * onCreate() ----> onBind() ----> onServiceConnected()
     *
     * 客户端调用多次bindService, 以上生命周期都不会被多次调用。
     */
    override fun onBind(intent: Intent): IBinder? {
        ZLog.d(TAG, "on bind,intent = $intent")
        return mServiceStub
    }

    override fun unbindService(conn: ServiceConnection?) {
        super.unbindService(conn)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
