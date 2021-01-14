package com.kingz.functiontest;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.zeke.home.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StartPageInstrumentedTest {
    @Rule
    private Context mContext;

    @Before
    public void setMainActivity() {
        mContext = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void testOpenMain() {
        Intent intent = new Intent(mContext,MainActivity.class);
        mContext.startActivity(intent);
    }
}
