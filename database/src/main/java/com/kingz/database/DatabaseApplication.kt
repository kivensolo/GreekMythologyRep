package com.kingz.database

import com.facebook.stetho.Stetho
import com.kingz.base.AppExecutors
import com.kingz.base.BaseApplication
import com.kingz.database.dao.AlbumDao
import com.kingz.database.dao.CookiesDao
import com.kingz.database.dao.SongDao
import com.kingz.database.dao.UserDao

/**
 * @author zeke.wang
 * @date 2020/6/11
 * @maintainer zeke.wang
 * @desc: 数据库Application顶层模块
 */
open class DatabaseApplication : BaseApplication() {

    private lateinit var mAppExecutors: AppExecutors

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
        mAppExecutors = AppExecutors()

        // 注册数据库查看器
        Stetho.initializeWithDefaults(this)
    }

    //------------ DAO 获取函数  可下沉至业务层-----------------//
    fun getSongDao(): SongDao = getDataBase().getSongDao()
    fun getAlbumDao(): AlbumDao = getDataBase().getAlbumDao()

    fun getUserDao(): UserDao = getDataBase().getUserDao()
    fun getCookiesDao(): CookiesDao = getDataBase().getCookiesDao()

    //------------ DAO 获取函数 -----------------//

    fun getDataBase():AppDatabase {
        return AppDatabase.getInstance(this, mAppExecutors)
    }

    fun destroyDatabase() {
        AppDatabase.destroyDatabase()
    }
}