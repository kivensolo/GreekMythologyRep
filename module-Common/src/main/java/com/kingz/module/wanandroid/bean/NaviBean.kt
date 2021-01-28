package com.kingz.module.wanandroid.bean

/**
 * @author Jenly [Jenly](mailto:jenly1314@gmail.com)
 */
class NaviBean {
    var cid = 0
    var name: String? = null
    private var articles: List<Article>? = null

    fun getArticles(): List<Article>? {
        return articles
    }

    fun setArticles(articles: List<Article>?) {
        this.articles = articles
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val naviBean = o as NaviBean
        if (cid != naviBean.cid) return false
        return if (name != null) name == naviBean.name else naviBean.name == null
    }

    override fun hashCode(): Int {
        var result = cid
        result = 31 * result + if (name != null) name.hashCode() else 0
        return result
    }

    override fun toString(): String {
        return "NaviBean{" +
                "cid=" + cid +
                ", name='" + name + '\'' +
                ", articles=" + articles +
                '}'
    }
}