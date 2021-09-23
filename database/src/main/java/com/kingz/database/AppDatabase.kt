package com.kingz.database

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kingz.database.config.DBConfig
import com.kingz.database.dao.*
import com.kingz.database.entity.*

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
 *
 * Release Note:
 * v2 :
 *   完善数据库迁移版本的改造
 * V3:
 *   增加HttpCookie表
 */
@Database(
    entities = [
        BaseEntity::class,
        SongEntity::class,
        UserEntity::class,
        CookiesEntity::class,
        CollectionArticle::class],
    version = 3, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSongDao(): SongDao
    abstract fun getAlbumDao(): AlbumDao
    abstract fun getUserDao(): UserDao
    abstract fun getCookiesDao(): CookiesDao
    abstract fun getCollectArticleDao(): CollectionArticleDao


    val databaseCreated = MutableLiveData<Boolean?>()

    companion object {
        private const val DATA_BASE_NAME = DBConfig.DATA_BASE_NAME
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
         * Room启动时通过SQLiteOpenHelper检测version是否发生变化，如果有，则触发onDowngrade或者onUpgrade。
         * 在RoomOpenHelper中进行升级或降级操作: 遍历已有的Migration列表去执行特定迁移的操作。
         * 执行完毕后，会进行结果有效性判断。
         *
         * fallbackToDestructiveMigration()： 在迁移失败时，将
         * 会删除数据库并重建…此时不会crash，但所有数据丢失。
         */
        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, DATA_BASE_NAME)
                .allowMainThreadQueries()   // 不允许主线程进行IO操作
                .fallbackToDestructiveMigration()
                .addMigrations(migration_1to2, migration_2to3)
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
//                                val ids = dataBase.getCollectArticalDao().insert(*Utils.initData)
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
         * 数据库迁移 V1~V2
         * 增加收藏数据表
         */
        private val migration_1to2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.run {
                    /**
                     * 增加新表操作
                     * 表名: 大小写有影响
                     * DEFAULT 无用
                     */
                    execSQL("CREATE TABLE IF NOT EXISTS ${DBConfig.TAB_NAME_OF_COLLECT_ARTICLE} (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "title_id INTEGER NOT NULL DEFAULT 1," +
                            "date TEXT NOT NULL DEFAULT '1970-1-1 00:00'," +
                            "title_name TEXT NOT NULL DEFAULT 'WanAndroid artical'," +
                            "link TEXT NOT NULL," +
                            "review_score REAL NOT NULL DEFAULT 1.0)")


                    /**
                     * 对表进行列的添加、修改或删除操作(ALTER TABLE)
                     * int类型默认值切记不要加''
                     *
                     * 此处模拟增加一列rank数据
                     */
//                   execSQL("ALTER TABLE collect_article ADD COLUMN rank INTEGER NOT NULL DEFAULT 0")
//                   execSQL("ALTER TABLE USER ADD COLUMN money TEXT NOT NULL DEFAULT ''")

                    /**
                     * 向表中插入新的列: INSERT INTO
                     * [语法:]
                     * INSERT INTO table_name VALUES (Value1, Value2,....)
                     * INSERT INTO table_name (COLUMN_1_NAME, COLUMN_2_NAME,...) VALUES (Value1, Value2,....)
                     *
                     */
                    execSQL("INSERT INTO ${DBConfig.TAB_NAME_OF_COLLECT_ARTICLE} VALUES(10,10086,'2021-04-02','学Android从入门到放弃','http://www.baidu.com','98.8')")


                    /**
                     * 删除表操作
                     */
                    //database.execSQL("DROP TABLE BaseEntity")
                }


            }
        }

        /**
         * 数据库迁移 V2~V3
         * 增加Cookies数据表
         */
        private val migration_2to3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS ${DBConfig.TAB_NAME_OF_HTTP_COOKIE} (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "url TEXT NOT NULL DEFAULT ''," +
                            "cookies TEXT NOT NULL DEFAULT '')")
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