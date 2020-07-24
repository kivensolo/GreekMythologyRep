package com.kingz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "songs")
@Entity
class SongEntity(
    @PrimaryKey

    @ColumnInfo(name = "singer")
    var singer: String = "",

    @ColumnInfo(name = "release_year")
    var releaseYear: Int = 0
) : BaseEntity()