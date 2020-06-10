package com.kingz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SongEntity(
        @PrimaryKey
        val id: Long? = null,
        val name: String = "",
        @ColumnInfo(name = "release_year")
        val releaseYear: Int = 0)