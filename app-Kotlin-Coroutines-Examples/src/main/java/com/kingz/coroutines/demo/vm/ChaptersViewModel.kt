package com.kingz.coroutines.demo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kingz.base.response.ResponseResult
import com.kingz.coroutines.data.api.wandroid.WAndroidApi
import com.kingz.coroutines.demo.base.BaseViewModel
import com.kingz.coroutines.demo.entity.ChaptersEntity
import com.zeke.kangaroo.utils.ZLog

class ChaptersViewModel(
        private val wAndroidApi: WAndroidApi
): BaseViewModel() {

    private val chaptersData = MutableLiveData<ResponseResult<ChaptersEntity>>()

    init {
        launchDefault {
            ZLog.d("kingz","viewModelScope.launch(Dispatchers.Default)")
            chaptersData.postValue(ResponseResult.loading(null))
            try {
                val result = wAndroidApi.fetchChapterData()
                chaptersData.postValue(ResponseResult.success(result))
            } catch (e: Exception) {
                chaptersData.postValue(ResponseResult.error(e.toString(), null))
            }
        }
    }

    fun getChapters(): LiveData<ResponseResult<ChaptersEntity>> {
        return chaptersData
    }
}