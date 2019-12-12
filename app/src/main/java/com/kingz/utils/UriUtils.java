package com.kingz.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * author：KingZ
 * date：2019/12/12
 * description： Uri tools.
 */
public class UriUtils {
    private UriUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    /**
     * 检查url地址是否是有效的
     *
     * @return true 有效
     * @throws URISyntaxException URI语法异常
     * @param uri
     */
    public static boolean isValid(URI uri){
        return !(uri.getHost() == null || uri.getPath() == null);
    }
}
