package com.kingz.ble.cons

@Suppress("MayBeConstant")
class BleMsg {
    companion object {
        // ------- Connect Event
        /** GATT 已连接 */
        val MSG_CONNECTED = 0x1001
        val MSG_DISCOVER_SUCCESS = 0x1002
        val MSG_DISCOVER_FAIL = 0x1003


        // ------- Scan Event
        val MSG_OF_SCAN_TIMEOUT = 0x2000

        // ------- Write Event
        val MSG_OF_WRITE_ERROR = 0x3000
        val MSG_OF_WRITE_OVER_TIME = 0x3001
        val MSG_OF_WRITE_FAILED_WHEN_NOT_CONNECT = 0x3002

        // ------- Erase Event
        val MSG_OF_ERASE_ERROR = 0x4000

        // ------- Setting Event
        val MSG_OF_SET_MTU = 0x5001

        // ------- Notifacation Event

        // ------- MTU EVENT
        val MTU_CHANGE_ILLEGAL = 0x6001
    }
}