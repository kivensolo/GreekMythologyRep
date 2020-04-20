package com.zeke.ktx.api

import android.content.Context
import com.google.gson.Gson
import com.zeke.ktx.api.callback.IDataResponse
import com.zeke.ktx.modules.player.entity.Live
import com.zeke.ktx.modules.player.entity.LiveDataDto

/**
 * @description：直播数据服务获取的实现类
 */
@Suppress("DEPRECATION")
class LiveDataProvider constructor() : DataApiService<MutableList<Live>> {
    fun getPlayUrl(context: Context, url: String, callback: IDataResponse<Any>) {
    }

    private lateinit var mCallBack: IDataResponse<MutableList<Live>>

    /**
     * 获取电视直播所有的电视台
     * @param context Context
     * @param callback DataApiService.IDataResponse
     */
    override fun requestData(context: Context,
                             callback: IDataResponse<MutableList<Live>>) {
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