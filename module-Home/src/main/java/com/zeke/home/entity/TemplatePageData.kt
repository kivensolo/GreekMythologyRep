package com.zeke.home.entity

/**
 * author：KingZ
 * date：2020/2/21
 * description：首页Page模板数据格式
 */
data class TemplatePageData(val id: String = "",
                            val name:String = "",
                            val type:String = "",
                            var page_content: MutableList<PageContent>? = null) {
    fun getPageContent(): MutableList<PageContent>? {
        return page_content
    }

    fun setPageContent(pc:MutableList<PageContent>?){
        page_content = pc
    }

}
data class PageContent(var id:String,var type:String)
