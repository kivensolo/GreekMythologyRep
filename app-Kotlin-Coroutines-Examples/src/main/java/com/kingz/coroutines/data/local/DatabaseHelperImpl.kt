package com.kingz.coroutines.data.local

import com.kingz.coroutines.data.local.entity.User

class DatabaseHelperImpl(private val appDatabase: RoomDemoDatabase) : DatabaseHelper {

    override suspend fun getUsers(): List<User> = appDatabase.userDao().getAll()

    override suspend fun insertAll(users: List<User>) {
        appDatabase.userDao().insertAll(users)
    }
}