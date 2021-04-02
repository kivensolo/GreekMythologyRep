package com.kingz.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kingz.database.entity.CollectionArticle

/**
 * author：ZekeWang
 * date：2021/3/31
 * description：收藏文章的DAO接口
 */
@Dao
interface CollectionArticalDao {
    @Insert
    fun insert(vararg articals: CollectionArticle):LongArray?

    @Delete
    fun delete(artical:CollectionArticle?):Int

    @Update
    fun update(vararg articals:CollectionArticle):Int

    //使用LiveData进行观察查询
    @get:Query("SELECT * FROM collect_artical")
    val allCollectArticals: LiveData<List<CollectionArticle?>?>

}