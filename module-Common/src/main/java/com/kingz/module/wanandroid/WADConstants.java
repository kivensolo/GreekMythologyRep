package com.kingz.module.wanandroid;

/**
 * author: King.Z <br>
 * date:  2020/8/29 10:21 <br>
 * description: WanAndroid的常量类  <br>
 */
public interface WADConstants {
    String KEY_IS_COLLECT = "key_is_collect";
    String KEY_IS_LOGIN = "key_is_login";
    String KEY_TREE = "key_tree";
    String KEY_USER = "pref_user";
    String KEY_USER_ID = "key_user_id";
    String KEY_USERNAME = "key_username";
    String KEY_PASSWORD = "key_password";
    String KEY_EMAIL = "key_email";
    String KEY_ICON = "key_icon";
    String KEY_TYPE = "key_type";
    String KEY_CHECK_TIME = "key_check_time";

    String EXTRA_SHOW_FRAGMENT_NAME = "show_fragment_name";
    String EXTRA_SHOW_FRAGMENT_ARGUMENTS = "show_fragment_args";
    String EXTRA_SHOW_FRAGMENT_TITLE = "show_fragment_title";

    interface Key{
        String KEY_FRAGEMTN_TYPE = "key_fragmennt_type";

    }

   interface Type {
        int TYPE_TAB_COLLECT = 1;
        int TYPE_TAB_KNOWLEDGE = 2;
        int TYPE_TAB_WEIXIN = 3;
        int TYPE_TAB_PROJECT = 4;
        int TYPE_SETTING = 5;
        int TYPE_COLOR = 6;
    }
}
