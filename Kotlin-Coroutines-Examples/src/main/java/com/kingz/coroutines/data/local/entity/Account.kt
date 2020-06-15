package com.kingz.coroutines.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * App用户登录信息
 */
@Entity
data class Account(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "pwd") val password: String?,
    @ColumnInfo(name = "avatar") val avatar: String?
)