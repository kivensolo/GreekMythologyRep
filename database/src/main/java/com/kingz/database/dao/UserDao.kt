package com.kingz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kingz.database.entity.UserEntity

/**
 * author: King.Z <br>
 * date:  2020/7/24 22:06 <br>
 * description: Data access object of user info.  <br>
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: UserEntity)

//    //TODO 如何删除全部数据?
//    @Delete
    // Method annotated with @Delete but does not have any parameters to delete.
//    suspend fun logout()  // does not have any parameters to delete

//    @Update
//    suspend fun update(data: UserEntity)

    @Query("SELECT * FROM user")
    suspend fun getUserInfo(): UserEntity?

    //TODO 更新用户Cookies信息
//    @Query("SELECT * FROM user where publicName NOT NULL")
//    suspend fun isUserLogin():Int
}