package com.zeke.home.api

import android.content.Context
import com.google.gson.Gson
import com.zeke.home.entity.Live
import com.zeke.home.entity.LiveDataDto
import com.zeke.network.response.IRequestResponse

/**
 * @description：直播数据服务获取的实现类
 */
@Suppress("DEPRECATION")
class LiveDataProvider : DataApiService<MutableList<Live>> {
    fun getPlayUrl(context: Context, url: String, callback: IRequestResponse<Any>) {
    }

    private lateinit var mCallBack: IRequestResponse<MutableList<Live>>

    /**
     * 获取电视直播所有的电视台
     * @param context Context
     * @param callback DataApiService.IDataResponse
     */
    override fun requestData(context: Context,
                             callback: IRequestResponse<MutableList<Live>>) {
        mCallBack = callback
        val assetManager = context.assets
        val inputStream = assetManager.open("live.json")
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)
        inputStream.close()
        val json = String(byteArray)
        val data = Gson().fromJson(json, LiveDataDto::class.java)
        if (data.lives != null) {
            callback.onSuccess(data.lives)
        }
    }
}