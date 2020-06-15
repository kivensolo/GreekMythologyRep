package com.kingz.coroutines.data.local

import android.content.Context
import androidx.room.Room
import com.kingz.database.AppDatabase

/**
 * @author zeke.wang
 * @date 2020/6/15
 * @maintainer zeke.wang
 * @desc: DatabaseBuilder
 */
object DatabaseBuilder {
    private const val playTableName = "room-demo-db"

    private var INSTANCE: RoomDemoDatabase? = null

    fun getInstance(context: Context): RoomDemoDatabase {
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
                    RoomDemoDatabase::class.java,
                    playTableName)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

    fun destroyDatabase() {
        INSTANCE = null
    }

}