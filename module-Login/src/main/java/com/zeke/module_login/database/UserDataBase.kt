package com.zeke.module_login.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kingz.database.dao.UserDao
import com.zeke.module_login.entity.UserInfoBean

/**
 * author: King.Z <br>
 * date:  2020/7/24 22:08 <br>
 * description:  <br>
 *
 *     FIXME 如何在低层模块去获取高层DataBase对象
 */
@Database(entities = [UserInfoBean::class], version = 1)
abstract class UserDataBase: RoomDatabase() {
    abstract fun getUserDao(): UserDao
}