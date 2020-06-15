package com.kingz.coroutines.data.local

import androidx.room.Database
import com.kingz.coroutines.data.local.dao.UserDao
import com.kingz.coroutines.data.local.entity.User
import com.kingz.database.AppDatabase

@Database(entities = [User::class], version = 1)
abstract class MvvmDatabase : AppDatabase() {
    abstract fun userDao(): UserDao

    abstract fun apiUserDao(): UserDao

}