package com.kingz.mobile.libhlscache.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created 2017/11/8.
 */
public class LogUtils {
    private static Logger sLogger = Logger.getLogger("libhlscache");
    private static boolean sEnable = true;

    public static void w(String s) {
        if (!sEnable) {
            return;
        }
        sLogger.log(Level.WARNING, s);
    }

    public static void i(String s) {
        if (!sEnable) {
            return;
        }
        sLogger.log(Level.INFO, s);
    }
}
