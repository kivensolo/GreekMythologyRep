package com.zeke.eyepetizer.bean

/**
 * author: King.Z <br></br>
 * date:  2021/5/23 19:52 <br></br>
 * description: 首页栏目列表
 *
 * https://github.com/1136535305/Eyepetizer/wiki/%E5%BC%80%E7%9C%BC-API-%E6%8E%A5%E5%8F%A3%E5%88%86%E6%9E%90#tag5
 */

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
data class EyepetizerTabListInfo(
    val tabInfo: TabInfoBean
)

data class TabInfoBean(
    var defaultIdx: Int,
    var tabList: List<TabListBean>? = null
)

data class TabListBean(
    val id: Int,         //栏目id
    var name: String = "",        //栏目名称
    var apiUrl: String = "", //该栏目详情数据ApI接口Url
    var tabType: Int,
    var nameType: Int,
    var adTrack: Any? = null
)