package com.kingz.file

import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.base.BaseSimpleActivity
import com.kingz.customdemo.databinding.DownloadDemoLayoutBinding
import com.kingz.module.common.router.RouterConfig
import com.kingz.utils.ToastTools
import com.kingz.work.FileDownloadWorker
import com.module.views.progress.HorizontalProgressBarNoNumber
import com.zeke.kangaroo.utils.NetUtils
import com.zeke.kangaroo.utils.ScreenDisplayUtils
import com.zeke.kangaroo.zlog.ZLog
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * workmanager文件下载测试
 *
 */
@Route(path = RouterConfig.PAGE_DEMO_WORK_MANAGER)
class WorkManagerDemoActivity : BaseSimpleActivity() {
    private val updateLiveData = MutableLiveData<String>()
    override fun initData(savedInstanceState: Bundle?) {
        updateLiveData.observe(this, { })
    }

    override fun initViewModel() {}
    private val appUrlPath =
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0143e35b513afca80121ade05df851.jpg%401280w_1l_2o_100sh.jpg&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1638779197&t=b11b387ede60a4f6c6771c0d8c0b770b"
    private var mProgressBarView: HorizontalProgressBarNoNumber? = null
    private lateinit var binding: DownloadDemoLayoutBinding
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initProgressView()
        binding.btnDownApp.setOnClickListener {
            ZLog.d("开始下载....")
            startDownLoadFile(appUrlPath)
        }
    }

    override fun getContentView(): View? {
        binding = DownloadDemoLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    /**
     * 进度条初始化
     */
    private fun initProgressView() {
        mProgressBarView = HorizontalProgressBarNoNumber(this)
        mProgressBarView?.apply {
            setTotalWidth(ScreenDisplayUtils.getScreenWidth(this@WorkManagerDemoActivity) / 2)
            setInnerPaintColor(-0x62450e)
            setOuterPaintColor(0x19ffffff)
            setProgressCompleteListener {
                ZLog.d("Download OK.")
                mProgressBarView?.visibility = View.INVISIBLE
            }
        }
        val lps = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addContentView(mProgressBarView, lps)
        mProgressBarView?.beginDraw()
    }

    /**
     * 文件下载初始化
     */
    private fun startDownLoadFile(downloadUrl: String) {
        val url = downloadUrl.trim { it <= ' ' }
        if (TextUtils.isEmpty(url)) {
            ZLog.e("url is empty!")
            ToastTools.i().showToast(this, "url is empty!")
            return
        }
        if (!NetUtils.isConnect(this)) {
            ZLog.e("网络未连接,无法进一步下载")
            ToastTools.i().showCustomToastByType(this, "url is empty!", ToastTools.ToastType.MGTV)
            return
        }
        //        Java方式连接网络
//        connectByJava(url);
        createFileDir() //确定文件保存路径
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiresStorageNotLow(true)
            .build()
        val urlData = Data.Builder()
            .putString(FileDownloadWorker.KEY_ADDR_URI, url)
            .build()
        val fileDownloadRequest = OneTimeWorkRequest.Builder(FileDownloadWorker::class.java)
            .setInitialDelay(2, TimeUnit.SECONDS) // 设置执行延迟
            .setConstraints(constraints) //设置约束
            .setInputData(urlData) // 设置输入参数(最大10KB)
            .addTag("download") // 设置Worker的Tag
            .build()
        WorkManager.getInstance(this).enqueue(fileDownloadRequest)
        WorkManager.getInstance(this).getWorkInfosByTagLiveData("download")
            .observe(this, Observer { workInfos ->
                    if (workInfos == null || workInfos.size == 0) {
                        return@Observer
                    }
                    val workInfo = workInfos[0]
                    val outputData = workInfo.outputData
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        ToastTools.i().showToast(baseContext, outputData.getString("result"))
                    }
                } // 目前1.0.1的版本不支持观察worker工作进程中的功能。 在2.3.0-alpha01版本才支持
            )
    }

    private fun createFileDir(): File {
        val filePath = Environment.getExternalStorageDirectory().toString() + "/TempFile"
        val path = File(filePath)
        if (!path.exists()) {
            path.mkdir()
        }
        val fileName = "file_sss.jpg"
        val fileSavePath = "$filePath/$fileName"
        val downloadFile = File(fileSavePath)
        if (!downloadFile.exists()) {
            try {
                downloadFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return downloadFile
    }

    companion object {
        const val TAG = "DownloadAPPActivity"
    }
}