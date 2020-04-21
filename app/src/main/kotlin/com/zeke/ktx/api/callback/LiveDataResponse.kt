package com.zeke.ktx.api.callback

import com.zeke.ktx.modules.player.entity.Live
import com.zeke.ktx.modules.player.entity.TimeTableData
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