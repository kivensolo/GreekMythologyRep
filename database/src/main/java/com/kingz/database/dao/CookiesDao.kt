package com.kingz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kingz.database.entity.CookiesEntity

/**
 * author：ZekeWang
 * date：2021/5/11
 * description：Data access object of httpCookie.
 */
@Dao
interface CookiesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cookie: CookiesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cookiesList: MutableList<CookiesEntity>): List<Long>?

    @Query(("DELETE FROM http_cookie"))
    suspend fun clear()

    //根据指定URL查找cookie
    @Query("SELECT * FROM http_cookie where url=:url")
    fun getCookies(url: String): CookiesEntity?
}