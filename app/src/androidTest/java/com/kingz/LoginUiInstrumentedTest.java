package com.kingz;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kingz.view.animation.ShakeAnimation;
import com.zeke.kangaroo.utils.NetUtils;

import org.junit.Before;
import org.junit.Rule;
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
    @Rule
    public ActivityTestRule<ShakeAnimation> mShakePageRule = new ActivityTestRule<>(ShakeAnimation.class);
    private Context mContext;

    @Before
    public void setMainActivity() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    /**
     * 顺序测试
     * @throws Exception
     */
    @Test
    public void test_sque()throws Exception {
        testPackageName();
        net_isConnect();
    }


    @Test
    public void testPackageName() throws Exception {
        assertEquals("com.kingz.customdemo", mContext.getPackageName());
    }


    @Test
    public void net_isConnect() throws Exception{
        assertEquals(true, NetUtils.isConnect(mContext));
    }

}
