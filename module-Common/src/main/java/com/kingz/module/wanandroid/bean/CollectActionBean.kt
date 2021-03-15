package com.kingz.module.wanandroid.bean

/**
 * author：ZekeWang
 * date：2021/2/27
 * description：收藏行为的Bean对象
 */
class CollectActionBean {
    var actionType :TYPE = TYPE.DEFAULT
    var isSuccess:Boolean = false
    var errorMsg:String = ""
    var bindArticleData:Article ?= null
    var articlePostion:Int = 0

    enum class TYPE{
        DEFAULT,COLLECT,UNCOLLECT
    }
}