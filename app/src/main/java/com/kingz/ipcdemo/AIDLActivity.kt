package com.kingz.ipcdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import com.kingz.customdemo.R
import com.zeke.kangaroo.utils.ZLog
import com.zeke.ktx.base.BaseActivity

/**
 * author: King.Z <br></br>
 * date:  2016/10/17 17:49 <br></br>
 * description: AIDL测试页面 <br></br>
 */
class AIDLActivity : BaseActivity() {
    //具体实现的IInterface对象（代理）
    private var mIBookManager: IBookManager? = null
    private var isBinded = false
    private var mBooks: List<Book>? = null

    companion object {
        val TAG: String = AIDLActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidl)
    }

    override fun onStart() {
        super.onStart()
        if (!isBinded) {
            attemptToBindService()
        }
    }

    private fun attemptToBindService() {
        val intent = Intent(this, BookShopService::class.java)
        // 跨进程调用通常使用如下方式
        // intent.setAction("com.kingz.aidl");
        // intent.setPackage("com.kingz.ipcdemo");
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }


    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            ZLog.d(localClassName, "service connected")
            mIBookManager = IBookManager.Stub.asInterface(service)
            service.linkToDeath({ ZLog.e("Binder is death") }, 0)
            isBinded = true
            Toast.makeText(this@AIDLActivity, "服务器已连接！", Toast.LENGTH_SHORT).show()
            mBooks = mIBookManager?.books
            ZLog.d(localClassName, mBooks?.toString())
        }

        override fun onServiceDisconnected(name: ComponentName) {
            ZLog.e(localClassName, "service disconnected")
            isBinded = false
        }
    }

    fun addBook(view: View?) {
        if (!isBinded) {
            attemptToBindService()
            Toast.makeText(this, "绑定中服务.....请稍后再试", Toast.LENGTH_SHORT).show()
            return
        }
        val book = Book()
        book.name = "APP研发录In"
        book.price = 30
        mIBookManager?.addBook(book)
        ZLog.d(TAG, "addBook() ${mBooks.toString()}")
    }

    fun deleteBook(view: View?) {
        val book = Book()
        book.name = "APP研发录In"
        book.price = 30
        // 此处的ICallback 用代理对象Stub
        mIBookManager?.deleteBook(book, object : ICallback.Stub() {
            override fun onSuccess(result: String) {
                ZLog.d(TAG, "deleteBook() sucess. result=$result")
            }

            override fun onError(errorCode: Int, reason: String) {
                ZLog.e(TAG, "deleteBook() error. reason=$reason")
            }
        })
    }

    fun getBooks(view: View?) {
        if (!isBinded) {
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT)
                    .show()
            attemptToBindService()
            return
        }
        mBooks = mIBookManager?.books
        ZLog.d(TAG, "getBooks() from service. ${mBooks?.toString()}")
    }

    override fun onStop() {
        super.onStop()
        if (isBinded) {
            unbindService(connection)
            isBinded = !isBinded
        }
    }
}