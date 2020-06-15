package com.kingz.coroutines.data.local

import com.kingz.coroutines.data.local.entity.User

/**
 * 业务层的数据抽象行为
 */
interface DatabaseHelper {

    suspend fun getUsers(): List<User>

    suspend fun insertAll(users: List<User>)

}