package com.kingz.coroutines.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kingz.coroutines.data.local.dao.UserDao
import com.kingz.coroutines.data.local.entity.User

@Database(entities = [User::class], version = 1)
abstract class RoomDemoDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}