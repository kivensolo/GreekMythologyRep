package com.kingz;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.MainActivity;
import com.kingz.customdemo.R;
import com.kingz.utils.NetTools;
import com.kingz.utils.ZLog;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * JUnit4中常用的几个注解：
 * <code>@BeforeClass</code> 测试类里所有用例运行之前，运行一次这个方法。方法必须是public static void
 * <code>@AfterClass</code> 与BeforeClass对应
 * <code>@After</code> 与Before对应
 * <code>@Test</code> 指定该方法为测试方法，方法必须是public void
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    public MainActivity mMainActivity = null;

    @Before
    public void setMainActivity() {
        ZLog.d("setMainActivity");
        this.mMainActivity = mActivityRule.getActivity();
    }

    @Test
    public void test_sque()throws Exception {
        testPackageName();
        net_isConnect();
    }


    @Test
    public void testPackageName() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.kingz.customdemo", appContext.getPackageName());
    }


    @Test
    public void net_isConnect() throws Exception{
        assertEquals(true,NetTools.isConnect());
    }

}
