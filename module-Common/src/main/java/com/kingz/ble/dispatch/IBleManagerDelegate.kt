package com.kapplication.beidou.watch.ble.dispatch

/**
 * 回调线程都是子线程
 */
interface IBleManagerDelegate {
    fun onWriteDataDispatch(task: BleWriteTask) {}
    fun onWriteDataFinish() {}
}