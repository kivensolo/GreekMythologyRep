package com.kingz.database.entity

import androidx.room.Entity

/**
 * @author zeke.wang
 * @date 2020/7/20
 * @maintainer zeke.wang
 * @desc: 用户信息的数据库DAO
 */
@Entity(primaryKeys = ["id"])
class UserInfoEntity(
    val admin:Boolean,
    val isLogin:Boolean,
    val userId:Int,
    val nickname:String,
    val publicName:String,
    val username:String,
    val email:String,
    val token:String
)