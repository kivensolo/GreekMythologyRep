package com.kingz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * author：ZekeWang
 * date：2021/10/8
 * description： 测试表
 */
@Entity(tableName = "info_table")
class Info {
    //Primary key constraint on infoId is ignored when being merged into com.kingz.database.entity.ProductEntity
//    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "infoId")
    var infoId = 0

    @ColumnInfo(name = "title")
    var title: String? = "defalut"

    @ColumnInfo(name = "content")
    var content: String? = "defalut"
}