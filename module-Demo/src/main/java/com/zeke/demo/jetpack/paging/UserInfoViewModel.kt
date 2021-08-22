package com.zeke.demo.jetpack.paging

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zeke.reactivehttp.base.BaseReactiveViewModel

/**
 * author: King.Z <br>
 * date:  2021/8/22 21:09 <br>
 * description: 在ViewModel中创建Pager实例 <br>
 */
class UserInfoViewModel(private val apiService: ReqresApiService) : BaseReactiveViewModel() {
    // 1. PagingConfig用来对Pager进行配置，pageSize表示每页加载Item的数量，
    // 这个size一般推荐要超出一屏显示的item数量
    val listData = Pager(PagingConfig(pageSize = 18)) {
           ReqresDatasource(apiService)
        }.flow  // 2  .flow表示结果由LiveData转为Flow
        .cachedIn(viewModelScope) // 3. cachedIn表示将结果缓存到viewModelScope，在ViewModel的onClear之前将一直存在

}