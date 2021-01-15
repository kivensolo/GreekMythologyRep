package com.kingz.functiontest;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.zeke.home.MainActivity;
import com.zeke.test.BaseInstrumentedTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class StartPageInstrumentedTest extends BaseInstrumentedTest {

    @Before
    @Override
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testOpenMain() {
        testStartActivity(MainActivity.class, intent -> {
            intent.putExtra("params","123456");
            return null;
        });
    }

    @Test
    public void testPackageName() {
        assertEquals("com.kingz.customdemo", getPackageName());
    }
}
