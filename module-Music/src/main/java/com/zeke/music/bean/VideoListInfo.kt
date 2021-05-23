package com.zeke.music.bean

/**
 * author：ZekeWang
 * date：2021/5/14
 * description：音悦台影片列表数据
 */
class VideoListInfo {
    /**
     * pageNum : 1
     * pageSize : 20
     * totalCount : 30
     * list :
     * [
     *   {
     *       "id": 71,
     *       "videoName": "留什么给你 - 中国之星 现场版[超清版]",
     *       "videoDesc": "留什么给你 - 中国之星 现场版[超清版]",
     *       "artistName": "孙楠",
     *       "videoImg": "https://img.yinyuetai.com/img/26f2e979bfee4a93958ee0b6d98d26c4.png"
     *   },
     *   {
     *       "id": 70,
     *       "videoName": "不说再见 电影《谁的青春不迷茫》主题曲 剧情版[超清版]",
     *       "videoDesc": "不说再见 电影《谁的青春不迷茫》主题曲 剧情版[超清版]",
     *       "artistName": "许飞",
     *       "videoImg": "https://img.yinyuetai.com/img/7f9550d5ad6645e49a6cf820ceb7a921.png"
     *   }
     * ]
     */
    var pageNum = 0
    var pageSize = 0
    var totalCount = 0
    var list: List<ListBean>? = null

    class ListBean {
        /**
         * id : 71
         * videoName : 留什么给你 - 中国之星 现场版[超清版]
         * videoDesc : 留什么给你 - 中国之星 现场版[超清版]
         * artistName : 孙楠
         * videoImg : https://img.yinyuetai.com/img/26f2e979bfee4a93958ee0b6d98d26c4.png
         */
        var id = 0
        var videoName: String? = null
        var videoDesc: String? = null
        var artistName: String? = null
        var videoImg: String? = null

    }
}