package com.kingz.module.common.user

import com.kingz.database.DatabaseApplication
import com.kingz.database.entity.UserEntity

/**
 * @author zeke.wang
 * @date 2020/8/26
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc: 用户信息相关
 */
object UserInfo {
    //获取本地缓存的用户数据
    suspend fun getUserInfor(): UserEntity? {
        return DatabaseApplication.getInstance().getUserDao().getUserInfo()
    }

    //清除用户数据
    suspend fun clearLocalUserInfo(){
        //清room用户表缓存
        val userDao = DatabaseApplication.getInstance().getUserDao()
        userDao.userLogout()

        val cookieDao = DatabaseApplication.getInstance().getCookiesDao()
        cookieDao.clear()

    }
}