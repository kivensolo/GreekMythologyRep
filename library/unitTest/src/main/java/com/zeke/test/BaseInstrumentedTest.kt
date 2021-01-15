package com.zeke.test

import android.app.Activity
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * author：ZekeWang
 * date：2021/1/15
 * description：仪器测试基类
 */
@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentedTest : BaseTest() {

    @Before
    override fun setUp() {
        super.setUp()
    }

    /**
     * Start activity by intent.
     */
    @Test
    fun testStartActivity(clazz: Class<out Activity>,
                          exec: (intent: Intent) -> Unit) {
        val intent =Intent(mContext, clazz::class.java)
        exec(intent)
        mContext?.startActivity(intent)
        assertNotNull(mContext)
    }

    /**
     * Get app's package name.
     */
    open fun getPackageName():String?{
        return mContext?.packageName
    }
}