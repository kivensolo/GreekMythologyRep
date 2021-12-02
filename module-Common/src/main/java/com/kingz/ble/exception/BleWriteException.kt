package com.kingz.ble.exception

import com.kingz.ble.cons.BleMsg


class BleWriteException(
    override var code: Int = BleMsg.MSG_OF_WRITE_ERROR,
    override var reason: String = "Data Write Exception"
) : BleException(code, reason)