package com.kingz.database

import com.kingz.database.entity.BaseEntity

/**
 * @author zeke.wang
 * @date 2020/6/11
 * @maintainer zeke.wang
 * @desc: 数据操作模型的抽象类
 * 可下沉至业务层
 */
abstract class AbsDatabaseModel<E : BaseEntity> : IDatabaseModel<E> {
    companion object {
        private const val TAG = "AbsDatabaseModel"
    }

    private val songDao = DatabaseApplication.getInstance().getSongDao()
    private val albumDao = DatabaseApplication.getInstance().getAlbumDao()

    override fun deleteSong(entity: E?) {
        entity ?: return
        songDao.delete(entity)
    }

    override fun insertSong(entity: E?) {
        entity ?: return
        songDao.insert(entity)
    }

    override fun querySongBy(songName: String?, singer: String?): BaseEntity? {
        if (songName.isNullOrEmpty()) return null
        return songDao.querySongBy(songName, singer)
    }

}