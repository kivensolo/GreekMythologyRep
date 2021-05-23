package com.zeke.music.viewmodel

import androidx.lifecycle.MutableLiveData
import com.zeke.kangaroo.utils.ZLog
import com.zeke.music.bean.RelatedVideoInfo
import com.zeke.music.bean.VideoInfo
import com.zeke.music.repository.YinYueTaiRemoteDataSource
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import kotlinx.coroutines.Job

/**
 * 音乐台ViewModel
 */
open class MusicViewModel : BaseReactiveViewModel() {

    val relatedVideoListLiveData: MutableLiveData<List<RelatedVideoInfo>> by lazy {
        MutableLiveData<List<RelatedVideoInfo>>()
    }


    val videoInfoLiveData: MutableLiveData<VideoInfo> by lazy {
        MutableLiveData<VideoInfo>()
    }

    // 需限定访问权限为protected 防止view层直接访问Model层的DataSource对象
    protected open val remoteDataSource by lazy {
        YinYueTaiRemoteDataSource(this)
    }

    /**
     * 获取影片信息
     */
    fun getVideoInfo(videoId:Int){
        launchIO {
            ZLog.d(TAG, "Get video info with id:{$videoId}")
            val videoInfo = remoteDataSource.getVideoInfo(videoId)
            videoInfoLiveData.postValue(videoInfo)
        }
    }

    /**
     * 获取影片推荐信息
     */
    fun getReleatedVideoList(videoId:Int){
        launchIO {
            ZLog.d(TAG, "Get video info with id:{$videoId}")
            val videoInfo = remoteDataSource.getRelatedVideoList(videoId)
            relatedVideoListLiveData.postValue(videoInfo)
        }
    }

    // 顶层接口方法
    override fun showLoading(job: Job?) {
        super.showLoading(job)
    }

    override fun dismissLoading() {
        super.dismissLoading()
    }

    override fun showToast(msg: String) {
        super.showToast(msg)
    }

    override fun finishView() {
        super.finishView()
    }
}