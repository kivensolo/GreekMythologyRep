package com.zeke.local.nioserver.utils;

import android.text.TextUtils;

/**
 * @author: King.Z
 * @since: 2020/11/26 21:53 <br>
 * @desc:
 */
public class Utils {
    public static int tryParseInt(String val) {
        return tryParseInt(val, 0);
    }

    public static long tryParseLong(String val) {
        return tryParseLong(val, 0);
    }

    public static int tryParseInt(String val, int defVal) {
        if (TextUtils.isEmpty(val)) {
            return defVal;
        }
        try {
            return Integer.parseInt(val);
        } catch (Exception ignored) {
        }
        return defVal;
    }

    public static long tryParseLong(String val, long defVal) {
        if (TextUtils.isEmpty(val)) {
            return defVal;
        }
        try {
            return Long.parseLong(val);
        } catch (Exception ignored) {
        }
        return defVal;
    }

}
