package com.kingz.database

import com.facebook.stetho.Stetho
import com.kingz.base.BaseApplication
import com.kingz.database.dao.AlbumDao
import com.kingz.database.dao.SongDao
import com.kingz.database.dao.UserDao

/**
 * @author zeke.wang
 * @date 2020/6/11
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
open class DatabaseApplication : BaseApplication() {

    companion object {

        @Suppress("unused")
        private const val TAG = "DatabaseApplication"

        private var INSTANCE: DatabaseApplication? = null

        fun getInstance(): DatabaseApplication {
            return INSTANCE!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        // 注册数据库查看器
        Stetho.initializeWithDefaults(this)
    }

    //------------ DAO 获取函数  可下沉至业务层-----------------//
    fun getSongDao(): SongDao {
        return DatabaseBuilder.getInstance(this)!!.getSongDao()
    }

    fun getAlbumDao(): AlbumDao {
        return DatabaseBuilder.getInstance(this)!!.getAlbumDao()
    }

    fun getUserDao(): UserDao {
        return DatabaseBuilder.getInstance(this)!!.getUserDao()
    }

    //------------ DAO 获取函数 -----------------//
    fun destroyDatabase() {
        DatabaseBuilder.destroyDatabase()
    }
}