package com.zeke.eyepetizer.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.kingz.database.AppDatabase
import com.kingz.module.common.CommonApp
import com.zeke.eyepetizer.bean.EyepetizerTabListInfo
import com.zeke.eyepetizer.bean.EyepetizerTabPageData
import com.zeke.eyepetizer.bean.VideoDetailMergeData
import com.zeke.eyepetizer.respository.EyepetizerRemoteDataSource
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import kotlinx.coroutines.Job
import retrofit2.http.Url

/**
 * 开眼视频的ViewModel
 */
open class EyepetizerViewModel : BaseReactiveViewModel() {
    protected val db: AppDatabase?

    init {
        db = CommonApp.getInstance().getDataBase()
    }

    val tabListLiveData: MutableLiveData<EyepetizerTabListInfo> by lazy {
        MutableLiveData<EyepetizerTabListInfo>()
    }

    val tabPageDetailLiveData: MutableLiveData<EyepetizerTabPageData> by lazy {
        MutableLiveData<EyepetizerTabPageData>()
    }

    val detailPageLiveData: MediatorLiveData<VideoDetailMergeData> by lazy {
        MediatorLiveData<VideoDetailMergeData>()
    }


    // 需限定访问权限为protected 防止view层直接访问Model层的DataSource对象
    protected open val remoteDataSource by lazy {
        EyepetizerRemoteDataSource(this)
    }

    fun getTabList() {
        launchIO {
            ZLog.d(TAG,"Get tab list.")
            val info = remoteDataSource.getTabList()
            tabListLiveData.postValue(info)
        }
    }

    fun getTabPageDetail(@Url url:String) {
        launchIO {
            val data = remoteDataSource.getTabPageDetail(url)
            ZLog.d(TAG,"Get tab page detail. with data.")
            tabPageDetailLiveData.postValue(data)
        }
    }

    fun getVideoDetailAndReleatedData(videoId: String){
        launchIO {
            val detailTask = asyncIO { remoteDataSource.getVideoDetailData(videoId) }
            val relatedTask = asyncIO { remoteDataSource.getVideoRelatedData(videoId) }
            val result = VideoDetailMergeData(detailTask.await(), relatedTask.await())
            detailPageLiveData.postValue(result)
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