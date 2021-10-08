package com.kingz.database.config

import androidx.annotation.VisibleForTesting

/**
 * author：ZekeWang
 * date：2021/9/23
 * description：数据库 配置项
 *
 * SQL语法记录：
 *
 * 【UPDATE】
 * 更新某一行中的一个列:
 * UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
 * 更新某一行中的若干列:
 * UPDATE 表名称 SET 列名称1 = 新值,列名称2 = 新值 WHERE 列名称 = 某值
 *
 * 【QUERY】
 * 查询name字段是否等于入参
 *  Query("SELECT * FROM ${tableName} WHERE name = :入参名 ")
 *
 * 查询某个字段是true
 *  @Query("SELECT * FROM ${tableName} WHERE hasBound = 1")
 *
 *  //根据用户id,更新某个字段集，相等，则赋值为1(true)，否则为0
 *  @Query("UPDATE ${tableName} SET currentUser = CASE WHEN userId = :userId THEN 1 ELSE 0 END")
    fun updateCurrentUser(userId: String)
 */
class DBConfig {
     companion object {
        //---------- 数据库名
        @VisibleForTesting
        const val DATA_BASE_NAME = "greek-mythology-db"

        //---------- 各表名配置项 START
        const val TAB_NAME_OF_COLLECT_ARTICLE = "collect_article"
        const val TAB_NAME_OF_HTTP_COOKIE = "http_cookie"
        const val TAB_NAME_OF_USER = "user"
        const val TAB_NAME_OF_PRODUCTS = "products"
        //---------- 各表名配置项 END
    }
}