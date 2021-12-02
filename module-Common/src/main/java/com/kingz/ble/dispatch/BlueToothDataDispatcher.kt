@file:Suppress("PropertyName", "PrivatePropertyName", "FunctionName")

package com.kapplication.beidou.watch.ble.dispatch

import BleWriteTask
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.kapplication.beidou.watch.ble.response.BleResponse
import com.kingz.ble.cons.BleMsg
import com.kingz.ble.dispatch.BleSliceTask
import com.kingz.ble.dispatch.IBleManagerDelegate
import com.kingz.ble.dispatch.IBleResponseCallBack
import com.kingz.ble.exception.BleWriteException
import com.kingz.ble.model.IBleData
import com.kingz.ble.model.SimpleBleData
import com.zeke.kangaroo.zlog.ZLog
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * 蓝牙数据发送者，负责蓝牙数据分发
 * 【对外发送】手机对蓝牙设备发送数据，
 *           采用队列形式发送数据，可有效解决异步发送蓝牙命令丢失的问题
 * 【对内发送】手机接收到蓝牙设备数据后，自身内部的发送。
 */
open class BlueToothDataDispatcher(val delegate: IBleManagerDelegate) {
    private val TAG = "BLE_Dispatcher"

    @Volatile
    var _sendWorkers = arrayOfNulls<Thread>(1)
    private var dataWriteQueue: ConcurrentLinkedQueue<BleWriteTask> = ConcurrentLinkedQueue()
    private val sendWorkerWaitObj = Object()
    private val WRITE_WORK_NAME = "BLE Write Worker"

    @Volatile
    var _sliceWorkers = arrayOfNulls<Thread>(1)
    private var dataSliceQueue: ConcurrentLinkedQueue<BleSliceTask> = ConcurrentLinkedQueue()
    private val sliceWorkerWaitObj = Object()
    private val SLICE_WORK_NAME = "BLE Slice Worker"

    /**
     * 前序发送任务
     */
    private var preSendTask: BleWriteTask?= null

    /**
     * 前序分片任务
     */
    private var preSliceTask: BleSliceTask?= null

    /**
     * 前序数据写数据中标识
     */
    @Volatile
    private var dataWriting = false

    /**
     * 前序数据分片执行中
     */
    @Volatile
    private var preDataSlicing = false

    /**
     * 数据写入超时次数
     */
    private var waitCounts = 0
    private val DEBUG_MODE = false

    private var uiHandler:Handler = Handler(Looper.getMainLooper())
    private val DEFAULT_WRITE_DATA_SPLIT_COUNT = 20 //默认20 (23-3)
    private val UART_DATA_COUNT = 256
    /**
     * 当前可使用的MTU
     */
    private var currentBleMtu:Int = DEFAULT_WRITE_DATA_SPLIT_COUNT

    /**
     * URAT每包256
     */
    private var UART_PACKAGE_SIZE:Int = UART_DATA_COUNT

    private var sliceBufferResponseData = ByteArray(0)


    init {
        val bleThreadGroup = ThreadGroup("BLE")
        initSendWorker(bleThreadGroup)
        initSliceWorker(bleThreadGroup)
    }

    private fun initSendWorker(bleThreadGroup: ThreadGroup) {
        _sendWorkers[0] = object : Thread(bleThreadGroup, WRITE_WORK_NAME) {
            override fun run() {
                while (true) {
                    if (checkPreTaskTimeOut()) {
                        continue
                    }

                    if (preSendTask == null) {
                        preSendTask = getWritePendingTask()
                    }
                    val pendingTask = preSendTask
                    if (pendingTask == null) {
                        //                                TLog("No more data,Send-worker IDLE.")
                        sendThreadWait(Int.MAX_VALUE)
                        continue
                    } else {
                        //                                TLog("Send-worker is wake. Start send data.")
                        //                                changeMtuBeforeSendData(pendingTask)
                        TLog("\n")
                        dataWriting = true
                        delegate.onWriteDataDispatch(pendingTask)
                        if (pendingTask.isSliceTask) {
                            TLog("======>>> Send slice data task, is last slice? ${pendingTask.endFlag}")
                        }
                    }
                }
            }

            private fun checkPreTaskTimeOut(): Boolean {
                if (dataWriting) {
                    if (waitCounts >= 3) {
                        ZLog.w("PreData is write time out, drop.")
                        uiHandler.post {
                            preSendTask?.callback?.onError(
                                BleWriteException(code = BleMsg.MSG_OF_WRITE_OVER_TIME, "Data Write Exception")
                            )
                        }
                        resetWrite()
                        return true
                    }
//                    TLog("[CHECK]PreData is writing. Wait.....")
                    sendThreadWait(5000)
                    waitCounts++
                    return true
                }
                return false
            }
        }
        _sendWorkers[0]?.start()
    }

    private fun initSliceWorker(bleThreadGroup: ThreadGroup){
        _sliceWorkers[0] = object : Thread(bleThreadGroup, SLICE_WORK_NAME) {
            override fun run() {
                while (true) {
                    if(preDataSlicing){
                        sliceThreadWait(300)
                        continue
                    }
                    if (preSliceTask == null) {
                        preSliceTask = getSlicePendingTask()
                    }
                    val pendingTask = preSliceTask
                    if (pendingTask == null) {
                        TLog("No more data,Slice-worker IDLE.")
                        sliceThreadWait(Int.MAX_VALUE)
                        continue
                    } else {
//                        TLog("Slice-worker Run ,To split data. [CAPACITY]:${dataSliceQueue.size}")
                        preDataSlicing = true
                        splitData(
                            pendingTask.data, pendingTask.sliceNum, pendingTask.callback
                        )
                    }
                }
            }
        }
        _sliceWorkers[0]?.start()
    }

    /**
     * 启动下一组数据发送
     */
    private fun startNextTask(){
        resetWrite()
        notifySendWorker()
    }

    /**
     * 重置数据发送相关变量属性
     * 每一条数据发送结束后(成功、失败、超时),
     */
    private fun resetWrite(){
        preSendTask = null
        dataWriting = false
        waitCounts = 0
    }

    /**
     * 重置分片相关变量
     */
    private fun resetSlice(){
        preSliceTask = null
        preDataSlicing = false
    }


  // <editor-fold defaultstate="collapsed" desc="蓝牙事件回调">
    fun onMtuChanged(mtu: Int, status: Int){
        ZLog.d(TAG, "onMtuChanged() mtu=$mtu  status=$status (0 is success)")
        when(status){
            BluetoothGatt.GATT_SUCCESS -> { //MTU设置成功
                currentBleMtu = mtu - 3     // 减去蓝牙协议头的三个字节
            }
//            EventCode.MTU_CHANGE_ILLEGAL -> {
//                // 设置的mtu无效, 使用原有大小
//            }
            else->{}
        }
    }

    var sliceTotalResponse = ByteArray(UART_DATA_COUNT)

     /**
     * 手机--->外设 数据写入成功的回调
     */
    @Synchronized
    fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ){
        TLog("<====== onCharacteristicWrite status=$status")
        when(status){
            BluetoothGatt.GATT_SUCCESS -> {
                preSendTask?.apply {
                    isWriteSuccess = true
                    if(isSliceTask && !endFlag){
                        startNextTask() // send next slice data.
                    }
                }
            }
            else ->{
                preSendTask?.isWriteSuccess = false
                uiHandler.post {  preSendTask?.callback?.onError(BleWriteException())}
                startNextTask()
            }
        }
    }

    /**
     * 外设--->手机
     */
    @Synchronized
    fun onCharacteristicChanged(characteristic: BluetoothGattCharacteristic){
        TLog("<====== onDataChangedInCharacter response.")
//                    "[${HexUtil.formatHexString(characteristic.value, true)}]")
        if(preSendTask == null){
            // preTask为空却收到数据，表明是手表主动发消息
            // TODO 手表主动发送消息给手机
            return
        }
        preSendTask?.apply {
            responseCounts++
            TLog("Response onSuccess of [$taskName], isSliceTask:$isSliceTask")
            val result = characteristic.value
            if (isSliceTask) {
                inflateSliceResponse(result, this)
            } else {
                uiHandler.post {
                    callback?.onSuccess(BleResponse(result))
                }
            }
        }

        // task为endTask, 则数据响应时触发下一条数据的发送
        if(preSendTask?.canSendNextTask == true){
            startNextTask()
        }
    }

    /**
     * 填充分片返回数据
     */
    private fun inflateSliceResponse(
        result: ByteArray, writeTask: BleWriteTask
    ) {
//        TLog("InflateSliceResponse sliceCount:${writeTask.sliceCounts} currentId:${writeTask.sliceId}")
        val tempArray = ByteArray(sliceBufferResponseData.size + result.size)
        if(sliceBufferResponseData.isNotEmpty()){
            System.arraycopy(
                sliceBufferResponseData, 0,
                tempArray, 0,
                sliceBufferResponseData.size
            )
        }
        System.arraycopy(
            result, 0,
            tempArray, sliceBufferResponseData.size,
            result.size
        )
        sliceBufferResponseData = tempArray
        if (writeTask.canSendNextTask) {
            TLog("<<<====== Slice response onFinish of [${writeTask.taskName}] \n")
            uiHandler.post {
                writeTask.callback?.onSuccess(BleResponse(tempArray))
            }
            sliceBufferResponseData = ByteArray(0)
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="内部私有方法">

    private fun getWritePendingTask(): BleWriteTask?{
        synchronized(dataWriteQueue) {
            if (!dataWriteQueue.isEmpty()) {
                return dataWriteQueue.poll()
            }
        }
        return null
    }

    private fun notifySendWorker(){
        try {
            synchronized(sendWorkerWaitObj){
                sendWorkerWaitObj.notify()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun sendThreadWait(ms: Int){
        try {
            val name = Thread.currentThread().name
            if(TextUtils.equals(WRITE_WORK_NAME, name)){
                synchronized(sendWorkerWaitObj) {
                    sendWorkerWaitObj.wait(ms.toLong())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getSlicePendingTask(): BleSliceTask?{
        synchronized(dataSliceQueue) {
            if (!dataSliceQueue.isEmpty()) {
                return dataSliceQueue.poll()
            }
        }
        return null
    }

     private fun notifySliceWorker(){
        resetSlice()
        try {
            synchronized(sliceWorkerWaitObj){
                sliceWorkerWaitObj.notify()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun sliceThreadWait(ms: Int){
        try {
            val name = Thread.currentThread().name
            if(TextUtils.equals(SLICE_WORK_NAME, name)){
                synchronized(sliceWorkerWaitObj) {
                    sliceWorkerWaitObj.wait(ms.toLong())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="对外提供的公共方法">

    fun startSendData(
        data: IBleData, sliceMode: Boolean = true, callback: IBleResponseCallBack? = null
    ) {
        if (sliceMode) {
            //TODO 自定义分片大小
            val sliceNum = DEFAULT_WRITE_DATA_SPLIT_COUNT
            addSliceTaskToQueue(BleSliceTask(data, sliceNum, callback))
        } else {
            val bleWriteTask = BleWriteTask(data, callback)
            bleWriteTask.isSliceTask = false
            addWriteTaskToQueue(bleWriteTask)
        }
    }

    /**
     * Get the current MTU value.
     */
    fun getCurrentMTU():Int = currentBleMtu

    /**
     * 分片处理逻辑
     * @param dataObj 原始数据类型
     * @param count 单个分片数据包大小
     */
    private fun splitData(
        dataObj: IBleData,
        count: Int,
        callback: IBleResponseCallBack?){
        val byteQueue: Stack<ByteArray> = Stack()
        val data = dataObj.getBleBytes()
        if (count > DEFAULT_WRITE_DATA_SPLIT_COUNT) {
            TLogW("Be careful: every send package size > 20! Ensure that the MTU is greater than 23!")
        }
        val dataSize = data.size
        if (dataSize <= count) {  // //when slice pkg == 1
            TLog("Set slice mode, but data size <= slice pkg size. No need split data!")
            preDataSlicing = false
            val bleWriteTask = BleWriteTask(dataObj, callback)
            bleWriteTask.isSliceTask = false
            addWriteTaskToQueue(bleWriteTask)
            notifySliceWorker()
            return
        }
        preDataSlicing = true
        val pkgCount: Int = dataSize / count + 1 //when slice pkg >=2
        TLog("Package write task for slice mode, slice data counts: $pkgCount")
        for (i in 0 until pkgCount) {
            var dataPkg: ByteArray
            var slicePkgSize: Int
            if (i == pkgCount - 1) {
                //Is last data package
                slicePkgSize = if (dataSize % count == 0) count else dataSize % count
                dataPkg = ByteArray(slicePkgSize)
                System.arraycopy(
                    data, i * count,
                    dataPkg, 0, slicePkgSize
                )
            } else {
                dataPkg = ByteArray(count)
                System.arraycopy(
                    data, i * count,
                    dataPkg, 0, count
                )
            }
            byteQueue.add(dataPkg)
        }

        val size = byteQueue.size
        byteQueue.forEachIndexed { index, bytes ->
            val simpleBleData = SimpleBleData(bytes)
            simpleBleData.realTaskName = dataObj::class.java.simpleName
            val bleSendTask = BleWriteTask(simpleBleData, callback)
            bleSendTask.sliceCounts = pkgCount
            bleSendTask.sliceId = index
            if (index == (size - 1)) {
                preDataSlicing = false
                addWriteTaskToQueue(bleSendTask)
                notifySliceWorker()
            } else {
                bleSendTask.endFlag = false
                addWriteTaskToQueue(bleSendTask, false)
            }
        }
    }

    /**
     * Add a data sending task to the concurrent linked queue.
     * @param task Send task.
     * @param isNotify 是否通知等待队列进行数据发送
     *        通常情况下为true,分片模式下，除了最后加入的数据，其他为false
     */
    private fun addWriteTaskToQueue(task: BleWriteTask, isNotify: Boolean = true) {
        TLog("add DataSendTask[${task.taskName}] to Queue.... isNotify:$isNotify")
        dataWriteQueue.add(task)
        if (isNotify && !preDataSlicing && !dataWriting) {
            notifySendWorker()
        }
    }

    /**
     * Add a data slice task.
     */
    private fun addSliceTaskToQueue(task: BleSliceTask, isNotify: Boolean = true) {
        TLog("add SliceTask[${task.taskName}] to Queue. Has data slicing? $preDataSlicing")
        dataSliceQueue.add(task)
        if (isNotify && !preDataSlicing) {
            preDataSlicing = true
            notifySliceWorker()
        }
    }

//    @Synchronized
    fun clearWriteQueue(){
       dataWriteQueue.clear()
    }

    fun getWriteHeadData(): IBleData? {
        return if (dataWriteQueue.size > 0) {
            dataWriteQueue.peek()?.data
        } else {
            null
        }
    }

    // </editor-fold>
    inner class SendDataItem{
        var data:ByteArray = ByteArray(20)
    }

    private fun TLog(msg:String){
        if(DEBUG_MODE){ ZLog.d(TAG,msg) }
    }
    private fun TLogW(msg:String){
        if(DEBUG_MODE){ ZLog.w(TAG,msg) }
    }

    private fun TLogE(msg:String){
        if(DEBUG_MODE){ ZLog.e(TAG,msg) }
    }
}