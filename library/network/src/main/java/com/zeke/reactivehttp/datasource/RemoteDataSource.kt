package com.zeke.reactivehttp.datasource

import com.zeke.reactivehttp.bean.IHttpWrapBean
import com.zeke.reactivehttp.callback.RequestCallback
import com.zeke.reactivehttp.exception.BaseHttpException
import com.zeke.reactivehttp.exception.ServerCodeBadException
import com.zeke.reactivehttp.viewmodel.IUIActionEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

/**
 * @Author: leavesC
 * @Date: 2020/5/4 0:55
 * @Desc:
 *      对应APIServer的RemotoDataSource
 */
abstract class RemoteDataSource<Api : Any>(iUiActionEvent: IUIActionEvent?, apiServiceClass: Class<Api>)
    : BaseRemoteDataSource<Api>(iUiActionEvent, apiServiceClass) {

    fun <Data> enqueueLoading(apiFun: suspend Api.() -> IHttpWrapBean<Data>,
                              baseUrl: String = "",
                              callbackFun: (RequestCallback<Data>.() -> Unit)? = null): Job {
        return enqueue(apiFun = apiFun, showLoading = true, baseUrl = baseUrl, callbackWrapperFun = callbackFun)
    }

    /**
     * 进行网络任务分发
     * @param apiFun lambda网络请求的挂起函数
     * @param showLoading 是否分发loading事件
     * @param baseUrl 是否指明自定义的baseUrl，为空的话，使用子DataSource自定义的BaseUrl
     * @param callbackWrapperFun 包含请求回调函数的高阶函数的函数类型参数
     */
    fun <Data> enqueue(apiFun: suspend Api.() -> IHttpWrapBean<Data>,
                       showLoading: Boolean = false,
                       baseUrl: String = "",
                       callbackWrapperFun: (RequestCallback<Data>.() -> Unit)? = null): Job {
        return launchMain {
            val callback = if (callbackWrapperFun == null) {
                null
            } else {
                RequestCallback<Data>().apply {
                    callbackWrapperFun.invoke(this) //将callback函数对象传递给外部高阶函数
                 }
            }
            try {
                if (showLoading) {
                    showLoading(coroutineContext[Job])
                }
                callback?.onStart?.invoke()
                val response: IHttpWrapBean<Data>
                try {
                    response = apiFun.invoke(getApiService(baseUrl))
                    if (!response.httpIsSuccess) {
                        throw ServerCodeBadException(response)
                    }
                } catch (throwable: Throwable) {
                    handleException(throwable, callback)
                    return@launchMain
                }
                onGetResponse(callback, response.httpData)
            } finally {
                try {
                    callback?.onFinally?.invoke()
                } finally {
                    if (showLoading) {
                        dismissLoading()
                    }
                }
            }
        }
    }

    fun <Data> enqueueOriginLoading(apiFun: suspend Api.() -> Data,
                                    baseUrl: String = "",
                                    callbackFun: (RequestCallback<Data>.() -> Unit)? = null): Job {
        return enqueueOrigin(apiFun = apiFun, showLoading = true, baseUrl = baseUrl, callbackFun = callbackFun)
    }

    fun <Data> enqueueOrigin(apiFun: suspend Api.() -> Data,
                             showLoading: Boolean = false,
                             baseUrl: String = "",
                             callbackFun: (RequestCallback<Data>.() -> Unit)? = null): Job {
        return launchMain {
            val callback = if (callbackFun == null) null else RequestCallback<Data>().apply {
                callbackFun.invoke(this)
            }
            try {
                if (showLoading) {
                    showLoading(coroutineContext[Job])
                }
                callback?.onStart?.invoke()
                val response: Data
                try {
                    response = apiFun.invoke(getApiService(baseUrl))
                } catch (throwable: Throwable) {
                    handleException(throwable, callback)
                    return@launchMain
                }
                onGetResponse(callback, response)
            } finally {
                try {
                    callback?.onFinally?.invoke()
                } finally {
                    if (showLoading) {
                        dismissLoading()
                    }
                }
            }
        }
    }

    private suspend fun <Data> onGetResponse(callback: RequestCallback<Data>?, httpData: Data?) {
        callback?.let {
            withNonCancellable {
                callback.onSuccess?.let {
                    withMain {
                        it.invoke(httpData)
                    }
                }
                callback.onSuccessIO?.let {
                    withIO {
                        it.invoke(httpData)
                    }
                }
            }
        }
    }

    /**
     * 同步请求，可能会抛出异常，外部需做好捕获异常的准备
     * @param apiFun
     */
    @Throws(BaseHttpException::class)
    fun <Data> execute(
        apiFun: suspend Api.() -> IHttpWrapBean<Data>,
        baseUrl: String = ""
    ): Data? {
        return runBlocking {
            try {
                val asyncIO = asyncIO {
                    apiFun.invoke(getApiService(baseUrl))
                }
                val response = asyncIO.await()
                if (response.httpIsSuccess) {
                    return@runBlocking response.httpData
                }
                throw ServerCodeBadException(response)
            } catch (throwable: Throwable) {
                throw generateBaseExceptionReal(throwable)
            }
        }
    }

}