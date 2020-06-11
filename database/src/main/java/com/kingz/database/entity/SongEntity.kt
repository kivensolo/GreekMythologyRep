package com.kingz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SongEntity(
        @PrimaryKey
        var id: Long? = null,
        var name: String = "",
        var singer: String = "",
        @ColumnInfo(name = "release_year")
        var releaseYear: Int = 0): BaseEntity()