package com.zeke.ktx.api


import android.content.Context
import com.zeke.network.response.IRequestResponse

interface DataApiService<T>{

    fun requestData(context: Context, callback: IRequestResponse<T>)

}