package com.zeke.ktx.player.service

import android.content.Context
import com.google.gson.Gson
import com.zeke.ktx.player.entity.Live
import com.zeke.ktx.player.entity.LiveDataDto

/**
 * @description：直播数据服务获取的实现类
 */
@Suppress("DEPRECATION")
class LiveApiServiceImpl constructor() : DataApiService<Live> {
    fun getPlayUrl(context: Context, url: String, callback: DataApiService.IDataCallback<Any>) {
    }

    private lateinit var mCallBack: DataApiService.IDataCallback<Live>

    /**
     * 获取电视直播所有的电视台
     * @param context Context
     * @param callback DataApiService.IDataCallback
     */
    override fun requestData(context: Context,
                             callback: DataApiService.IDataCallback<Live>) {
        mCallBack = callback
        val assetManager = context.assets
        val inputStream = assetManager.open("live.json")
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)
        inputStream.close()
        val json = String(byteArray)
        val data = Gson().fromJson(json, LiveDataDto::class.java)
        if (data.lives != null) {
            callback.onResult(data.lives)
        }
    }

}