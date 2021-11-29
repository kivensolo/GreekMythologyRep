package com.kingz.ble.dispatch

import com.kapplication.beidou.watch.ble.dispatch.IBleTask
import com.kingz.ble.model.IBleData
import com.kingz.ble.model.SimpleBleData

class BleSliceTask(
    var data: IBleData,
    var sliceNum :Int = 0,
    var callback: IBleResponseCallBack? = null,
) : IBleTask {

    var isSliceSuccess: Boolean = false

    override val taskName: String = if (data is SimpleBleData) {
        (data as SimpleBleData).realTaskName
    } else {
        data::class.java.simpleName
    }
}