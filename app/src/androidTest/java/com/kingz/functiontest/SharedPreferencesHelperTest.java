package com.kingz.functiontest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

/**
 * SharedPreferences测试类
 */
@RunWith(AndroidJUnit4.class)
public class SharedPreferencesHelperTest {
    private static final String TEST_NAME = "Test name";
    private static final String TEST_EMAIL = "test@email.com";

    private static final Calendar TEST_DATE_OF_BIRTH = Calendar.getInstance();

    private SharedPreferences mSharePreferences;

    /** 上下文 */
    private Context mContext;

    @Before
    public void setUp(){
        //获取application的context
        mContext = ApplicationProvider.getApplicationContext();
        //实例化SharedPreferences
        mSharePreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        //mock相关操作

    }

    /**
     * 测试保存数据是否成功
     */
    @Test
    public void sharedPreferencesHelper_SavePersonalInformation(){
    }
    /**
     * 测试保存数据，然后获取数据是否成功
     */
    @Test
    public void sharedPreferencesHelper_SaveAndReadPersonalInformation(){
    }
}
