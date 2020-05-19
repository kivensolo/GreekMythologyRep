package com.zeke.home.callback

import com.zeke.home.entity.Live
import com.zeke.home.entity.TimeTableData
import com.zeke.network.response.IRequestResponse

/**
 * author：KingZ
 * date：2020/2/22
 * description：直播数据回调接口
 */
interface LiveDataResponse : IRequestResponse<MutableList<Live>> {
    fun onTimeTables(data: MutableList<TimeTableData>?)
    fun onPlayUrl(url:String)
}