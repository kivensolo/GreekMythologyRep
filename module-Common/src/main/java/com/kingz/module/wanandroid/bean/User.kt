package com.kingz.module.wanandroid.bean

import java.io.Serializable

class User : Serializable {
    /**
     * admin : false
     * chapterTops : []
     * collectIds : [7484,2696,7654,5573,7958,8252,8227,8080,3365,2439,1467,3596,2897,979,8247,8438,8694]
     * email :
     * icon :
     * id : 12331
     * nickname : RookieJay
     * password :
     * token :
     * type : 0
     * username : RookieJay
     */
    private val admin = false
    var email: String? = null
    var icon: String? = null
    var id = 0
    private val nickname: String? = null
    var password: String? = null
    var type = 0
    var username: String? = null
    private val chapterTops: List<*>? = null
    var collectIds: List<Int>? = null

    override fun toString(): String {
        return "User(admin=$admin, email=$email, icon=$icon, id=$id, nickname=$nickname, password=$password, type=$type, username=$username, chapterTops=$chapterTops, collectIds=$collectIds)"
    }
}