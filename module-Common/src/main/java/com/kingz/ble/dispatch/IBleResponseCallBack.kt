package com.kingz.ble.dispatch

import com.kapplication.beidou.watch.ble.response.BleResponse
import com.kingz.ble.exception.BleException

/**
 * author：ZekeWang
 * date：2021/8/12
 * description：Bluetooth send data callback interface.
 *
 * Event callback on UI thread.
 */
interface IBleResponseCallBack {
    /**
     * @param response  Response Data object.
     *
     * Get response byteArray data through char.value；
     */
    fun onSuccess(response: BleResponse)

    /**
     * @param exception Exception of reponse.
     *
     *  <p>NOTICE:</p>
     *     When timeout occurs, the internal controller will automatically send subsequent data.
     */
    fun onError(exception: BleException)
}