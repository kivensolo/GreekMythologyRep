package com.zeke.demo.gaussian_blur

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
    var blurCounts = 2
    lateinit var uiBinding:ActivityJniBlurTestBinding

    private lateinit var srcBitmap: Bitmap
    private lateinit var srcBitmapPixelBytes:ByteBuffer
    private lateinit var blurBitmap:Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        srcBitmap = BitmapFactory.decodeResource(resources, R.drawable.p0)
        srcBitmapPixelBytes = ByteBuffer.allocateDirect(srcBitmap.byteCount)

        //TODO inflate优化
        uiBinding = ActivityJniBlurTestBinding.inflate(LayoutInflater.from(this))
        setContentView(uiBinding.root)
        blurBitmap = Bitmap.createBitmap(srcBitmap.width, srcBitmap.height, Bitmap.Config.ARGB_8888)

        uiBinding.blurSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blurCounts = progress / 10
                beginBlur()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        val function: (v: View) -> Unit = {
            beginBlur()
        }
        uiBinding.blurButton.setOnClickListener(function)
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
                ZLog.e("blur failed !!!!")
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
        if(srcBitmapPixelBytes.array().isEmpty()){
            srcBitmapPixelBytes = ByteBuffer.allocateDirect(bitmap.byteCount)
        }
        val pxBuffer = ByteBuffer.wrap(srcBitmapPixelBytes.array())
        bitmap.copyPixelsToBuffer(pxBuffer)
        WildFireUtils.doBlur(pxBuffer, bitmap.width, bitmap.height, blurCounts)
        pxBuffer.rewind()
        return if(apply){
            bitmap.copyPixelsFromBuffer(pxBuffer)
            bitmap
        }else{
            blurBitmap.copyPixelsFromBuffer(pxBuffer)
            blurBitmap
        }
    }
}
