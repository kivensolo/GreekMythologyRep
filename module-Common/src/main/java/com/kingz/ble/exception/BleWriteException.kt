package com.kapplication.beidou.watch.ble.exception

import com.kapplication.beidou.watch.ble.cons.BleMsg
import com.kingz.ble.exception.BleException

class BleWriteException(
    override var code: Int = BleMsg.MSG_OF_WRITE_ERROR,
    override var reason: String = "Data Write Exception"
) : BleException(code, reason)