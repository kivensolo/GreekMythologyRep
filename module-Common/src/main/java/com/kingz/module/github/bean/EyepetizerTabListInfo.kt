package com.kingz.module.github.bean

/**
 * author: King.Z <br></br>
 * date:  2021/5/23 19:52 <br></br>
 * description: 开眼视频分类列表数据 <br></br>
 *
 * https://github.com/1136535305/Eyepetizer/wiki/%E5%BC%80%E7%9C%BC-API-%E6%8E%A5%E5%8F%A3%E5%88%86%E6%9E%90#tag5
 *
 *
 */
class EyepetizerTabListInfo {
    /**
     * {
     * "tabInfo": {
     *     "tabList": [
     *         {
     *             "id": -1,
     *             "name": "发现",
     *             "apiUrl": "http://baobab.kaiyanapp.com/api/v5/index/tab/discovery",
     *             "tabType": 0,
     *             "nameType": 0,
     *             "adTrack": null
     *         },
     *         {
     *             "id": -2,
     *             "name": "推荐",
     *             "apiUrl": "http://baobab.kaiyanapp.com/api/v5/index/tab/allRec?page=0",
     *             "tabType": 0,
     *             "nameType": 0,
     *             "adTrack": null
     *         }
     *         .......
     *     ],
     *     "defaultIdx": 1
     * }
     */
    var tabInfo: TabInfoBean? = null

    class TabInfoBean {
        var defaultIdx = 0
        var tabList: List<TabListBean>? = null

        class TabListBean {
            //栏目id
            var id = 0
            //栏目名称
            var name: String = ""
            //该栏目详情数据ApI接口Url
            var apiUrl: String = ""
            var tabType = 0
            var nameType = 0
            var adTrack: Any? = null
            override fun toString(): String {
                return "TabListBean(id=$id, name='$name', apiUrl='$apiUrl', " +
                        "tabType=$tabType, nameType=$nameType, adTrack=$adTrack)"
            }
        }
        override fun toString(): String {
            return "TabInfoBean(defaultIdx=$defaultIdx, tabList=$tabList)"
        }
    }

    override fun toString(): String {
        return "EyepetizerTabListInfo(tabInfo=$tabInfo)"
    }
}