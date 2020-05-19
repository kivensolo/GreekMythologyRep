package com.zeke.home.entity

/**
 * author：KingZ
 * date：2020/2/21
 * description：首页推荐数据格式
 */
data class HomeRecomData(val id: String,
                         val name:String,
                         val type:String,
                         var page_content: MutableList<PageContent>?) {
    fun getPageContent(): MutableList<PageContent>? {
        return page_content
    }

    fun setPageContent(pc:MutableList<PageContent>?){
        page_content = pc
    }

}
data class PageContent(var id:String,var type:String)
