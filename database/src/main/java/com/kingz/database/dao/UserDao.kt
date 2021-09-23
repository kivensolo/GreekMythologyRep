package com.kingz.database.dao

import androidx.room.*
import com.kingz.database.config.DBConfig
import com.kingz.database.entity.UserEntity

/**
 * author: King.Z <br>
 * date:  2020/7/24 22:06 <br>
 * description: Data access object of user info.  <br>
 * 增加多用户管理操作  2021/9/23
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: UserEntity)

    // 删除指定用户 Method annotated with @Delete but does not have any parameters to delete.
    @Delete
    suspend fun deleteUser(user:UserEntity)
    // does not have any parameters to delete

    /**
     * 删除全部用户
     */
    @Query("DELETE FROM ${DBConfig.TAB_NAME_OF_USER}")
    suspend fun deleteAll(): Int

    /**
     * 退出当前用户
     */
    @Query("UPDATE ${DBConfig.TAB_NAME_OF_USER} SET token = '' WHERE currentUser = 1")
    suspend fun userLogout()

//    @Update
//    suspend fun update(data: UserEntity)

    @Query("SELECT * FROM ${DBConfig.TAB_NAME_OF_USER}")
    suspend fun getUserInfo(): UserEntity?

    /**
     * 根据用户名查找指定用户信息
     */
    @Query("SELECT * FROM ${DBConfig.TAB_NAME_OF_USER} where username = :name")
    suspend fun getUserByName(name:String):UserEntity?

    /**
     * 更新当前用户的头像
     */
    @Query("UPDATE ${DBConfig.TAB_NAME_OF_USER} SET userAvatar = :path WHERE currentUser = 1")
    suspend fun updateAvatar(path: String)

    /**
     * 更新当前用户
     */
    @Query("UPDATE ${DBConfig.TAB_NAME_OF_USER} SET currentUser = CASE WHEN id = :userId THEN 1 ELSE 0 END")
    fun updateCurrentUser(userId: Int)


    @Transaction
    suspend fun insertAndUpdate(user: UserEntity) {
        insert(user)
        updateCurrentUser(user.id)
    }
}