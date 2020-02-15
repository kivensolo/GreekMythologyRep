package com.zeke.ktx.player.service


import android.content.Context
import com.zeke.ktx.player.entity.TimeTableData

interface DataApiService<T>{

    fun requestData(context: Context, callback: IDataCallback<T>)

    interface IDataCallback<T>{
        fun onResult(data: MutableList<T>?)
        fun onTimeTables(data: MutableList<TimeTableData>?)
        fun onPlayUrl(url:String)
    }
}