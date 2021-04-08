package com.kingz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * author：ZekeWang
 * date：2021/3/31
 * description：用户收藏文章的Entity
 */
@Entity(tableName = "collect_article")
class CollectionArticle : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name="title_id",defaultValue = "1")
    var title_id:Int = 0

    @ColumnInfo(name="title_name",defaultValue = "WanAndroid artical")
    lateinit var title_name:String

    @ColumnInfo(name="date",defaultValue = "1970-1-1 00:00")
    lateinit var date:String

    @ColumnInfo(name="link")
    lateinit var link:String

    @ColumnInfo(name="review_score",defaultValue = "1.0")
    var score=0.0

}