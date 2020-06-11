package com.kingz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kingz.database.dao.AlbumDao
import com.kingz.database.dao.SongDao
import com.kingz.database.entity.BaseEntity
import com.kingz.database.entity.SongEntity

/**
 * @author zeke.wang
 * @date 2020/6/10
 * @maintainer zeke.wang
 * @desc: App抽象数据库层
 */
@Database(entities = [BaseEntity::class, SongEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSongDao():SongDao

    abstract fun getAlbumDao(): AlbumDao


    companion object {

        private const val playTableName = "GreekDB"

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room
                            .databaseBuilder(context.applicationContext,
                                    AppDatabase::class.java,
                                    playTableName)      // 创建RoomDatabase实例对象
                            .allowMainThreadQueries()   // 允许主线程查询(Room默认不允许在主线程进行数据查询)
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyDatabase() {
            INSTANCE = null
        }
    }
}