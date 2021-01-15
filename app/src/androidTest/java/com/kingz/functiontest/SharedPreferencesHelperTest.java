package com.kingz.functiontest;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.zeke.test.BaseSharedPreferencesTest;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * SharedPreferences测试类
 */
@RunWith(AndroidJUnit4.class)
public class SharedPreferencesHelperTest extends BaseSharedPreferencesTest {
    private static final String TEST_NAME = "Test name";
    private static final String TEST_EMAIL = "test@email.com";

    /**
     * 测试保存数据是否成功
     */
    @Test
    public void sharedPreferencesHelper_SavePersonalInformation() {
        testSaveData(editor -> {
            editor.putLong("token", 111L);
            editor.apply();
            return null;
        });
    }

    /**
     * 测试保存数据，然后获取数据是否成功
     */
    @Test
    public void sharedPreferencesHelper_SaveAndReadPersonalInformation() {
    }
}
