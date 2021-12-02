
import com.kapplication.beidou.watch.ble.dispatch.IBleTask
import com.kingz.ble.dispatch.IBleResponseCallBack
import com.kingz.ble.model.IBleData
import com.kingz.ble.model.SimpleBleData

class BleWriteTask(
    var data: IBleData,
    var callback: IBleResponseCallBack? = null,
) : IBleTask {
    var isWriteSuccess: Boolean = false

    private var name = "Task-"

    init {
        name += if (data is SimpleBleData) {
            (data as SimpleBleData).realTaskName
        } else {
            data::class.java.simpleName
        }
    }

    /**
     * 数据发送模式是否是分片模式
     */
    var isSliceTask:Boolean = true
    var sliceCounts = 1
    var responseCounts = 0
    /**
     * 分片任务id （Start from 0)
     */
    var sliceId = 0

    /**
     * 是否是单个数据的结尾
     * 分片数据中的最后一条为true,其余为false
     */
    var endFlag: Boolean = true

    val canSendNextTask:Boolean
        get() = responseCounts == sliceCounts

    override val taskName: String
        get() = name
}