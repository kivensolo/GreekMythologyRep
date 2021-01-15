package com.zeke.test

import android.content.Intent
import android.os.IBinder
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * author：ZekeWang
 * date：2021/1/15
 * description：Test of Android Service.
 */
@RunWith(AndroidJUnit4::class)
abstract class BaseServiceTest : BaseTest() {
    @Rule
    val mServiceRule: ServiceTestRule = ServiceTestRule()

    @Before
    open fun setup() {

    }

    open fun testWithBindService(intent:Intent) : IBinder {
        return mServiceRule.bindService(intent)
    }


    @After
    @Ignore("not implemented yet")
    open fun testDone() {}
}