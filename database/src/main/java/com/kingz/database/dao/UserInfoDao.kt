package com.kingz.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kingz.database.entity.UserInfoEntity

/**
 * @author zeke.wang
 * @date 2020/7/20
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc: 用户信息DAO
 */
@Dao
interface UserInfoDao {
    @Query("SELECT * FROM UserInfoEntity WHERE isLogin = 1")
    fun queryUserIsLogin():LiveData<UserInfoEntity>

    @Query("SELECT token FROM UserInfoEntity WHERE userId == :userId")
    fun queryUserToken(userId:String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserInfo(entity: UserInfoEntity)
}