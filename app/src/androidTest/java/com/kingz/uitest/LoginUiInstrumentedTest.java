package com.kingz.uitest;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.zeke.kangaroo.utils.NetUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * Espresso 测试
 */
@RunWith(AndroidJUnit4.class)
public class LoginUiInstrumentedTest {
//    @Rule
//    public ActivityTestRule<ShakeAnimation> mShakePageRule = new ActivityTestRule<>(ShakeAnimation.class);
    private Context mContext;

    @Before
    public void setMainActivity() {
        mContext = ApplicationProvider.getApplicationContext();
    }

    /**
     * 顺序测试
     * @throws Exception
     */
    @Test
    public void test_sque(){
        testPackageName();
        net_isConnect();
    }


    @Test
    public void testPackageName() {
        assertEquals("com.kingz.customdemo", mContext.getPackageName());
    }


    @Test
    public void net_isConnect() {
        assertEquals(true, NetUtils.isConnect(mContext));
    }

}
