package com.kingz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity(primaryKeys = ["id"])
open class BaseEntity : Serializable {
//    @PrimaryKey(autoGenerate = true)
//    var id = 0

    @ColumnInfo(name = "type")
    var type: String = ""
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "img_url")
    var pic: String? = null
    @ColumnInfo(name = "id")
    var id: String = ""
}

