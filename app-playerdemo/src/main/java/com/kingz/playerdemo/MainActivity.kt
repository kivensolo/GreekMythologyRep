package com.kingz.playerdemo

import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.kingz.playerdemo.sync.MediaSync
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


/**
 * 使用MediaCodec播放本地视频
 * https://blog.csdn.net/u011418943/article/details/107561111
 */
class MainActivity : AppCompatActivity() {

    private val TAG: String = "PlayerDemoActivity"
    private var isStreamEnd = false

    private var callback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            Log.i(TAG, "surfaceChanged")
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.i(TAG, "surfaceDestroyed")
//            workerThread?.interrupt()
//            workerThread = null
//            if (audioMediaCodecWorker != null) {
//                audioMediaCodecWorker!!.interrupt()
//                audioMediaCodecWorker = null
//            }
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.i(TAG, "surfaceCreated")
            // 音频解码
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        filePathUri = "android.resource://$packageName/${R.raw.welcome_video}"

        val mFile = File(externalCacheDir, "ChiLing.mp4")

        // 设置Surface不维护自己的缓冲区，等待屏幕的渲染引擎将内容推送到用户面前
        // 该api已经废弃，这个编辑会自动设置
        // surfaceView.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        val holder = surface_view.holder
        holder.addCallback(callback)

        val mediaSync = MediaSync(
            holder.surface,
            mFile.absolutePath
        )
        play_player.setOnClickListener {
            mediaSync.start()
        }
        pause_player.setOnClickListener {
            mediaSync.pause()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        isStreamEnd = true
    }
}
