package com.kapplication.beidou.watch.ble.dispatch

import com.kapplication.beidou.watch.ble.response.BleResponse
import com.kapplication.beidou.watch.ble.response.ResponseOfFlashWrite
import com.kapplication.beidou.watch.ble.util.HexUtil
import com.kapplication.common.log.KLog
import com.kingz.ble.dispatch.IBleResponseCallBack
import com.kingz.ble.exception.BleException
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
        KLog.d("write bin file Flash onResult(${(index + 1)}/${size}):[${HexUtil.formatHexString(response.result, true)}]")
        val result = ResponseOfFlashWrite(response.result)
        if (result.isSuccess()) {
            if (index == (size - 1)) {
                continuation?.resume(true)
            }
            index++
        } else {
            continuation?.resume(false)
        }
    }

    override fun onError(exception: BleException) {
        KLog.e("write bin file Flash onError:[${exception.reason}", exception)
        continuation?.resume(false)
    }
}