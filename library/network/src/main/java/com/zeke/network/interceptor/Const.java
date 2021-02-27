package com.zeke.network.interceptor;

/**
 * author：ZekeWang
 * date：2021/2/27
 * description：全局常量类
 */
public interface Const {
    interface Url {

    }

    interface Local {
        String COOKIE_PREF = "cookies_prefs";
    }

    interface Http {
        long DEFAULT_TIMEOUT = 5000;
        String COOKIE_PARAMS = "Cookie";
        String SET_COOKIE = "Set-cookie";

        String WAN_ANDROID_LOGIN_KEY = "user/login";
        String WAN_ANDROID_REGISTER_KEY = "user/register";
    }


}
