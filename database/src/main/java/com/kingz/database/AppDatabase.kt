package com.kingz.database

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kingz.database.dao.AlbumDao
import com.kingz.database.dao.CollectionArticalDao
import com.kingz.database.dao.SongDao
import com.kingz.database.dao.UserDao
import com.kingz.database.entity.BaseEntity
import com.kingz.database.entity.CollectionArtical
import com.kingz.database.entity.SongEntity
import com.kingz.database.entity.UserEntity

/**
 * @author zeke.wang
 * @date 2020/6/10
 * @maintainer zeke.wang
 * @desc: App抽象数据库层
 *
 * @Database 表示继承自RoomDatabase的抽象类，
 *           entities指定表的实现类列表，version指定了DB版本
 *
 * 必须提供获取DAO接口的抽象方法，Room将通过这个方法实例化DAO接口。
 * 编译时将生成AppDatabase_Impl.java实现类
 */
@Database(
    entities = [BaseEntity::class,SongEntity::class,
        UserEntity::class, CollectionArtical::class],
    version = 3, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSongDao(): SongDao
    abstract fun getAlbumDao(): AlbumDao
    abstract fun getUserDao(): UserDao
    abstract fun getCollectArticalDao(): CollectionArticalDao
    val databaseCreated = MutableLiveData<Boolean?>()

    companion object {
        private const val DATA_BASE_NAME = "greek-mythology-db"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
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
                AppDatabase::class.java, DATA_BASE_NAME)
//                .allowMainThreadQueries()   // 不允许主线程进行IO操作
                .fallbackToDestructiveMigration()
                .addMigrations(migration_1to2,migration_2to3) //执行数据库迁移
                .addCallback(object :Callback(){
                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        // 若使用了fallbackToDestructiveMigration()
                        // 那么升级失败时次函数会被回调，可以试着在这里重新初始化D
                        super.onDestructiveMigration(db)
                    }
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
//                        Executors.newFixedThreadPool(5).execute {
//                                //RoomDatabase创建后异步插入初始化数据，并通知MediatorLiveData
                                val dataBase = getInstance(context)
////                                val ids = dataBase.getCollectArticalDao().insert(*Utils.initData)
                                dataBase.databaseCreated.postValue(true)
//                            }
                    }
                })
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
                //当添加int 类型数据时，需要添加默认值
//                database.execSQL("ALTER TABLE UserEntity ADD COLUMN age INTEGER NOT NULL DEFAULT 10")
                database.execSQL("ALTER TABLE USER ADD COLUMN money TEXT NOT NULL DEFAULT ''")
            }
        }

        private val migration_2to3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 新增了文章收藏数据的表, 但是COLUMN要手动加
                database.execSQL("CREATE TABLE IF NOT EXISTS `COLLECT_ARTICAL` (" +
                        "`date` TEXT NOT NULL DEFAULT '1970-1-1 00:00'," +
                        "`id` INTEGER NOT NULL DEFAULT null," +
                        "`title_name` TEXT NOT NULL DEFAULT 'WanAndroid artical'," +
                        "`review_score` REAL NOT NULL DEFAULT '1.0'" +
                        ")")
//                database.execSQL("ALTER TABLE COLLECT_ARTICAL ADD COLUMN date TEXT NOT NULL DEFAULT '1970-1-1 00:00'")
//                database.execSQL("ALTER TABLE COLLECT_ARTICAL ADD COLUMN id INTEGER NOT NULL DEFAULT 0")
//                database.execSQL("ALTER TABLE COLLECT_ARTICAL ADD COLUMN title_name TEXT NOT NULL DEFAULT 'WanAndroid artical'")
//                database.execSQL("ALTER TABLE COLLECT_ARTICAL ADD COLUMN review_score REAL NOT NULL DEFAULT '1.0'")
            }
        }

        /**
         * 数据库迁移对象
         * vesion增加，提供Migration(即使表结构没有变化) - 数据正常
         */
        private val migration_6to7: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //没有变化，所以是一个空实现
            }
        }
    }

}