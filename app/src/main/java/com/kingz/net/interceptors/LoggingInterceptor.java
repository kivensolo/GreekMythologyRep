package com.kingz.net.interceptors;

import com.zeke.kangaroo.utils.ZLog;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * date：2019/9/17
 * description：http loging 拦截器
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        ZLog.d("INFO",String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));
        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        ZLog.d("INFO",String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}
