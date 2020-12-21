package com.starcor.mobile.libhlscache;

import com.kingz.mobile.libhlscache.HLSCache;
import com.kingz.mobile.libhlscache.HLSModel;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * Created  2017/11/10.
 */
public class HLSModelTest {
    /**
     * private boolean isM3u8TsTimesSame(BufferedReader lhs, BufferedReader rhs) throws IOException
     */
    @Test
    public void isM3u8TsTimesSame() throws Exception {
        HLSCache hlsManager = new HLSCache("");
        HLSModel hLSModel = hlsManager.getHLSModel();
        File raw = Tools.getResourceFile(getClass().getClassLoader(), "raw.m3u8");
        File local = Tools.getResourceFile(getClass().getClassLoader(), "local.m3u8");
        File diffTsDurationLocal = Tools.getResourceFile(getClass().getClassLoader(), "local_diff_ts_duration.m3u8");
        File diffFileLineLenLocal = Tools.getResourceFile(getClass().getClassLoader(), "local_diff_file_line_len.m3u8");

        BufferedReader rawBr = new BufferedReader(new FileReader(raw));
        BufferedReader localBr = new BufferedReader(new FileReader(local));
        BufferedReader diffTsDurationBr = new BufferedReader(new FileReader(diffTsDurationLocal));
        BufferedReader diffFileLineLineBr = new BufferedReader(new FileReader(diffFileLineLenLocal));

        assertEquals(callIsM3u8TsTimesSame(hLSModel, rawBr, localBr), true);
        assertEquals(callIsM3u8TsTimesSame(hLSModel, rawBr, diffTsDurationBr), false);
        assertEquals(callIsM3u8TsTimesSame(hLSModel, rawBr, diffFileLineLineBr), false);
    }

    private boolean callIsM3u8TsTimesSame(HLSModel hlsModel, BufferedReader lhs, BufferedReader rhs) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = HLSModel.class.getDeclaredMethod("isM3u8TsTimesSame", BufferedReader.class, BufferedReader.class);
        method.setAccessible(true);
        return (boolean) method.invoke(hlsModel, lhs, rhs);
    }

}