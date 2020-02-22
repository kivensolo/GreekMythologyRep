package com.zeke.ktx.api


import android.content.Context
import com.zeke.ktx.api.callback.IDataResponse

interface DataApiService<T>{

    fun requestData(context: Context, callback: IDataResponse<T>)

}