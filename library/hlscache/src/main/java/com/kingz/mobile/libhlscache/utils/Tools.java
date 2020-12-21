package com.kingz.mobile.libhlscache.utils;

/**
 * Created 2017/11/21.
 */
public class Tools {

    public static String getAbsoluteUrl(String baseUrl, String rawUrl) {
        if (rawUrl.startsWith("http")) {
            return rawUrl;
        } else {
            return baseUrl.substring(0, baseUrl.lastIndexOf('/')) + rawUrl;
        }
    }

}
