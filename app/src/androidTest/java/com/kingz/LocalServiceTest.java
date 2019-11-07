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
