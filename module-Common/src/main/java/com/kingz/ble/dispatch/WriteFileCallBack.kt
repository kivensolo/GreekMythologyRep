package com.kingz.ble.dispatch

import com.kapplication.beidou.watch.ble.response.BleResponse
import com.kingz.ble.exception.BleException
import com.kingz.module.common.utils.HexUtil
import com.zeke.kangaroo.zlog.ZLog
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/**
 * 蓝牙分包写文件的回调
 * @param index 分包的index
 * @param size 分包的总数
 */
class WriteFileCallBack(private val size: Int, private val continuation: Continuation<Boolean>? = null):
    IBleResponseCallBack {
    private var index: Int = 0

    override fun onSuccess(response: BleResponse) {
        ZLog.d("write bin file Flash onResult(${(index + 1)}/${size}):[${HexUtil.formatHexString(response.result, true)}]")
        val isSuccess = true // 根据返回数据判断数据是否成功
        if (isSuccess) {
            if (index == (size - 1)) {
                continuation?.resume(true)
            }
            index++
        } else {
            continuation?.resume(false)
        }
    }

    override fun onError(exception: BleException) {
        ZLog.e("write bin file Flash onError:[${exception.reason}", exception)
        continuation?.resume(false)
    }
}