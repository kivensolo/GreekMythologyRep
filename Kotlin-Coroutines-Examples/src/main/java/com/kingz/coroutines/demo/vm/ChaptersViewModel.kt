package com.kingz.coroutines.demo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kingz.coroutines.data.api.wandroid.WAndroidApi
import com.kingz.coroutines.demo.base.BaseViewModel
import com.kingz.coroutines.demo.entity.ChaptersEntity
import com.kingz.coroutines.utils.Resource
import com.zeke.kangaroo.utils.ZLog

class ChaptersViewModel(
        private val wAndroidApi: WAndroidApi
): BaseViewModel() {

    private val chaptersData = MutableLiveData<Resource<ChaptersEntity>>()

    init {
        launchDefault {
            ZLog.d("kingz","viewModelScope.launch(Dispatchers.Default)")
            chaptersData.postValue(Resource.loading(null))
            try {
                val result = wAndroidApi.fetchChapterData()
                chaptersData.postValue(Resource.success(result))
            } catch (e: Exception) {
                chaptersData.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getChapters(): LiveData<Resource<ChaptersEntity>> {
        return chaptersData
    }
}