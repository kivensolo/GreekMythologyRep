package com.zeke.network.interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 获取cookie拦截器，用于首次请求
 */
public class SaveCookiesInterceptor implements Interceptor {

    private Context mContext;

    public SaveCookiesInterceptor(Context context) {
        mContext = context;
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
            saveCookie(request.url(), cookie);
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

    /**
     * 如果 response 的header 中包含 cookie 信息，则保存cookie到本地
     * 分别为该url和host设置相同的cookie，其中host可选
     * 这样能使得该cookie的应用范围更广
     */
    private void saveCookie(HttpUrl url, String cookies) {
        SharedPreferences sp = mContext.getSharedPreferences(Const.Local.COOKIE_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (isUserLoginAPI(url.toString())) {
            editor.putString(url.toString(), cookies);
            editor.putString(url.host(), cookies);
        }
        editor.apply();
    }

    /**
     * 是否是用户登录或者注册接口
     */
    private boolean isUserLoginAPI(String url){
        if(TextUtils.isEmpty(url)){
            return false;
        }
        return url.contains(Const.Http.WAN_ANDROID_LOGIN_KEY)
                || url.contains(Const.Http.WAN_ANDROID_REGISTER_KEY);
    }
}