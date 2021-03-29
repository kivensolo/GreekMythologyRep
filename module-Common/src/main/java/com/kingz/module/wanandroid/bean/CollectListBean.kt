package com.kingz.module.wanandroid.bean

/**
 * WanAndroid 用户收藏文章列表
 */
class CollectListBean {
    /**
     * curPage : 1
     * datas : [{"apkLink": "",
                "author": "MannaYang",
                "chapterId": 97,
                "chapterName": "音视频",
                "collectInside": false,
                "courseId": 13,
                "desc": "",
                "envelopePic": "",
                "fresh": true,
                "id": 8824,
                "link": "https://juejin.im/post/5d42d4946fb9a06ae439d46b",
                "niceDate": "22分钟前",
                "origin": "",
                "prefix": "",
                "projectLink": "",
                "publishTime": 1564713410000,
                "superChapterId": 97,
                "superChapterName": "多媒体技术",
                "tags": [],
                "title": "Android 基于MediaCodec+MediaMuxer实现音视频录制合成",
                "type": 0,
                "userId": -1,
                "visible": 1,
                "zan": 0
            }, {
                "apkLink": "",
                "author": " coder-pig",
                "chapterId": 74,
                "chapterName": "反编译",
                "collectInside": false,
                "courseId": 13,
                "desc": "",
                "envelopePic": "",
                "fresh": true,
                "id": 8823,
                "link": "https://juejin.im/post/5d42f440e51d4561e53538b6",
                "niceDate": "33分钟前",
                "origin": "",
                "prefix": "",
                "projectLink": "",
                "publishTime": 1564712761000,
                "superChapterId": 74,
                "superChapterName": "热门专题",
                "tags": [],
                "title": "忘了他吧！我偷别人APP的代码养你",
                "type": 0,
                "userId": -1,
                "visible": 1,
                "zan": 0
            },
               .........
            ]
     * offset : 0
     * over : false
     * pageCount : 342
     * size : 20
     * total : 6840
     */
    private var curPage = 0
    private val offset = 0
    private val over = false
    private val pageCount = 0
    private val size = 0
    private val total = 0
    var datas: List<Article>? = null
    override fun toString(): String {
        return "CollectListBean(curPage=$curPage, collectList=$datas)"
    }

}