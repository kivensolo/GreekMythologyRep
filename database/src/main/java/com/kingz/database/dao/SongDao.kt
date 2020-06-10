package com.kingz.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.kingz.database.entity.SongEntity

@Dao
abstract class SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data: SongEntity)

     @Delete
    abstract fun delete(data: SongEntity)
}