package com.kingz.database.dao

import androidx.room.*
import com.kingz.database.entity.BaseEntity
import com.kingz.database.entity.SongEntity

@Dao
abstract class SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data: BaseEntity)

    @Delete
    abstract fun delete(data: BaseEntity)

    @Query("SELECT * FROM SongEntity WHERE name==:songName ")
    abstract fun querySongBy(songName: String): SongEntity?

    @Query("SELECT * FROM SongEntity WHERE name==:songName AND singer==:singer")
    abstract fun querySongBy(songName: String, singer: String?): SongEntity?

    @Update
    abstract fun update(data: SongEntity)
}