package com.kingz.database

import com.kingz.database.entity.BaseEntity

/**
 * 数据库抽象操作模型
 * 可下沉至业务层，具体行为由业务控制。此处只用于演示
 */
interface IDatabaseModel<E : BaseEntity> {
    fun deleteSong(entity: E?)

    fun insertSong(entity: E?)

    fun querySongBy(songName: String?, singer: String?): BaseEntity?

    //Other opration...
}