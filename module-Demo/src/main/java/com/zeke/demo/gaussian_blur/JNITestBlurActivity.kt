package com.zeke.demo.gaussian_blur

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.module.common.router.RouterConfig
import com.zeke.demo.R
import com.zeke.demo.databinding.ActivityJniBlurTestBinding
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.utils.WildFireUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer

@Route(path = RouterConfig.PAGE_JNI_TEST_BLUR)
class JNITestBlurActivity : AppCompatActivity() {
    //常规模糊的模糊次数
    var blurCounts = 2
    //快速模糊时的半径范围
    var blurRadius = 5 //(不能超过宽高的一半)
    lateinit var uiBinding:ActivityJniBlurTestBinding

    private lateinit var srcBitmap: Bitmap
    private lateinit var srcBitmapPixelBytes:ByteBuffer
    private lateinit var blurBitmap:Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO inflate优化
        uiBinding = ActivityJniBlurTestBinding.inflate(LayoutInflater.from(this))
        setContentView(uiBinding.root)

        srcBitmap = BitmapFactory.decodeResource(resources, R.drawable.marvel_qiyi)
        uiBinding.targetPic.setImageBitmap(srcBitmap)
        srcBitmapPixelBytes = ByteBuffer.allocateDirect(srcBitmap.byteCount)

        blurBitmap = Bitmap.createBitmap(srcBitmap.width, srcBitmap.height, Bitmap.Config.ARGB_8888)

        uiBinding.blurSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            @SuppressLint("SetTextI18n")
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                blurCounts = seekBar.progress / 10
                blurRadius = seekBar.progress / 5 // MAX: 20 25的话，差不多300ms+
                uiBinding.textRadius.text = "${seekBar.progress}%"
                beginBlur()
            }
        })
    }

    private fun beginBlur() {
        lifecycleScope.launch(Dispatchers.IO) {
            val startMs = System.currentTimeMillis()
            val blurBitmap = blurBitmap(srcBitmap)
            if (blurBitmap != null) {
                withContext(Dispatchers.Main){
                    uiBinding.blurPic.setImageBitmap(blurBitmap)
                }
                ZLog.d("Native Blur TIME " + (System.currentTimeMillis() - startMs).toString() + "ms")
            } else {
                ZLog.e("Blur failed !!!!")
            }
        }
    }

    /**
     * 将bitmap做一次高斯模糊
     *
     * @param bitmap
     * @param apply 原有基础上应用模糊
     * @return
     */
    private fun blurBitmap(bitmap: Bitmap?, apply:Boolean = false): Bitmap? {
        if (bitmap == null) {
            return null
        }
        ZLog.d("Start blur blurRadius= $blurRadius")
//        synchronized(srcBitmapPixelBytes){
            if(srcBitmapPixelBytes.array().isEmpty()){
                srcBitmapPixelBytes = ByteBuffer.allocateDirect(bitmap.byteCount)
            }else{
                srcBitmapPixelBytes.clear()
            }
            val pxBuffer = srcBitmapPixelBytes
            bitmap.copyPixelsToBuffer(pxBuffer)
           val success =  WildFireUtils.doBlur(pxBuffer, bitmap.width, bitmap.height, blurCounts)
//            val success = WildFireUtils.doFastBlur(pxBuffer, bitmap.width, bitmap.height, blurRadius)
            pxBuffer.rewind()
            if(!success){
                return null
            }
            return if(apply){
                bitmap.copyPixelsFromBuffer(pxBuffer)
                bitmap
            }else{
                blurBitmap.copyPixelsFromBuffer(pxBuffer)
                blurBitmap
            }
//        }
    }
}
