package com.kingz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * author：ZekeWang
 * date：2021/3/31
 * description：收藏文章的Entity
 *
 * @Entity 标识数据库中的表
 * @PrimaryKey 表示主键，autoGenerate表示自增
 * @ColumnInfo表示字段，name表示字段名称
 */
@Entity(tableName = "collect_artical")
class CollectionArtical : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name="title_name",defaultValue = "WanAndroid artical")
    lateinit var title:String

    @ColumnInfo(name="date",defaultValue = "1970-1-1 00:00")
    lateinit var date:String

    @ColumnInfo(name="review_score",defaultValue = "1.0")
    var score=0.0

}