package com.kingz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.kingz.database.config.DBConfig
import java.io.Serializable

/**
 * author: King.Z <br>
 * date:  2020/7/24 22:12 <br>
 * description: 数据库保存的用户信息条目 <br>
 */
@Entity(tableName = DBConfig.TAB_NAME_OF_USER, primaryKeys = ["id"])
class UserEntity(
    @ColumnInfo(name = "admin")
    var admin: Boolean = false,

    @ColumnInfo(name = "nickname")
    var nickname: String = "",

    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "publicName")
    var publicName: String = "",

    @ColumnInfo(name = "token")
    var token: String = "",

    @ColumnInfo(name = "cookies")
    var cookies: String = "",

    @ColumnInfo(name = "id")
    var id: Int = -1,

    //后续加的，wanandroid不一定有
    @ColumnInfo(name = "userAvatar")
    var userAvatar: String? = "",

    @ColumnInfo(name = "currentUser")
    var currentUser: Int = 1 // 1为当前用户
): Serializable {

    override fun toString(): String {
        return "UserEntity(admin=$admin, nickname='$nickname', username='$username', publicName='$publicName', token='$token', cookies='$cookies', id=$id)"
    }
}