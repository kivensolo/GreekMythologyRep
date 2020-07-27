package com.kingz.database.dao

import androidx.room.*
import com.kingz.database.entity.BaseEntity
import com.kingz.database.entity.SongEntity

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: BaseEntity)

    @Delete
    fun delete(data: BaseEntity)

    @Query("SELECT * FROM SongEntity WHERE name==:songName ")
    fun querySongBy(songName: String): SongEntity?

    @Query("SELECT * FROM SongEntity WHERE name==:songName AND singer==:singer")
    fun querySongBy(songName: String, singer: String?): SongEntity?

    @Update
    fun update(data: SongEntity)
}