package com.zeke.ktx.api.callback

import com.zeke.ktx.modules.player.entity.Live
import com.zeke.ktx.modules.player.entity.TimeTableData

/**
 * author：KingZ
 * date：2020/2/22
 * description：直播数据回调接口
 */
interface LiveDataResponse :IDataResponse<MutableList<Live>>{
    fun onTimeTables(data: MutableList<TimeTableData>?)
    fun onPlayUrl(url:String)
}