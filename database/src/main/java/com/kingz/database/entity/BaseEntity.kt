package com.kingz.database.entity

import androidx.room.Entity
import java.io.Serializable

@Entity(primaryKeys = ["videoId"])
open class BaseEntity : Serializable {
    var type: String = ""
    var videoName: String? = null
    var contentPic: String? = null
    var videoId: String = ""
    var lastPosition: Long = 0
    var lastIndex: Int = 0
}

