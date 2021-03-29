package com.kingz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kingz.database.dao.AlbumDao
import com.kingz.database.dao.SongDao
import com.kingz.database.dao.UserDao
import com.kingz.database.entity.BaseEntity
import com.kingz.database.entity.SongEntity
import com.kingz.database.entity.UserEntity

/**
 * @author zeke.wang
 * @date 2020/6/10
 * @maintainer zeke.wang
 * @desc: App抽象数据库层
 */
@Database(
    entities = [
        BaseEntity::class,
        SongEntity::class,
        UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSongDao(): SongDao
    abstract fun getAlbumDao(): AlbumDao
    abstract fun getUserDao(): UserDao

    companion object {
        private const val databaseName = "greek-mythology-db"
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }

        /**
         * Room启动时将检测version是否发生增加，如果有，那么将找到Migration去执行特定的操作。
         * 如果没有,则会因为fallbackToDestructiveMigration()。将
         * 会删除数据库并重建…此时不会crash，但所有数据丢失。
         */
        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java,databaseName)
//                .allowMainThreadQueries()   // 不允许主线程进行IO操作
                .fallbackToDestructiveMigration()
                .addMigrations(migration_1to2,migration_2to3) //执行数据库迁移
                .build()

        internal val database: AppDatabase by lazy {
            getInstance(DatabaseApplication.getInstance())
        }

        fun destroyDatabase() {
            INSTANCE = null
        }

        /**
         * 数据库迁移对象
         */
        private val migration_1to2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // TODO 如何修改表名?  UserEntity 改为user
                database.execSQL("ALTER TABLE UserEntity ADD COLUMN money TEXT NOT NULL DEFAULT ''")
            }
        }
        /**
         * 数据库迁移对象
         * vesion增加，提供Migration(即使表结构没有变化) - 数据正常
         */
        private val migration_2to3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //没有变化，所以是一个空实现
            }
        }
    }

}