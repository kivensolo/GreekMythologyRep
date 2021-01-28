package com.kingz.module.wanandroid.bean

/**
 * WanAndroid收藏列表Bean
 */
class CollectListBean {
    /**
     * curPage : 1
     * datas {
     * }
     */
    var curPage = 0
    var collectList: List<CollectBean>? = null
    override fun toString(): String {
        return "CollectListBean(curPage=$curPage, collectList=$collectList)"
    }

}