package com.kingz.mobile.libhlscache.http;

import java.io.IOException;

/**
 * Created 2017/11/8.
 */
public class HttpResponseNot200Exception extends IOException {

    public HttpResponseNot200Exception(String s) {
        super(s);
    }

    public HttpResponseNot200Exception(String s, Throwable throwable) {
        super(s, throwable);
    }
}
