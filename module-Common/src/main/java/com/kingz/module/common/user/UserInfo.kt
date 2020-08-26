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
    suspend fun getUserInfor(): UserEntity? {
        return DatabaseApplication.getInstance().getUserDao().getUserInfo()
    }
}