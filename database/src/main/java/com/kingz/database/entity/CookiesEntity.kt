package com.kingz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

/**
 * author：ZekeWang
 * date：2021/5/11
 * description：网络请求的Cookie表
 * @since DataBase v3
 */
@Entity(tableName = "http_cookie", primaryKeys = ["id"])
class CookiesEntity(
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "url", defaultValue = "")
    var url: String = "",
    @ColumnInfo(name = "cookies", defaultValue = "")
    var cookies: String = ""
) : Serializable {
    override fun toString(): String {
        return "CookiesEntity(id=$id, url='$url', cookies='$cookies')"
    }
}