package com.zeke.network.interceptor;

import android.text.TextUtils;

import com.zeke.network.cookie.ICookiesHandler;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加cookie拦截器，用于非首次请求(需要cookies的接口)
 */
public class AddCookiesInterceptor implements Interceptor {
    private ICookiesHandler mCookiesHandler;

    /**
     * 外部传递的CookieHandler
     *
     * @param cookiesHandler Cookie处理器
     */
    public AddCookiesInterceptor(ICookiesHandler cookiesHandler) {
        mCookiesHandler = cookiesHandler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader("Content-type", "application/json; charset=utf-8");
        if(mCookiesHandler != null){
            String cookies = mCookiesHandler.getCookies(request.url());
            if (!TextUtils.isEmpty(cookies)) {
                // 将 Cookie 添加到请求头
                builder.addHeader(Const.Http.COOKIE_PARAMS, cookies);
            }
        }
        return chain.proceed(builder.build());
    }
}
