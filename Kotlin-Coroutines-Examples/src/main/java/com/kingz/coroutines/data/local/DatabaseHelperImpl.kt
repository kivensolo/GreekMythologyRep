package com.kingz.coroutines.data.local

import com.kingz.coroutines.data.local.entity.User
import com.kingz.database.AppDatabase

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {

    override suspend fun getUsers(): List<User> = (appDatabase as MvvmDatabase).userDao().getAll()

    override suspend fun insertAll(users: List<User>) {
        (appDatabase as MvvmDatabase).userDao().insertAll(users)
    }
}