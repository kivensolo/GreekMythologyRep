package com.kingz.coroutines.demo.entity

/**
 * 登录用户数据结构
 */
data class LoginEntity(
        val code: Int,
        val userId: String,
        val result: String
)