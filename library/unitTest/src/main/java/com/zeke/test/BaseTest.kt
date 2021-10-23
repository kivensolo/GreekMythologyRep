package com.zeke.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.zeke.kangaroo.zlog.ZLog
import org.junit.Before

/**
 * author：ZekeWang
 * date：2021/1/15
 * description： Adnroid UnitTest Base layer.
 */
abstract class BaseTest {
    companion object {
        private val TAG = BaseTest::class.java.simpleName
    }

    var mContext: Context? = null

    @Before
    open fun setUp(){
        ZLog.d(TAG, "@Befor setUp()")
        //获取application的context
        mContext = ApplicationProvider.getApplicationContext()
    }

    open fun getAppContext():Context?{
        return mContext
    }
}