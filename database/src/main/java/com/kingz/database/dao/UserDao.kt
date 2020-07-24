package com.kingz.database.dao

import androidx.room.*
import com.kingz.database.entity.UserEntity

/**
 * author: King.Z <br>
 * date:  2020/7/24 22:06 <br>
 * description: Data access object of user info.  <br>
 */
@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data: UserEntity)

//    //TODO 如何删除全部数据?
//    @Delete
//    abstract fun delete()  // does not have any parameters to delete

    @Update
    abstract fun update(data: UserEntity)

    @Query("SELECT * FROM UserEntity")
    abstract fun getUserInfo(): UserEntity?
}