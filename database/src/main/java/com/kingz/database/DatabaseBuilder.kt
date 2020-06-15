package com.kingz.database

import android.content.Context
import androidx.room.Room

/**
 * @author zeke.wang
 * @date 2020/6/15
 * @maintainer zeke.wang
 * @desc: DatabaseBuilder
 */
object DatabaseBuilder {
    private const val playTableName = "greek-mythology-db"

    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    playTableName
            )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

    fun destroyDatabase() {
        INSTANCE = null
    }

}