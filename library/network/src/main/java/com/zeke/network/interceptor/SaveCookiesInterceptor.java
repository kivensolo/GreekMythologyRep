package com.zeke.network.interceptor;

import com.zeke.network.cookie.ICookiesHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 获取cookie拦截器，用于首次请求
 */
public class SaveCookiesInterceptor implements Interceptor {

    private ICookiesHandler mCookiesHandler;

    /**
     * 外部传递的CookieHandler
     *
     * @param cookiesHandler Cookie处理器
     */
    public SaveCookiesInterceptor(ICookiesHandler cookiesHandler) {
       mCookiesHandler = cookiesHandler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);
        /*
         * 服务器想要客户端保存Cookie到本地,在响应头中，就会添加了Set-Cookie字段
         * 该字段可能有多个
         */
        if (!response.headers(Const.Http.SET_COOKIE).isEmpty()) {
            List<String> cookies = response.headers(Const.Http.SET_COOKIE);
            String cookie = encodeCookie(cookies);
            if(mCookiesHandler != null){
                mCookiesHandler.setCookies(request.url(),cookie);
            }
        }
        return response;
    }

    //整合cookie为唯一字符串
    private String encodeCookie(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        Set<String> set = new HashSet<>();
        for (String cookie : cookies) {
            String[] arr = cookie.split(";");
            for (String s : arr) {
                if (set.contains(s)) { continue; }
                set.add(s);
            }
        }

        for (String cookie : set) {
            sb.append(cookie).append(";");
        }

        int last = sb.lastIndexOf(";");
        if (sb.length() - 1 == last) {
            sb.deleteCharAt(last);
        }

        return sb.toString();
    }
}