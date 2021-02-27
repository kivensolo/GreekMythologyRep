package com.zeke.network.interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加cookie拦截器，用于非首次请求(需要cookies的接口)
 */
public class AddCookiesInterceptor implements Interceptor {

    private Context mContext;

    public AddCookiesInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader("Content-type", "application/json; charset=utf-8");
        String cookie = getCookie(request.url().toString(), request.url().host());
        if (!TextUtils.isEmpty(cookie)) {
            // 将 Cookie 添加到请求头
            builder.addHeader(Const.Http.COOKIE_PARAMS, cookie);
        }
        return chain.proceed(builder.build());
    }

    private String getCookie(String url, String domain) {
        SharedPreferences sp = mContext.getSharedPreferences(Const.Local.COOKIE_PREF,
                Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(domain) &&
                (url.contains("lg/uncollect") //取消收藏站内文章
                || url.contains("article")    // 获取文章列表
                || url.contains("lg/collect") // 收藏站内文章
                || url.contains("lg/todo"))
                || url.contains("coin")) { //积分 API
            return sp.getString(domain, "");
        }
        return "";
    }
}
