package com.kingz.coroutines.demo.entity

/**
 * 推荐文章的数据类
 */
data class ChaptersEntity(
        val `data`: List<Data>,
        val errorCode: Int,
        val errorMsg: String
)

data class Data(
        val children: List<Any>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int
)