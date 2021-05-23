package com.kingz.database.dao

import androidx.room.*
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

    // 删除指定用户 Method annotated with @Delete but does not have any parameters to delete.
    @Delete
    suspend fun deleteUser(user:UserEntity)
    // does not have any parameters to delete

    //删除全部
    @Query(("DELETE FROM user"))
    suspend fun userLogout()

//    @Update
//    suspend fun update(data: UserEntity)

    @Query("SELECT * FROM user")
    suspend fun getUserInfo(): UserEntity?

    //根据用户名查找指定用户信息
    @Query("SELECT * FROM user where username=:name")
    suspend fun getUserByName(name:String):UserEntity?
}