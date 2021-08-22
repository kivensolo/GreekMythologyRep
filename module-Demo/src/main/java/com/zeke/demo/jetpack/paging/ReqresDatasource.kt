package com.zeke.demo.jetpack.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * author: King.Z <br>
 * date:  2021/8/22 19:03 <br>
 * description: Reqres 提供的mock数据源 <br>
 *
 *     https://reqres.in/
 */

class ReqresDatasource(private val apiService: ReqresApiService) : PagingSource<Int, Data>()  {

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition //用户的滚动位置锚点
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        try {
            //param.key为空时，默认加载第1页数据
            val currentLoadingPageKey = params.key ?: 1
            val response = apiService.getUserListData(currentLoadingPageKey)

            val responseData = mutableListOf<Data>()
            val data = response.body()?.data ?: emptyList()
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}