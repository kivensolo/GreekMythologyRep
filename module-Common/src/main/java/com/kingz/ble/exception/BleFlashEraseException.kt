package com.kingz.ble.exception

import com.kingz.ble.cons.BleMsg

class BleFlashEraseException(
    override var code: Int = BleMsg.MSG_OF_ERASE_ERROR,
    override var reason: String = "Flash Erase Exception"
) : BleException(code, reason)