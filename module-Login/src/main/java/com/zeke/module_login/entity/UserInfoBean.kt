package com.zeke.module_login.entity

/**
 * author: King.Z <br>
 * date:  2020/7/19 17:05 <br>
 * description:  <br>
 */
data class UserInfoBean(
    val data:Data?,
    val errorCode:Int,
    val errorMsg:String?
)

data class Data(
    val admin:Boolean,
//    val chapterTops:List<??>,
    val nickname:String,
    val username:String,
    val publicName:String,
    val collectIds:List<Int>,
    val coinCount:Int,
    val email:String?,
    val icon:String?,
    val id:Int,
    val password:String,
    val token:String,
    val type:Int
)