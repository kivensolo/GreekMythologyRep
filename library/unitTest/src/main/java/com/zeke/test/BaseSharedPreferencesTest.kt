package com.zeke.test

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.runner.RunWith

/**
 * author：ZekeWang
 * date：2021/1/15
 * description：SharedPreferences 测试基类
 */
@RunWith(AndroidJUnit4::class)
abstract class BaseSharedPreferencesTest : BaseTest() {
    lateinit var mSharePreferences: SharedPreferences
    lateinit var mSharePreferencesEditor: SharedPreferences.Editor

    @Before
    @SuppressLint("CommitPrefEdits")
    override fun setUp() {
        super.setUp()
        //实例化SharedPreferences
        mSharePreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        mSharePreferencesEditor = mSharePreferences.edit()
    }

    /**
     * 测试保存数据是否成功
     */
    open fun testSaveData(exec: (editoe: SharedPreferences.Editor) -> Unit) {
        exec(mSharePreferencesEditor)
    }

    /**
     * 测试获取数据
     */
    open fun testReadData(exec: (editoe: SharedPreferences) -> Unit) {
        exec(mSharePreferences)
    }
}