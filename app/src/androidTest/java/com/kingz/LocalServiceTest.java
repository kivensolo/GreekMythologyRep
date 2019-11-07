package com.kingz;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kingz.four_components.service.LocalService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * author：KingZ
 * date：2019/11/7
 * description：{@link com.kingz.four_components.service.LocalService} 单元测试类
 *
 * JUnit4中常用的几个注解：
 *  <code>@Befor</code>
 *      在每个测试方法之前都会运行一次，只需声明成public
 *  <code>@After</code>
 *      与Before对应
 *
 *  <code>@BeforeClass</code>
 *      测试类里所有用例运行之前，运行一次这个方法(只运行一次)。
 *      方法必须是public static,因为运行的时候测试类还没实例化
 *  <code>@AfterClass</code>
 *      与BeforeClass对应
 *
 *  <code>@Test</code>
 *      指定该方法为测试方法，方法必须是public void
 */
@MediumTest
@RunWith(AndroidJUnit4.class)
public class LocalServiceTest {
    private Intent serviceIntent;
    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Before
    public void setup() throws Exception {
        serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), LocalService.class);
    }

    @Test
    public void testWithBoundService() throws TimeoutException {
        IBinder binder = mServiceRule.bindService(serviceIntent);
        LocalService service =  ((LocalService.LocalBinder)binder).getService();
        assertEquals("LocalService", service.getClass().getSimpleName());
    }

    @Test
    public void testGetNumber() throws TimeoutException {
        IBinder binder = mServiceRule.bindService(serviceIntent);
        LocalService service =  ((LocalService.LocalBinder)binder).getService();
        assertTrue(service.getRandomNumber() <= 100);
    }
}
