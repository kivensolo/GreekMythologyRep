package com.kingz.module.wanandroid.bean

class CollectBean {
    /**
     * author : 小编
     * chapterId : 352
     * chapterName : 资讯
     * courseId : 13
     * desc :
     * envelopePic :
     * id : 15915
     * link : http://www.wanandroid.com/blog/show/2
     * niceDate : 刚刚
     * origin :
     * originId : 2864
     * publishTime : 1530849681000
     * title : 玩Android API
     * userId : 3273
     * visible : 0
     * zan : 0
     */
    var author: String? = null
    var chapterId = 0
    var chapterName: String? = null
    var courseId = 0
    var desc: String? = null
    var envelopePic: String? = null
    var id = 0
    var link: String? = null
    var niceDate: String? = null
    var origin: String? = null
    var originId = -1
    var publishTime: Long = 0
    var title: String? = null
    var userId = 0
    var visible = 0
    var zan = 0

    override fun toString(): String {
        return "CollectBean{" +
                "author='" + author + '\'' +
                ", chapterId=" + chapterId +
                ", chapterName='" + chapterName + '\'' +
                ", courseId=" + courseId +
                ", desc='" + desc + '\'' +
                ", envelopePic='" + envelopePic + '\'' +
                ", id=" + id +
                ", link='" + link + '\'' +
                ", niceDate='" + niceDate + '\'' +
                ", origin='" + origin + '\'' +
                ", originId=" + originId +
                ", publishTime=" + publishTime +
                ", title='" + title + '\'' +
                ", userId=" + userId +
                ", visible=" + visible +
                ", zan=" + zan +
                '}'
    }
}