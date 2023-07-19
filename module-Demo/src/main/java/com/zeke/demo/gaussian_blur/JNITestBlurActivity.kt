package com.zeke.demo.gaussian_blur

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.module.common.router.RouterConfig
import com.starcor.xul.XulUtils
import com.zeke.demo.R
import com.zeke.demo.databinding.ActivityJniBlurTestBinding
import com.zeke.kangaroo.zlog.ZLog
import java.nio.ByteBuffer

@Route(path = RouterConfig.PAGE_JNI_TEST_BLUR)
class JNITestBlurActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jni_blur_test)
        val uiBinding = ActivityJniBlurTestBinding.inflate(LayoutInflater.from(this))
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.p0)

        val function: (v: View) -> Unit = {
            val startMs = System.currentTimeMillis()
            val blurBitmap = blurBitmap(bitmap)

            if (blurBitmap != null) {
                uiBinding.blurPic.setImageBitmap(blurBitmap)
                Log.d(getString(R.string.app_name),
                    "Native Blur TIME " + (System.currentTimeMillis() - startMs).toString() + "ms"
                )
            } else {
                ZLog.e("blur failed !!!!")
            }
        }
        findViewById<View>(R.id.blur_button).setOnClickListener(function)
    }

    /**
     * 将bitmap做一次高斯模糊
     *
     * @param bitmap
     * @return
     */
    fun blurBitmap(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) {
            return null
        }
        val pixelsBuf = ByteBuffer.allocateDirect(bitmap.byteCount)
        bitmap.copyPixelsToBuffer(pixelsBuf)
        XulUtils.doBlurOnBuffer(pixelsBuf, bitmap.width, bitmap.height, 2)
//        MyNativeUtils.doFastBlur(pixelsBuf, bitmap.width, bitmap.height, 2)
        pixelsBuf.rewind()
        bitmap.copyPixelsFromBuffer(pixelsBuf)
        return bitmap
    }
}
