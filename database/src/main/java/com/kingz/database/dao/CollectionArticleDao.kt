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
interface CollectionArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg articles: CollectionArticle):LongArray?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articlesList: MutableList<CollectionArticle>):List<Long>?

    @Delete
    fun delete(article:CollectionArticle?):Int

    @Update
    fun update(vararg articles:CollectionArticle):Int

    //使用LiveData进行观察查询
    @get:Query("SELECT * FROM collect_article")
    val allCollectArticles: LiveData<List<CollectionArticle?>?>

    @Query("SELECT * FROM collect_article")
    fun getUserAllCollectLIst():List<CollectionArticle>

}