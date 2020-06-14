package com.zeke.home.model

import com.kingz.database.AbsDatabaseModel
import com.kingz.database.DatabaseApplication
import com.kingz.database.entity.BaseEntity
import com.kingz.database.entity.SongEntity

class HomeSongModel<E : BaseEntity> : AbsDatabaseModel<E>() {

    private val songDao = DatabaseApplication.getInstance().getSongDao()

    fun testInsertData() {
        val songEntity = SongEntity()
        songEntity.id = 1000
        songEntity.name = "美人鱼"
        songEntity.singer = "林俊杰"
        songEntity.releaseYear = 2020
        songDao.insert(songEntity)
    }
}